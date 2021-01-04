public class Main {
	
	/**
	 * Main function parses Gps data and Can Message Dump and prints the data parsed.
	 * Requires 2 arguments, 1 - CanMessage dump fileName(CANmessages.trc), 2- Gps Track html fileName(GPX Track.htm)
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Enter 2 input files CAN Message file and GPS Track HTM file");
		}
		SimulationController.getInstance().setInputFiles(args[0], args[1]);
		SimulatorFrame frame = new SimulatorFrame();
	}
}
