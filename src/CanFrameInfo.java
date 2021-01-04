/**
 * 
 * This class stores information pertaining to different types of sensors in the
 * can dump.
 *
 */
public class CanFrameInfo {

	// represents frameID from CAN Frames Info.txt
	private String frameID;
	// represents the starting byte to be considered for processing
	private int startByte;
	// represents the ending byte to be considered for processing
	private int endByte;
	// represents the index in the starting byte to be considered for processing
	private int startByteStartIndex;
	// represents the index in the ending byte to be considered for processing
	private int endByteEndIndex;
	// max value of the data in hexadecimal
	private int bites;
	
	// start value of decoded data
	private float startOffset;
	// end value of decoded data
	private float endOffset;
	
	// Step size of the data to be multipled with the decoded data.
	private float stepSize;
	// Sensor Type
	private SensorReadingTypes sensorReadingType;
	
	// Unit of the data.
	private String unit;

	public CanFrameInfo(String frameID, String startInfo, String endInfo, int bites, float startOffset, float endOffset,
			float stepSize, SensorReadingTypes sensorReadingType, String unit) {
		this.frameID = frameID;
		this.bites = bites;
		this.stepSize = stepSize;
		this.sensorReadingType = sensorReadingType;
		this.setUnit(unit);
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		startByte = Integer.parseInt(startInfo.substring(startInfo.indexOf("By") + 2, startInfo.indexOf("Bi")));
		endByte = Integer.parseInt(endInfo.substring(endInfo.indexOf("By") + 2, endInfo.indexOf("Bi")));
		startByteStartIndex = Integer.parseInt(startInfo.substring(startInfo.indexOf("Bi") + 2));
		endByteEndIndex = Integer.parseInt(endInfo.substring(endInfo.indexOf("Bi") + 2));
	}

	
	// Setters and Getters
	public String getFrameID() {
		return frameID;
	}

	public void setFrameID(String frameID) {
		this.frameID = frameID;
	}

	public int getBites() {
		return bites;
	}

	public void setBites(int bites) {
		this.bites = bites;
	}

	public float getStepSize() {
		return stepSize;
	}

	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}

	public SensorReadingTypes getSensorReadingType() {
		return sensorReadingType;
	}

	public void setSensorReadingType(SensorReadingTypes sensorReadingType) {
		this.sensorReadingType = sensorReadingType;
	}

	public int getStartByte() {
		return startByte;
	}

	public void setStartByte(int startByte) {
		this.startByte = startByte;
	}

	public int getEndByte() {
		return endByte;
	}

	public void setEndByte(int endByte) {
		this.endByte = endByte;
	}

	public int getStartByteStartIndex() {
		return startByteStartIndex;
	}

	public void setStartByteStartIndex(int startByteStartIndex) {
		this.startByteStartIndex = startByteStartIndex;
	}

	public int getEndByteEndIndex() {
		return endByteEndIndex;
	}

	public void setEndByteEndIndex(int endByteEndIndex) {
		this.endByteEndIndex = endByteEndIndex;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(float startOffset) {
		this.startOffset = startOffset;
	}

	public float getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(float endOffset) {
		this.endOffset = endOffset;
	}
}
