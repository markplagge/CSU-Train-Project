package trainProject;

import trainProjectEnums.BlockID;
import trainProjectEnums.SensorID;
import trainProjectEnums.SwitchID;

/**
 * The ITrain class encapsulates the ITrainInterpreter and ITrainReceiver classes into one easy to use package.
 * This allows the operators of the train to create and manage only one class, while maintaining the
 * granularity of having three control classes per train.
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 *
 */
public interface ITrain extends ITrainInterpreter{

	/**
	 * GetTrainBlock returns the current block that the train is on.
	 * @return BlockID - a enum representing the block of train.
	 */
	public BlockID getTrainBlock();
	/**
	 * getNextTrainSensor returns the next sensors that the train will encounter
	 * @return SensorID - A sensor array with the next sensor(s) that the train will hit.
	 */
	public SensorID[] getNextTrainSensor();
	/**
	 * isControledByHand returns a boolean value that tells you if the train is controled by hand.
	 * @return
	 */
	public boolean isControledByHand();
	
	/**
	 * Set the train to listen to hand control inputs.
	 * @param handControl
	 */
	public void setControlByHand(boolean handControl);
	

	/**
	 * @return the next switch that the train will hit.
	 */
	public SwitchID getNextSwitch();
	
	
}
