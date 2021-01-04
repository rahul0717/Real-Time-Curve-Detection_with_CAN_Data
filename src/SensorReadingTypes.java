/**
 * Types of sensor readings analyzed in this project.
 * Includes values from Can Frames Info.txt and Gps
 *
 */
public enum SensorReadingTypes {
	STEERING_WHEEL_ANGLE("Steering Wheel Angle"),
	VEHICLE_SPEED("Vehicle Speed"),
	YAW_RATE("Vehicle Yaw rate"),
	LONGITUDINAL_ACCELERATION("Vehicle Longitudinal acceleration"),
	LATERAL_ACCELERATION("Vechicle Lateral Acceleration"),
	GPS_LOCATION("Gps");

	private String value;
	 
	SensorReadingTypes(String value) {
        this.value = value;
    }
 
    public String getValue() {
        return value;
    }
}
