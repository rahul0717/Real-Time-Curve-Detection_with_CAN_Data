/**
 * 
 * Class is a type of SensorData to store Gps Location Data. 
 *
 */
public class GpsSensorData extends SensorData {

	GpsLocationData value;
	
	GpsSensorData(){
		type = 1;
	}
	
	@Override
	Object getValue() {
		return value;
	}

	@Override
	void setValue(Object value) {
		this.value = (GpsLocationData)value;
	}

	public String toString() {
		return value.toString();
	}

}
