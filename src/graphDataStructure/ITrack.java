package graphDataStructure;

import java.util.Observable;

import trainProjectEnums.Polarity;
import trainProjectEnums.SwitchStatus;

/**
 * The track category of class will manage the state of the track, as well as the structure
 * of the track.It will respond to queries regarding possible routes, proper switches, and will accept sensor information.
 * 
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 *
 */
public abstract class ITrack extends Observable{
	
    /**
     * 
     */
    
	/**
	 * getSwitchState returns a value that informs you what the status of a switch is. 
	 * @param switchNumber What switch hath the hubris of mankind wrought upon us?
	 * @return switchStatus - An enumerator that represents the two possible switch states.
	 * 
	 */
	public abstract SwitchStatus getSwitchState(int switchNumber);
	
	/**
	 * setSwitchState changes the state of a switch. This MUST communicate with the hardware client. 
	 * @param switchNumber
	 * @param newSwitchState
         * @param hardwareClientRef a reference to the singleton hardware client so that we can inform the
         * @return  
	 */
	public abstract boolean setSwitchState(int switchNumber, SwitchStatus newSwitchState, Object hardwareClientRef);
	
        /**
         * Given a specific block, and a direction, what are the next sensors that we should be able to see?
         * @param block
         * @param direction
         * @return
         */
        public abstract int[] getNextSensors(int block, Polarity direction);

        /**
         * Given a block and a direction, this method returns the next block.
         * @param currentBlock
         * @param direction
         * @return
         */
        public abstract int getNextBlock(int currentBlock, Polarity direction);
        
	
}
