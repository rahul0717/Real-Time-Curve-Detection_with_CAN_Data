import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SensorReadingsPanel extends JPanel implements Observer {

	// variables are volatile, since they are accessed by multiple threads (To avoid caching values in thread)
	// UI Components
	volatile JTextField timeTextField;
	volatile JTextField vehicleSpeedTextField;
	volatile JTextField steerAngleTextField;
	volatile JTextField yawRateTextField;
	volatile JTextField longitudinalAccelerationTextField;
	volatile JTextField lateralAccelerationTextField;
	volatile JTextField gpsLatitudeTextField;
	volatile JTextField gpsLongitudeTextField;
	volatile JLabel curveMessageLabel;
	volatile JLabel curveWarningMessageLabel;
	volatile JButton startButton;

	SensorReadingsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel readingsPanel = getSensorReadingsPanel();
		JPanel buttonsPanel = getStartButtonPanel();
		add(readingsPanel);
		add(buttonsPanel);
		add(getCurveWarningPanel());
		add(getCurveDetectionPanel());
	}

	/**
	 * Creates A Panel with a start button.
	 * Start button starts the simulation
	 * 
	 * @return Buttons JPanel
	 */
	private JPanel getStartButtonPanel() {
		JPanel buttonsPanel = new JPanel();
		startButton = new JButton("Start");
		// Action listener which is triggered on button click to start the simulation
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeTextField.setText("");
				vehicleSpeedTextField.setText("");
				steerAngleTextField.setText("");
				yawRateTextField.setText("");
				longitudinalAccelerationTextField.setText("");
				lateralAccelerationTextField.setText("");
				gpsLatitudeTextField.setText("");
				gpsLongitudeTextField.setText("");

				curveMessageLabel.setText("");
				curveMessageLabel.setFont(new Font("Serif", Font.PLAIN, 20));

				curveWarningMessageLabel.setText("");
				curveWarningMessageLabel.setForeground(Color.RED);
				curveWarningMessageLabel.setFont(new Font("Serif", Font.PLAIN, 20));

				SimulationController controller = SimulationController.getInstance();
				startButton.setEnabled(false);
				controller.runSimulator(SensorReadingsPanel.this);
			}
		});
		buttonsPanel.add(startButton);
		return buttonsPanel;
	}

	/**
	 * Creates a panel which has all Sensor Readings displayed.
	 * 
	 * @return sensorReadingsPanel(JPanel)
	 */
	private JPanel getSensorReadingsPanel() {
		JPanel sensorReadingsPanel = new JPanel();
		sensorReadingsPanel.setLayout(new BoxLayout(sensorReadingsPanel, BoxLayout.X_AXIS));
		sensorReadingsPanel.setMinimumSize(new Dimension(Integer.MAX_VALUE, 100));
		
		// Initializing UI Labels for all sensor readings.
		JLabel timeLabel = new JLabel("Time (milliseconds)");
		JLabel vehicleSpeedLabel = new JLabel("Vehicle Speed");
		JLabel steerAngleLabel = new JLabel("Steer Angle");
		JLabel yawRateLabel = new JLabel("Yar Rate");
		JLabel longitudinalAccelerationLabel = new JLabel("LongitudinalAcceleration");
		JLabel lateralAccelerationLabel = new JLabel("Lateral Acceleration");
		JLabel gpsLatitudeLabel = new JLabel("GPS-Latitude");
		JLabel gpsLongitudeLabel = new JLabel("GPS-Longitude");

		// Setting default values.
		timeTextField = new JTextField("", 8);
		vehicleSpeedTextField = new JTextField("", 8);
		steerAngleTextField = new JTextField("", 8);
		yawRateTextField = new JTextField("", 8);
		longitudinalAccelerationTextField = new JTextField("", 8);
		lateralAccelerationTextField = new JTextField("", 8);
		gpsLatitudeTextField = new JTextField("", 8);
		gpsLongitudeTextField = new JTextField("", 8);

		addEntry(sensorReadingsPanel, timeLabel, timeTextField);
		addEntry(sensorReadingsPanel, vehicleSpeedLabel, vehicleSpeedTextField);
		addEntry(sensorReadingsPanel, steerAngleLabel, steerAngleTextField);
		addEntry(sensorReadingsPanel, yawRateLabel, yawRateTextField);
		addEntry(sensorReadingsPanel, longitudinalAccelerationLabel, longitudinalAccelerationTextField);
		addEntry(sensorReadingsPanel, lateralAccelerationLabel, lateralAccelerationTextField);
		addEntry(sensorReadingsPanel, gpsLatitudeLabel, gpsLatitudeTextField);
		addEntry(sensorReadingsPanel, gpsLongitudeLabel, gpsLongitudeTextField);

		return sensorReadingsPanel;
	}

	/**
	 * Groups the given label and textfield inside a JPanel and adds to the given panel.
	 * @param panel
	 * @param label
	 * @param field
	 */
	private void addEntry(JPanel panel, JLabel label, JTextField field) {
		JPanel group = new JPanel();
		group.add(label);
		group.add(field);
		panel.add(group);
		// Adding border to the group panel
		group.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));

	}

	/**
	 * Creates a panel which displays the message when a curve is detected
	 * @return
	 */
	private JPanel getCurveDetectionPanel() {
		JPanel curveDetectionPanel = new JPanel();
		curveMessageLabel = new JLabel("");
		curveDetectionPanel.add(curveMessageLabel);
		return curveDetectionPanel;
	}

	/**
	 * Creates a panel to display warnings about detected curves
	 * @return
	 */
	private JPanel getCurveWarningPanel() {
		JPanel curveWarningPanel = new JPanel();
		curveWarningMessageLabel = new JLabel("");
		curveWarningPanel.add(curveWarningMessageLabel);
		return curveWarningPanel;
	}

	// Setters and getters to update Sensor Reading in UI.
	public void setTimeText(String time) {
		timeTextField.setText(time);
	}

	public void setVehicleSpeed(String vehicleSpeed) {
		vehicleSpeedTextField.setText(vehicleSpeed);
	}

	public void setSteerAngle(String steerAngle) {
		steerAngleTextField.setText(steerAngle);
	}

	public void setYawRate(String yawRate) {
		yawRateTextField.setText(yawRate);
	}

	public void setLongitudinalAcceleration(String longAcceleration) {
		longitudinalAccelerationTextField.setText(longAcceleration);
	}

	public void setLateralAcceleration(String latAcceleration) {
		lateralAccelerationTextField.setText(latAcceleration);
	}

	public void setGpsLatitude(String latitude) {
		gpsLatitudeTextField.setText(latitude);
	}

	public void setGpsLongitude(String longitude) {
		gpsLongitudeTextField.setText(longitude);
	}

	public void setCurveWarningMessageLabel(String curveWarningMessage) {
		curveWarningMessageLabel.setText(curveWarningMessage);
		curveWarningMessageLabel.revalidate();
	}

	public void enableStartButton() {
		startButton.setEnabled(true);
	}

	public void setCurveMessageLabel(String message) {
		curveMessageLabel.setText(message);
		curveMessageLabel.revalidate();
	}
	
	
	/**
	 * Method invoked by Observable when a data is changed.
	 * update reads data from the data frame and updates values in the UI.
	 */
	@Override
	public void update(Observable o, Object arg) {
		SimulationDataFrame df = (SimulationDataFrame) arg;
		// Swing components are not thread save.
		// Trying to modify UI text directly leads to dead locks.
		// Modifying UI data from Event dispatcher threads need to be done using SwingUtilities.invokeLater
		SwingUtilities.invokeLater(new UpdateUI(df));

	}

	// A Task to be update the UI using SimulationDataFrame df
	class UpdateUI implements Runnable {
		SimulationDataFrame df;

		UpdateUI(SimulationDataFrame df) {
			this.df = df;
		}

		@Override
		public void run() {

			// Get values from data frame, assign "-" as default value if value is null or -1 (For float) 
			String time = df.getTime() == -1 ? "-" : Float.toString(df.getTime());
			String vehicleSpeed = df.getVehicleSpeed() == -1 ? "-" : Float.toString(df.getVehicleSpeed());
			String steerAngle = df.getSteerAngle() == -1 ? "-" : Float.toString(df.getSteerAngle());
			String yawRate = df.getYawRate() == -1 ? "-" : Float.toString(df.getYawRate());
			String longAcceleration = df.getLongitudinalAcceleration() == -1 ? "-"
					: Float.toString(df.getLongitudinalAcceleration());
			String latAcceleration = df.getLateralAcceleration() == -1 ? "-"
					: Float.toString(df.getLateralAcceleration());
			String latitude = df.getGpsLatitude() == -1 ? "-" : Float.toString(df.getGpsLatitude());
			String longitude = df.getGpsLongitude() == -1 ? "-" : Float.toString(df.getGpsLongitude());

			// Set values in UI Components
			setTimeText(time);
			setVehicleSpeed(vehicleSpeed);
			setSteerAngle(steerAngle);
			setYawRate(yawRate);
			setLongitudinalAcceleration(longAcceleration);
			setLateralAcceleration(latAcceleration);
			setGpsLatitude(latitude);
			setGpsLongitude(longitude);

			// Checks and adds to UI if a curve warning message is set by Simulator
			String curveWarningMessage = df.getCurveWarningMessage();
			if (curveWarningMessage != null && !curveWarningMessage.isEmpty()) {
				setCurveWarningMessageLabel(curveWarningMessage);
			} else {
				setCurveWarningMessageLabel("");
			}
			// Returns the curve object set in the data frame
			Curve curve = df.getCurve();
			// If curve object is not null, a curve has been detected by the simulator.
			if (curve != null) {
				String message = constructCurveDetectedMessage(curve);
				setCurveMessageLabel(message);
			} else {
				// If not curve is detected, set curve detection message in UI to empty
				setCurveMessageLabel("");
			}
			// If simulation has finished running, enable start button in UI to start second iteration.
			if (!df.isSimulationRunning()) {
				enableStartButton();
			}
		}
		
		/**
		 * Constructs a message to be displayed in the UI for the given curve.
		 * @param curve
		 * @return Curve Detection Message
		 */
		private String constructCurveDetectedMessage(Curve curve) {
			GpsLocationData endLocation = curve.getEndPositionGpsData();
			String message = "Curve Detected: " + curve.getSpeedType() + " Speed " + curve.getDirection() + " Curve.";
			message += "\nAverage Speed: " + curve.getAverageVehicleSpeed() + " Start Position: GPS("
					+ curve.getStartPositionGpsData().getLatitude() + ", "
					+ curve.getStartPositionGpsData().getLongitude() + ")";
			
			// If curve is not finished endLocation will be null;
			if (endLocation != null) {
				message += " End Position: GPS(" + curve.getEndPositionGpsData().getLatitude() + ", "
						+ curve.getEndPositionGpsData().getLongitude() + ")";
			} else {
				message += " End Position: Computing..";
			}
			return message;
		}

	}
}
