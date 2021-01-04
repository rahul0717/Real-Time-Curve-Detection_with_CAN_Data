/**
 * Class to store all information regarding a sensor reading (from CAN message file and GPS Track file).
 * 
 *
 */
public class SensorReading implements Comparable<SensorReading> {

	// Message Number in CAN message.
	private int messageNumber;
	// TimeOffset in ms from start time
	private float timeOffset;
	// Message Type - eg Rx
	private String type;
	// Frame id in hex of frame in CAN Message.
	private String hexId;
	// Length of Data.
	private int dataLength;
	// Can Frame Hex Data
	private String canFrameHexData;
	// Decoded Data from Can message / latitude and longitude data for GPS
	private SensorData sensorData;
	// Unit for sensor Data
	private String sensorDataUnit;
	private SensorReadingTypes sensorReadingType;


	public SensorReading(int messageNumber, float timeOffset, String type, String hexId, int dataLength,
			String canFrameHexData, SensorData sensorData, String sensorDataUnit,
			SensorReadingTypes sensorReadingType) {
		this.messageNumber = messageNumber;
		this.timeOffset = timeOffset;
		this.type = type;
		this.hexId = hexId;
		this.dataLength = dataLength;
		this.canFrameHexData = canFrameHexData;
		this.sensorData = sensorData;
		this.sensorDataUnit = sensorDataUnit;
		this.sensorReadingType = sensorReadingType;
	}
	
	// Returns a string representation for the Sensor Reading.
	@Override
	public String toString() {
		
		String result =  "[ Sensor Reading Type: " + sensorReadingType.getValue();
		if(hexId.length() > 0) {
			result += ", Can Frame ID: " + hexId;
		}
		result +=  ", Time Offset: " + timeOffset + ", Sensor Data: " + sensorData + ", Unit: "+ sensorDataUnit +"]";
		return result;
	}

	
	public int getMessageNumber() {
		return messageNumber;
	}

	public float getTimeOffset() {
		return timeOffset;
	}

	public String getHexId() {
		return hexId;
	}

	public int getDataLength() {
		return dataLength;
	}

	public String getType() {
		return type;
	}

	public String getCanFrameHexData() {
		return canFrameHexData;
	}

	@Override
	public int compareTo(SensorReading canFrame) {
		return Float.compare(this.timeOffset, canFrame.timeOffset);
	}

	public SensorReadingTypes getSensorReadingType() {
		return sensorReadingType;
	}

	public SensorData getSensorData() {
		return sensorData;
	}

	public String getSensorDataUnit() {
		return sensorDataUnit;
	}

}
