import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class SimulatorFrame extends JFrame {
	
	/**
	 * Main Frame class which initiates UI Components(Panels)
	 */
	SimulatorFrame(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		SensorReadingsPanel sensorReadingPanel = new SensorReadingsPanel();
		add(sensorReadingPanel);
		pack();
		setVisible(true);
	}

}
