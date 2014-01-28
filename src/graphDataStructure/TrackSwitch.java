package graphDataStructure;

/**
 * The TrackSwitch class creates a switch that is part of a representation of a track.
 * The switch knows about what is adjacent to it, such as sensors in either 
 * a 180 or 90 state and the switches adjacent to it based on those states. The TrackSwich
 * also knows its ID number and its current state. The TrackSwitch uses int's to identify
 * the switches that are its neighbors based on direction.
 * 
 * Accessors and mutators have been added to the class to set and to retrieve these parameters.
 * 
 * @author Mark Plagge, Ian Blake-Knox, Richard Pike
 */

public class TrackSwitch {
	/**
	 * Class variables
	 */
	private int switchID;
	private int switchState;
	private String sensor180;
	private String sensor90;
	private int cw180_AdjSwitchNum;
	private int cw90_AdjSwitchNum;
	private int ccw180_AdjSwitchNum;
	private int ccw90_AdjSwitchNum;
	
	/**
	 * default constructor
	 */
	public TrackSwitch(){
		//Does nothing
	}
	/**
	 * Constructor. This constructor takes parameters that are native to each switch.
	 * Examples of native parameters are the switches ID number, the sensors that are 
	 * adjacent to it in either 180 or 90 state, and its current state of 180 or 90.
	 * 
	 * @param id
	 * @param switchState
	 * @param AdjSens180
	 * @param adjSens90
	 */
	public TrackSwitch(int id, int switchState, String AdjSens180, String adjSens90, int cw180_AdjSwitch,
			int cw90_AdjSwitch, int ccw180_AdjSwitch, int ccw90_AdjSwitch) {
		this.setSwitchID(id);
		this.setSwitchState(switchState);
		this.setSensor180(AdjSens180);
		this.setSensor90(adjSens90);
		this.setCW_180_AdjSwitchNum(cw180_AdjSwitch);
		this.setCW_90_AdjSwitchNum(cw90_AdjSwitch);
		this.setCCW_180_AdjSwitchNum(ccw180_AdjSwitch);
		this.setCCW_90_AdjSwitchNum(ccw90_AdjSwitch);
	}
	/**
	 * sets the identification number of the switch
	 * 
	 * @param verticeID
	 */
	public void setSwitchID(int id) {
		this.switchID = id;
	}
	/**
	 * 
	 * @return the ID of the current switch
	 */
	public int getSwitchID() {
		return switchID;
	}
	/**
	 * 
	 * @param initSwitchState
	 */
	public void setSwitchState(int switchState) {
		this.switchState = switchState;
	}
	/**
	 * 
	 * @return state of the current switch
	 */
	public int getSwitchState() {
		return switchState;
	}
	/**
	 * set the sensor that is adjacent to the switch if the state is set
	 * to 180
	 * 
	 * @param AdjSens180
	 */
	public void setSensor180(String AdjSens180) {
		this.sensor180 = AdjSens180;
	}
	/**
	 * 
	 * @return the sensor that is adjacent if the state is set to 180
	 */
	public String getSensor180() {
		return sensor180;
	}
	/**
	 * set the sensor that is adjacent to the switch if the state is set
	 * to 90
	 * 
	 * @param AdjSens90
	 */
	public void setSensor90(String AdjSens90) {
		this.sensor90 = AdjSens90;
	}
	/**
	 * 
	 * @return the sensor that is adjacent if the state is set to 90
	 */
	public String getSensor90() {
		return sensor90;
	}
	/**
	 * Uses an int to set the number of the switch that is adjacent to
	 * this switch if the direction is clockwise and the switch state is
	 * set to 180. Visibility is private because once the adjacent switch
	 * number is set it does not need to change.
	 * 
	 * @param cw180_AdjSwitch
	 */
	private void setCW_180_AdjSwitchNum(int cw180_AdjSwitch) {
		this.cw180_AdjSwitchNum = cw180_AdjSwitch;
	}
	/**
	 * Returns the number of the switch that is adjacent to this switch
	 * if the direction is clockwise and the switch is set to state 180.
	 * 
	 * @return cw180_AdjSwitchNum
	 */
	public int getCW_180_AdjSwitchNum() {
		return cw180_AdjSwitchNum;
	}
	/**
	 * Uses an int to set the number of the switch that is adjacent to
	 * this switch if the direction is clockwise and the switch state is
	 * set to 90. Visibility is private because once the adjacent switch
	 * number is set it does not need to change.
	 * 
	 * @param cw90_AdjSwitch
	 */
	private void setCW_90_AdjSwitchNum(int cw90_AdjSwitch) {
		this.cw90_AdjSwitchNum = cw90_AdjSwitch;
	}
	/**
	 * Returns the number of the switch that is adjacent to this switch
	 * if the direction is clockwise and the switch is set to state 90.
	 * 
	 * @return
	 */
	public int getCW_90_AdjSwitchNum() {
		return cw90_AdjSwitchNum;
	}
	/**
	 * Uses an int to set the number of the switch that is adjacent to
	 * this switch if the direction is counter-clockwise and the switch
	 * state is set to 180. Visibility is private because once the
	 * adjacent switch number is set it does not need to change.
	 * 
	 * @param ccw180_AdjSwitch
	 */
	private void setCCW_180_AdjSwitchNum(int ccw180_AdjSwitch) {
		this.ccw180_AdjSwitchNum = ccw180_AdjSwitch;
	}
	/**
	 * Returns the number of the switch that is adjacent to this switch
	 * if the direction is counter-clockwise and the switch is set to state 180.
	 * 
	 * @return
	 */
	public int getCCW_180_AdjSwitchNum() {
		return ccw180_AdjSwitchNum;
	}
	/**
	 * Uses an int to set the number of the switch that is adjacent to
	 * this switch if the direction is counter-clockwise and the switch
	 * state is set to 90. Visibility is private because once the
	 * adjacent switch number is set it does not need to change.
	 * 
	 * @param ccw90_AdjSwitch
	 */
	private void setCCW_90_AdjSwitchNum(int ccw90_AdjSwitch) {
		this.ccw90_AdjSwitchNum = ccw90_AdjSwitch;
	}
	/**
	 * Returns the number of the switch that is adjacent to this switch
	 * if the direction is counter-clockwise and the switch is set to state 90.
	 * 
	 * @return
	 */
	public int getCCW_90_AdjSwitchNum() {
		return ccw90_AdjSwitchNum;
	}
	/**
	 * Returns the ID of the current switch formated as a String in the 
	 * following format: "ID: ID#"
	 * 
	 * @return ID
	 */
	public String toString() {
		return "ID: " + getSwitchID();
	}
}