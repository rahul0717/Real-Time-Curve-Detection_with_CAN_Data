import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class VehicleSensorsDataParser {

	// String used to identify start time data in CANMessage file
	private static final String KEY_START_TIME = "Start time:";
	// String used to identify GPS data in GPX Track file
	private static final String KEY_GPS_DATA = "new GLatLng(";
	// String used to split hex data.
	private static final String DEFAULT_HEX_DATA_SPLITTER = " ";
	private static final String MESSAGE_TYPE_RX = "Rx";

	private String canDumpFileName;
	private String gpsDataFileName;

	VehicleSensorsDataParser(String canDumpFileName, String gpsDataFileName) {
		this.canDumpFileName = canDumpFileName;
		this.gpsDataFileName = gpsDataFileName;
	}
	
	/**  
	 * Parses Gps Data file and populates SensorReadings from File.
	 * @param vechicleSensorsData
	 */
	public void parseGpsData(VehicleSensorsData vechicleSensorsData) {
		System.out.println("Parsing Gps Data");
		List<SensorReading> sensorReadings = vechicleSensorsData.getSensorReadings();
		// If not initialized create new ArrayList.
		if(sensorReadings == null) {
			sensorReadings = new ArrayList<>();
			vechicleSensorsData.setSensorReadings(sensorReadings);
		}
		
		// Read File line by line
		try (BufferedReader br = new BufferedReader(new FileReader(this.gpsDataFileName))) {
			int offset = 0;
			// Flag used to stop loop when all GpsData is read. 
			// This avoids reading lines in file with keyword "new GLatLng(" does not have coordinates which is in js for computation)
			boolean started = false;
			for (String line; (line = br.readLine()) != null;) {
				// Use KEY_GPS_DATA to check if line has Gps Coordinates. 
				if(line.contains(KEY_GPS_DATA)) {
					started = true;
					// Extracting coordinates from line by removing unwanted parts of the line.
					String coordinates = line.substring(line.indexOf(KEY_GPS_DATA) + KEY_GPS_DATA.length(), line.indexOf(")"));
					String[] coordinatesSplit = coordinates.split(",");
					float latitude = Float.parseFloat(coordinatesSplit[0].trim());
					float longitude = Float.parseFloat(coordinatesSplit[1].trim());
					// Create a GPS sensor data
					GpsLocationData gpsLocationData = new GpsLocationData(latitude, longitude);
					SensorData gpsSensorData = new GpsSensorData();
					gpsSensorData.setValue(gpsLocationData);
					SensorReading sensorReading = new SensorReading(offset+1, offset, MESSAGE_TYPE_RX, "", 0, "", gpsSensorData, "degree", SensorReadingTypes.GPS_LOCATION);
					sensorReadings.add(sensorReading);
					// Since GPS Data is received every 1 second, offset is incremented by 1000ms for each reading.
					offset+= 1000;
				} else {
					if(started) {
						break;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses CANmessage file and populates SensorReadings from File.
	 * 
	 * @param vechicleSensorsData
	 */
	public void parseCanDump(VehicleSensorsData vechicleSensorsData) {
		System.out.println("Parsing CAN Messages Data");
		// If not initialized create new ArrayList.
		List<SensorReading> sensorReadings = vechicleSensorsData.getSensorReadings();
		if(sensorReadings == null) {
			sensorReadings = new ArrayList<>();
			vechicleSensorsData.setSensorReadings(sensorReadings);
		}
		// Read File line by line
		try (BufferedReader br = new BufferedReader(new FileReader(this.canDumpFileName))) {
			for (String line; (line = br.readLine()) != null;) {
				// Lines without ";" contain sensor data.
				if (line.charAt(0) != ';') {
					// Get all sensor reads in frame ( Each frame can have multiple sensor readings)
					List<SensorReading> sensorReadingsInFrame = getSensorReadingsInFrame(line);
					if (sensorReadingsInFrame == null) {
						continue;
					}
					sensorReadings.addAll(sensorReadingsInFrame);
				} else {
					// Using KEY_START_TIME to identify line with startTime and extracting information. 
					if (line.contains(KEY_START_TIME)) {
						vechicleSensorsData.setStartTime(
								line.substring(line.indexOf(KEY_START_TIME) + KEY_START_TIME.length()).trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses line, reads information from frame, decodes the data in the line.
	 * Each line can hava data for multiple sensors. 
	 * @param line
	 * @return A list of sensor readings in given line.
	 */
	private List<SensorReading> getSensorReadingsInFrame(String line) {
		// Split by " " can lead to empty array values which should be ignored. 
		String[] dataAsWords = line.split(" ");
		int idx = 0;
		String hexDataSeparator = "";
		int messageNumber = 0, dataLength = 0;
		float timeOffset = 0f;
		String type = "Rx", frameId = "", completeHexData = "";
		for (int i = 0; i < dataAsWords.length; i++) {
			// Ignore words which are empty.
			if (dataAsWords[i].trim().length() == 0) {
				continue;
			}
			idx++;
			switch (idx) {
				// First non empty word is messageNumber
				case 1: {
					messageNumber = Integer.parseInt(dataAsWords[i].replace(")", ""));
					break;
				}
				// Second non empty word is timeOffset
				case 2: {
					timeOffset = Float.parseFloat(dataAsWords[i]);
					break;
				}
				// third non empty word is message type (RX)
				case 3: {
					type = dataAsWords[i];
				}
				// First non empty word is frame ID
				case 4: {
					frameId = dataAsWords[i];
					break;
				}
				// Fifth non empty word is data length of hex data.
				case 5: {
					dataLength = Integer.parseInt(dataAsWords[i]);
					break;
				}
				// Non empty words from 6th count contribute towards the hex data.
				default: {
					completeHexData += hexDataSeparator + dataAsWords[i];
					hexDataSeparator = DEFAULT_HEX_DATA_SPLITTER;
					break;
				}
			}
		}

		// Get list of Sensor Reading Types mapped to frame ID.
		List<SensorReadingTypes> sensorReadingTypesForFrameId = CanFramesInfo.getSensorReadingsMappedToFrameId(frameId);
		if (sensorReadingTypesForFrameId == null) {
			return null;
		}
		// Build and return list of sensor readings decoded in line.
		return buildSensorReadings(sensorReadingTypesForFrameId, hexDataSeparator, messageNumber, timeOffset, type, frameId, dataLength, completeHexData);
	}

	// Builds Sensor Reading Objects with data from given frame and decoded data.
	private List<SensorReading> buildSensorReadings(List<SensorReadingTypes> sensorReadingTypesForFrameId,
			String hexValueSeparator, int messageNumber, float timeOffset, String type, String hexId, int dataLength,
			String completeHexData) {
		List<SensorReading> sensorReadings = new ArrayList<>();
		// For each sensor type build a Sensor Reading object.
		for (SensorReadingTypes sensorReadingType : sensorReadingTypesForFrameId) {
			// Decodes the given hex data for given sensorReadingType.
			float decodedData = decodeHexData(completeHexData, sensorReadingType);
			// Creates Can Sensor Data Object.
			SensorData sensorData = new CanSensorData();
			sensorData.setValue(decodedData);
			SensorReading sensorReading = new SensorReading(messageNumber, timeOffset, type, hexId, dataLength,
					completeHexData, sensorData,
					CanFramesInfo.getCanFrameInfoForSensorReadingType(sensorReadingType).getUnit(), sensorReadingType);
			sensorReadings.add(sensorReading);
		}
		return sensorReadings;
	}

	/**
	 * Returns a  binary representation of given hex.
	 * Each hex value is padded with 0s to make it of length 4 
	 * @param hex
	 * @return binary string representation of hex
	 */
	private String convertHexToBinary(String hex) {
		String finalBinary = "";
		for (int i = 0; i < hex.length(); i++) {
			// hex value represented as an int.
			int hexAsInt = Integer.parseInt(Character.toString(hex.charAt(i)), 16);
			// converts int representation of hex to binary string.
			String binaryString = Integer.toBinaryString(hexAsInt).toString();
			// Pad with 0 to make binary of size 4
			while (binaryString.length() < 4) {
				binaryString = "0" + binaryString;
			}
			finalBinary += binaryString;
		}
		return finalBinary;
	}

	/**
	 * Decodes the given hexData for sensorReadingType.
	 * Function uses CanFramesInfo to fetch step size and startoffset to decode the data.
	 * hex -> binary -> decimal.
	 * Final decoded data = (decimal result * stepSize) + startOffsetValye
	 * 
	 * @param hexData
	 * @param sensorReadingType
	 * @return Decoded data.
	 */
	private float decodeHexData(String hexData, SensorReadingTypes sensorReadingType) {
		CanFrameInfo canFrameInfo = CanFramesInfo.getCanFrameInfoForSensorReadingType(sensorReadingType);
		// Splits hexData by DEFAULT_HEX_DATA_SPLITTER.
		String[] hexDataAsBytes = hexData.split(DEFAULT_HEX_DATA_SPLITTER);
		String binary = "";
		for (int i = canFrameInfo.getStartByte(); i <= canFrameInfo.getEndByte(); i++) {
			String curr = "";
			String hex = hexDataAsBytes[i - 1];
			// If first byte, use startByteStartIndex info to check which bits in start byte to consider.
			if (i == canFrameInfo.getStartByte()) {
				curr = convertHexToBinary(hex).substring(7 - canFrameInfo.getStartByteStartIndex());
				binary = curr;
			} else if (i == canFrameInfo.getEndByte()) {
				// If end byte, use endByteEndIndex info to check which bits in end Byte byte to consider.
				curr = convertHexToBinary(hex).substring(0, 8 - canFrameInfo.getEndByteEndIndex());
				binary += curr;
			} else {
				// If not start byte or end byte consider all bits.
				curr = convertHexToBinary(hex);
				binary += curr;
			}
		}
		int decimalValue = Integer.parseInt(binary, 2);
		return (decimalValue * canFrameInfo.getStepSize()) + canFrameInfo.getStartOffset();
	}
	
	/**
	 * Parses GPS track file and CANmessage.trc file and returns an Object Representation of All sensor readings.
	 * @return VehicleSensorsData - Representation of all sensor readings from CANmesasge.trc and GPS data.
	 */
	public VehicleSensorsData parse() {
		VehicleSensorsData vechicleSensorsData = new VehicleSensorsData();
		parseCanDump(vechicleSensorsData);
		parseGpsData(vechicleSensorsData);
		vechicleSensorsData.sortSensorReadingByTimeOffset();
		return vechicleSensorsData;
	}

}
