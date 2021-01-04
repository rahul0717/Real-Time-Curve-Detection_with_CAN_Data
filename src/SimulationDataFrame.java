/**
 * A Class to store every single sensor value with the correct timing.
 *
 */
public class SimulationDataFrame {

	private float time;
	private float vehicleSpeed;
	private float steerAngle;
	private float yawRate;
	private float longitudinalAcceleration;
	private float lateralAcceleration;
	private float gpsLatitude;
	private float gpsLongitude;
	private Curve curve;
	private String curveWarningMessage;
	// Flag to check if simulation is over.
	private boolean simulationRunning = true;

	// Setter and Getters
	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getVehicleSpeed() {
		return vehicleSpeed;
	}

	public void setVehicleSpeed(float vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}

	public float getSteerAngle() {
		return steerAngle;
	}

	public void setSteerAngle(float steerAngle) {
		this.steerAngle = steerAngle;
	}

	public float getYawRate() {
		return yawRate;
	}

	public void setYawRate(float yawRate) {
		this.yawRate = yawRate;
	}

	public float getLongitudinalAcceleration() {
		return longitudinalAcceleration;
	}

	public void setLongitudinalAcceleration(float longitudinalAcceleration) {
		this.longitudinalAcceleration = longitudinalAcceleration;
	}

	public float getLateralAcceleration() {
		return lateralAcceleration;
	}

	public void setLateralAcceleration(float lateralAcceleration) {
		this.lateralAcceleration = lateralAcceleration;
	}

	public float getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(float gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public float getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(float gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Curve getCurve() {
		return curve;
	}

	public void setCurve(Curve curve) {
		this.curve = curve;
	}

	public String getCurveWarningMessage() {
		return curveWarningMessage;
	}

	public void setCurveWarningMessage(String curveWarningMessage) {
		this.curveWarningMessage = curveWarningMessage;
	}

	public boolean isSimulationRunning() {
		return simulationRunning;
	}

	public void setSimulationRunning(boolean simulationRunning) {
		this.simulationRunning = simulationRunning;
	}
	
	
	// toString to print the value of the data frame.
	@Override
	public String toString() {
		String curveData = "";
		if(curve != null) {
			curveData = ", Curve Detected: " + curve.toString();
		}
		return "SimulationDataFrame [time=" + time + ", vehicleSpeed=" + vehicleSpeed + ", steerAngle=" + steerAngle
				+ ", yawRate=" + yawRate + ", longitudinalAcceleration=" + longitudinalAcceleration
				+ ", lateralAcceleration=" + lateralAcceleration + ", gpsLatitude=" + gpsLatitude + ", gpsLongitude="
				+ gpsLongitude + curveData + "]";
	}

}
