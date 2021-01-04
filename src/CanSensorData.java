/**
 * Class to store Sensor data from CAN Frames.
 *
 */

public class CanSensorData extends SensorData {

	float value;

	CanSensorData(){
		type = 0;
	}

	@Override
	Object getValue() {
		return value;
	}

	@Override
	void setValue(Object value) {
		this.value = (float) value;
	}
	
	public String toString() {
		return "" + value;
	}
}
