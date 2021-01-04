/**
 * 
 * Class to store information about a curve
 *
 */

public class Curve {

    private float averageVehicleSpeed;
    private float startTime;
    private float endTime;
    private String direction;
    private String speedType;
    private GpsLocationData startPositionGpsData;
    private GpsLocationData endPositionGpsData;

    // Getters and Setters
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setAverageVehicleSpeed(float vehicleSpeed) {
        this.averageVehicleSpeed = vehicleSpeed;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public float getAverageVehicleSpeed() {
        return averageVehicleSpeed;
    }

    public void setSpeedType(String speedType) {
    	this.speedType = speedType;
    }
    
	public String getSpeedType() {
		return speedType;
	}

	public GpsLocationData getStartPositionGpsData() {
		return startPositionGpsData;
	}

	public void setStartPositionGpsData(GpsLocationData startPositionGpsData) {
		this.startPositionGpsData = startPositionGpsData;
	}

	public GpsLocationData getEndPositionGpsData() {
		return endPositionGpsData;
	}

	public void setEndPositionGpsData(GpsLocationData endPositionGpsData) {
		this.endPositionGpsData = endPositionGpsData;
	}

	// To String to print message about the curve
	@Override
	public String toString() {
		return "Curve [AverageVehicleSpeed=" + getDefaultValues(averageVehicleSpeed) + ", startTime=" + getDefaultValues(startTime) + ", endTime=" + getDefaultValues(endTime)
				+ ", direction=" + getDefaultValues(direction) + ", speedType=" + getDefaultValues(speedType) + ", startPositionGpsData="
				+ startPositionGpsData + ", endPositionGpsData=" + endPositionGpsData + "]";
	}
	
	/**
	 * If val is null returns "-" else returns val
	 * @param val
	 * @return "-" if empty or val
	 */
	private String getDefaultValues(String val) {
		if(val == null || val.isEmpty()) {
			return "-";
		}else {
			return val;
		}
	}
	
	/**
	 * If val is null returns "-" else returns val
	 * @param val
	 * @return "-" if empty or val
	 */
	
	private String getDefaultValues(Float val) {
		if(val == null || val == 0) {
			return "-";
		}else {
			return Float.toString(val);
		}
	}
	
}

