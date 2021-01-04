import java.util.Collections;
import java.util.List;

/**
 * Stores all information regarding the sensor readings for a given vehicle.
 *
 */
public class VehicleSensorsData {

	// Start Time representing from when sensor readings' started recording.
	// Base for timeoffset for all sensor readings.
	private String startTime;
	private List<SensorReading> sensorReadings;

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public List<SensorReading> getSensorReadings() {
		return sensorReadings;
	}

	public void setSensorReadings(List<SensorReading> sensorReadings) {
		this.sensorReadings = sensorReadings;
	}

	// sorts all sensor readings by Time Offset
	public void sortSensorReadingByTimeOffset() {
		if(sensorReadings != null) {
			Collections.sort(sensorReadings);
		}
	}
	
	@Override
	public String toString() {
		
		sortSensorReadingByTimeOffset();
		String result = "Start Time: " + startTime + "\n";
		result += "------------------------------------------------------------------------------------------------------------\n";
		result += "------------------------------------------------------------------------------------------------------------\n";
		result += "  Time Offset |         Sensor Reading Type             |    Value   ( Unit )     \n";
		result += "------------------------------------------------------------------------------------------------------------\n";
		result += "------------------------------------------------------------------------------------------------------------\n";
		
		for(SensorReading reading : sensorReadings) {
			result += "    " + reading.getTimeOffset() + "   |  " + reading.getSensorReadingType().getValue() + "  |    " + reading.getSensorData() + "  ( " + reading.getSensorDataUnit() +" )\n" ;
		}
		result += "\nStart Time: " + startTime + "\n";
		return result;
	}
	
}
