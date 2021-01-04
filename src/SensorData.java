/**
 * Wrapper class to store value of sensor data part of CAN message and GPS data(latitude and longitude)
 *
 */
abstract class SensorData {
	
	int type;
	abstract Object getValue();
	abstract void setValue(Object value);

}
