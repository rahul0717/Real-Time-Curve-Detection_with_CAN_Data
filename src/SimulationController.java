import java.util.Observer;

/**
 * A Singleton controller class to control interaction between Model(Simulator) and the UI 
 *
 */
public class SimulationController {

	private VehicleSensorsData sensorData;
	private static SimulationController instance;
	private Simulator simulator;
	private String canMessageFile;
	private String gpsTrackFile;
	
	/**
	 * Private constructor to make the class a singleton.
	 */
	private SimulationController(){
		
	}
	
	public void setInputFiles(String canMessageFile, String gpsTrackFile) {
		this.canMessageFile = canMessageFile;
		this.gpsTrackFile = gpsTrackFile;
	}

	/**
	 * Runs the simulator.
	 * If simulator object is not created, create a new object.
	 * Simulator is a class level variable to store the simulation results in the next iteration.
	 * Simulation is ran as a separate thread (Event Dispatcher Thread) which notifies the UI when a new DataFrame is created.
	 * @param source
	 */
	public void runSimulator(Observer source) {
		
		if(canMessageFile == null || gpsTrackFile == null) {
			return;
		}
		// If sensor data is not computed before, parse the files and get the sensor data
		if(sensorData == null) {
			VehicleSensorsDataParser parser = new VehicleSensorsDataParser(canMessageFile, gpsTrackFile);
			sensorData = parser.parse();
		}
		// Create simulator if not created before.
		// Set source(UI Panel) as observer of the simulator.
		// Simulator will notify the UI when a new dataframe is created.
		if(simulator == null) {
			simulator = new Simulator(sensorData);
			simulator.addObserver(source);
		}
		// Run simulator in a new thread.
		Thread t = new Thread(simulator);
		t.start();
	}
	
	
	/**
	 * Returns the singleton instance for the controller
	 * @return
	 */
	public static SimulationController getInstance() {
		if(instance == null) {
			instance = new SimulationController();
		}
		return instance;
	}
	
}
