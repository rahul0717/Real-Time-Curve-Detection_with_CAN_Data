import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class runs the simulation using the VehicleSensors Data
 *
 */
public class Simulator extends Observable implements Runnable {

	
	private static final int HIGH_SPEED_THRESHOLD = 50;
	private static final int CURVE_THRESHOLD = 12;
	private static final String HIGH_SPEED = "High";
	private static final String LOW_SPEED = "Low";
	
	private static final String LEFT_CURVE = "Left";
	private static final String RIGHT_CURVE = "Right";

	
	private VehicleSensorsData data;
	// List to store all detected curves
	private List<Curve> curveList;
	//Flag to check if this is first simulation
	private boolean firstSimulation;
    
    
    
    Simulator(VehicleSensorsData data) {
        this.data = data;
        curveList = new ArrayList<>();
        firstSimulation = true;
    }
    

    public void run() {
        boolean leftCurveStarted = false;
        boolean rightCurveStarted = false;
        List<SensorReading> readings = data.getSensorReadings();
        float vehicleSpeed = -1;
        float totalVehicleSpeedCurve = 0;
        int velcoityNumber = 0;
        float averageVehicleSpeedCurve;
        float steerAngle = -1;
        float yawRate = -1;
        float longitudinalAcceleration = -1;
        float lateralAcceleration = -1;
        float gpsLatitude = -1;
        float gpsLongitude = -1;

        float prevTimeOffset = -1;
        Curve curveObj = null;
        SimulationDataFrame df = new SimulationDataFrame();
        long simulationStartTime = System.currentTimeMillis();
        for (SensorReading reading : readings) {
        	
        	// Combine sensor readings in single dataframe for sensor readings with the same time offset.
            if (prevTimeOffset == -1 || reading.getTimeOffset() == prevTimeOffset) {
                prevTimeOffset = reading.getTimeOffset();
                if (reading.getSensorReadingType() == SensorReadingTypes.STEERING_WHEEL_ANGLE) {
                    steerAngle = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.VEHICLE_SPEED) {
                    vehicleSpeed = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.YAW_RATE) {
                    yawRate = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.LONGITUDINAL_ACCELERATION) {
                    longitudinalAcceleration = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.LATERAL_ACCELERATION) {
                    lateralAcceleration = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.GPS_LOCATION) {
                    GpsLocationData gpsData = (GpsLocationData) reading.getSensorData().getValue();
                    gpsLatitude = gpsData.getLatitude();
                    gpsLongitude = gpsData.getLongitude();
                }

            } else {
            	// When current iteration time is different from previous stored time, add previous sensor values
            	// To a single data frame.
                setDfValues(df, vehicleSpeed, steerAngle, yawRate, longitudinalAcceleration, 
                		lateralAcceleration, gpsLatitude, gpsLongitude, prevTimeOffset);

                // update sensor values with values from current iteration
                prevTimeOffset = reading.getTimeOffset();
                if (reading.getSensorReadingType() == SensorReadingTypes.STEERING_WHEEL_ANGLE) {
                    steerAngle = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.VEHICLE_SPEED) {
                    vehicleSpeed = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.YAW_RATE) {
                    yawRate = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.LONGITUDINAL_ACCELERATION) {
                    longitudinalAcceleration = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.LATERAL_ACCELERATION) {
                    lateralAcceleration = (float) reading.getSensorData().getValue();
                } else if (reading.getSensorReadingType() == SensorReadingTypes.GPS_LOCATION) {
                    GpsLocationData gpsData = (GpsLocationData) reading.getSensorData().getValue();
                    gpsLatitude = gpsData.getLatitude();
                    gpsLongitude = gpsData.getLongitude();
                }

                // Curve Detection
                if(leftCurveStarted || rightCurveStarted) {
                	totalVehicleSpeedCurve += vehicleSpeed;
                	velcoityNumber += 1;
                	averageVehicleSpeedCurve = (float) Math.floor(totalVehicleSpeedCurve / velcoityNumber);
                    curveObj.setAverageVehicleSpeed(averageVehicleSpeedCurve);
                }
                
                // If left curve is not started and if steer angle is below threshold, left curve detected.
                if (!leftCurveStarted && steerAngle < -CURVE_THRESHOLD) {
                	leftCurveStarted = true;
                	totalVehicleSpeedCurve += vehicleSpeed;
                    velcoityNumber += 1;
                    averageVehicleSpeedCurve = (float) Math.floor(totalVehicleSpeedCurve / velcoityNumber);
                    // Creates a curveObj with required information
                	curveObj = new Curve();
                	createCurveObject(curveObj, LEFT_CURVE, totalVehicleSpeedCurve, averageVehicleSpeedCurve, prevTimeOffset, gpsLatitude, gpsLongitude);
                }
                // If Vehicle was in a left curve and if steer angle is back below threshold, earlier curve has ended.
                if (leftCurveStarted && steerAngle > -CURVE_THRESHOLD) {
                    averageVehicleSpeedCurve = (float) Math.floor(totalVehicleSpeedCurve / velcoityNumber);
                    setEndDataForCurveObject(curveObj, averageVehicleSpeedCurve, prevTimeOffset, gpsLatitude, gpsLongitude);
                    leftCurveStarted = false;
                    totalVehicleSpeedCurve = 0;
                    velcoityNumber = 0;
                }
                if (!rightCurveStarted && steerAngle > CURVE_THRESHOLD) {
                    rightCurveStarted = true;
                    totalVehicleSpeedCurve += vehicleSpeed;
                    velcoityNumber += 1;
                    averageVehicleSpeedCurve = (float) Math.floor(totalVehicleSpeedCurve / velcoityNumber);
                	curveObj = new Curve();
                    createCurveObject(curveObj, RIGHT_CURVE, totalVehicleSpeedCurve, averageVehicleSpeedCurve, prevTimeOffset, gpsLatitude, gpsLongitude);                    
                    
                }
                if (rightCurveStarted && steerAngle < CURVE_THRESHOLD) {
                    averageVehicleSpeedCurve = (float) Math.floor(totalVehicleSpeedCurve / velcoityNumber);
                    setEndDataForCurveObject(curveObj, averageVehicleSpeedCurve, prevTimeOffset, gpsLatitude, gpsLongitude);
                    rightCurveStarted = false;
                    totalVehicleSpeedCurve = 0;
                    velcoityNumber = 0;
                }
                // Sleeps for simulationTime - Real Time milliseconds.
                // This is to keep simulation time same as real time.
                sleepToSimulateRealTime(df, simulationStartTime);

                // If not first simulation, check if a curve exits before current data frame.
                if(!firstSimulation && curveList != null){
                	String warningMessage = getCurveWarningMessage(df);
                	df.setCurveWarningMessage(warningMessage);
                }
                df.setCurve(curveObj);
                update(df);
                if(!leftCurveStarted && !rightCurveStarted) {
                	curveObj = null;
                }
            }
        }
        // Since we check and update Df of previous iteration, update DF once after all iteration is done.
        setDfValues(df, vehicleSpeed, steerAngle, yawRate, longitudinalAcceleration, lateralAcceleration, gpsLatitude, gpsLongitude, prevTimeOffset);
        // Set simulation running to false;
        df.setSimulationRunning(false);
        
        // Notify dataframe in UI
        update(df);
        // Set first simulation is over. 
        firstSimulation = false;
    }
    
    /**
     * Sets sensor readings for dataframe
     * @param df
     * @param vehicleSpeed
     * @param steerAngle
     * @param yawRate
     * @param longitudinalAcceleration
     * @param lateralAcceleration
     * @param gpsLatitude
     * @param gpsLongitude
     * @param prevTimeOffset
     */
    private void setDfValues(SimulationDataFrame df, float vehicleSpeed, float steerAngle, float yawRate, float longitudinalAcceleration,
    		float lateralAcceleration, float gpsLatitude, float gpsLongitude, float prevTimeOffset) {
    	df.setVehicleSpeed(vehicleSpeed);
        df.setSteerAngle(steerAngle);
        df.setYawRate(yawRate);
        df.setLongitudinalAcceleration(longitudinalAcceleration);
        df.setLateralAcceleration(lateralAcceleration);
        df.setGpsLatitude(gpsLatitude);
        df.setGpsLongitude(gpsLongitude);
        df.setTime(prevTimeOffset);
        df.setSimulationRunning(false);
    }
    
    /**
     * Creates a new curveObj and sets values to it.
     * Curve is in high speed if vehicle speed is greater than 50
     * @param curveObj
     * @param direction
     * @param vehicleSpeed
     * @param averageVehicleSpeedCurve
     * @param prevTimeOffset
     * @param gpsLatitude
     * @param gpsLongitude
     */
    private void createCurveObject(Curve curveObj, String direction, float vehicleSpeed, float averageVehicleSpeedCurve, 
    		float prevTimeOffset, float gpsLatitude, float gpsLongitude) {
        if(vehicleSpeed > HIGH_SPEED_THRESHOLD) {
        	curveObj.setSpeedType(HIGH_SPEED);
    	} else {
    		curveObj.setSpeedType(LOW_SPEED);
    	}
        curveObj.setStartTime(prevTimeOffset);
        curveObj.setDirection(direction);
        curveObj.setAverageVehicleSpeed(averageVehicleSpeedCurve);
        GpsLocationData startPositionGpsData = new GpsLocationData(gpsLatitude, gpsLongitude);
        curveObj.setStartPositionGpsData(startPositionGpsData);
    }
    
    /**
     * Sets values to curveObj when curve detection ends.
     * @param curveObj
     * @param averageVehicleSpeedCurve
     * @param prevTimeOffset
     * @param gpsLatitude
     * @param gpsLongitude
     */
    private void setEndDataForCurveObject(Curve curveObj, float averageVehicleSpeedCurve, float prevTimeOffset, float gpsLatitude, float gpsLongitude) {
        curveObj.setAverageVehicleSpeed(averageVehicleSpeedCurve);
        curveObj.setEndTime(prevTimeOffset);
        GpsLocationData endPositionGpsData = new GpsLocationData(gpsLatitude, gpsLongitude);
        curveObj.setEndPositionGpsData(endPositionGpsData);
        if(firstSimulation) {
        	curveList.add(curveObj);
        }
    }
    
    /**
     * Sleep until simulation time matches real time of sensors.
     * @param df
     * @param simulationStartTime
     */
    private void sleepToSimulateRealTime(SimulationDataFrame df, long simulationStartTime) {
    	try {
        	long timeout = (long)(df.getTime() - (System.currentTimeMillis() - simulationStartTime));
        	if(timeout > 0) {
        		Thread.sleep(timeout);
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * Given two latitude, longitude coordinates, calculates their distance using Haversine formula
     * @param latitude1
     * @param longitude1
     * @param latitude2
     * @param longitude2
     * @return
     */
    private double getDistance(float latitude1, float longitude1, float latitude2, float longitude2) {
		if ((latitude1 == latitude2) && (longitude1 == longitude2)) {
			return 0;
		}
		else {
			double distance = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.cos(Math.toRadians(longitude1 - longitude2));
			distance = Math.acos(distance);
			distance = Math.toDegrees(distance);
			distance = distance * 60 * 1.1515;
			distance = distance * 1.609344;
			return distance;
		}
	}
    
    /**
     * Uses dataframe information to check if a curve is ahead within 80 meters.
     * @param df
     * @return Message if curve is ahead of current data frame.
     */
    private String getCurveWarningMessage(SimulationDataFrame df) {
    	String warningMessage ="";
        for(Curve c : curveList){
        	if(df.getTime() <= c.getStartTime()) {
        		double distance = getDistance(df.getGpsLatitude(), df.getGpsLongitude(), c.getStartPositionGpsData().getLatitude(), c.getStartPositionGpsData().getLongitude());
            	distance = distance * 1000;
                if( distance > 0 && distance <= 80 ){
                   warningMessage = "Warning: " + c.getDirection() + " Curve ahead in " + distance + " meters. Average Speed: " + c.getAverageVehicleSpeed();
                   break;
                } else {
                	warningMessage = "";
                	break;
                }
        	}
        }
        return warningMessage;
    }

    /**
     * Clears the console screen, This is to print all values in a single line.
     */
    public static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}
	private void update(SimulationDataFrame df ) {
		clearScreen();
		System.out.print("\r" + df);
		setChanged();
		notifyObservers(df);
	}

}
