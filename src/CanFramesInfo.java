import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores all Can Frames Information.
 * Information for each FrameId.
 * FrameId to Sensor mapping.
 * Sensor To FrameInfo mapping.
 *
 */
public class CanFramesInfo {
	
	// Map to store sensorType to CanFrameInfo mapping
	private static Map<SensorReadingTypes, CanFrameInfo> sensorTypeToCanFrame;
	// Map to store frame Id to Sensor Type mapping.
	// Each Frame Id can be mapped to multiple sensor readings.
	private static Map<String, List<SensorReadingTypes>> frameIdToSensorReadingTypesMap;
	
	// Creating all CanFrameInfo objects.
	// Values are hard coded based on Can Frames Info.txt
	static {
		sensorTypeToCanFrame = new HashMap<>();
		
		sensorTypeToCanFrame.put(SensorReadingTypes.STEERING_WHEEL_ANGLE, new CanFrameInfo("0003", "By1Bi5", "By2Bi0", 14, -2048f, 2047f, 0.5f, SensorReadingTypes.STEERING_WHEEL_ANGLE, "degree"));
		
		sensorTypeToCanFrame.put(SensorReadingTypes.VEHICLE_SPEED, new CanFrameInfo("019F", "By1Bi3", "By2Bi0", 12, 0f, 409.4f, 0.1f, SensorReadingTypes.VEHICLE_SPEED, "km/h"));
		
		sensorTypeToCanFrame.put(SensorReadingTypes.YAW_RATE, new CanFrameInfo("0245", "By1Bi7", "By2Bi0", 16, -327.68f, 327.66f, 0.01f, SensorReadingTypes.YAW_RATE, "degree/s"));
		
		sensorTypeToCanFrame.put(SensorReadingTypes.LONGITUDINAL_ACCELERATION, new CanFrameInfo("0245", "By5Bi7", "By5Bi0", 8, -10.24f, 20.08f, 0.08f, SensorReadingTypes.LONGITUDINAL_ACCELERATION, "m/s^2"));
		
		sensorTypeToCanFrame.put(SensorReadingTypes.LATERAL_ACCELERATION, new CanFrameInfo("0245", "By6Bi7", "By6Bi0", 8, -10.24f, 10.08f, 0.08f, SensorReadingTypes.LATERAL_ACCELERATION, "m/s^2"));
		
		// Creates frame Id to Sensort Types mapping based on above objects created.
		frameIdToSensorReadingTypesMap = new HashMap<>();
		for(SensorReadingTypes sensorReading : sensorTypeToCanFrame.keySet()) {
			String frameId = sensorTypeToCanFrame.get(sensorReading).getFrameID();
			List<SensorReadingTypes> readingsForFrameId = frameIdToSensorReadingTypesMap.getOrDefault(frameId, new ArrayList<SensorReadingTypes>());
			readingsForFrameId.add(sensorReading);
			frameIdToSensorReadingTypesMap.put(frameId, readingsForFrameId);
		}
	}
	
	/**
	 * Returns the CanFrameInfo for given SensorReadingType ( Yaw Rate, Speed, Steering Wheel Angle..)
	 * @param sensorReadingType
	 * @return CanFrameInfo
	 */
	public static CanFrameInfo getCanFrameInfoForSensorReadingType(SensorReadingTypes sensorReadingType) {
		return sensorTypeToCanFrame.get(sensorReadingType);
	}
	
	
	/**
	 * Returns the list of SensorReadingTypes mapped for given frame Id
	 * frameId is not case sensitive.
	 * @param frameId
	 * @return List of sensorReadingTypes mapped to frameId if exists else returns null.
	 */
	public static List<SensorReadingTypes> getSensorReadingsMappedToFrameId(String frameId) {
		if(frameIdToSensorReadingTypesMap.containsKey(frameId)) {
			return frameIdToSensorReadingTypesMap.get(frameId);
		} else if(frameIdToSensorReadingTypesMap.containsKey(frameId.toLowerCase())) {
			return frameIdToSensorReadingTypesMap.get(frameId.toLowerCase());
		}
		return null;
	}
	
}
