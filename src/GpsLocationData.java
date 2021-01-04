/**
 * 
 * Class stores Gps Location Data (Latitude and Longitude)
 *
 */
public class GpsLocationData {

	private float latitude;
	private float longitude;
	
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	GpsLocationData(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String toString() {
		return " latitude: " + latitude + ", longitude: " + longitude + " ";
	}
}
