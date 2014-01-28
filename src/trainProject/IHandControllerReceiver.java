package trainProject;

import java.util.Observer;

import trainProjectEnums.Polarity;
import trainProjectEnums.SwitchStatus;
/**
 * IHandControllerReceiver: This class talks directly to the hardware of the train interface.
 * The class will poll the hardware, and then will update the receiving object. 
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 */
public interface IHandControllerReceiver extends Observer{
   
    /**
     * switchStatus: this method returns the switch status from the hardware controller. 
     * It uses the standard enum to represent the switch position. This is only for the 
     * switch controller.
     * @return switchStatus
     */
    public SwitchStatus getSwitchState();
    
    /**
     * getPolarity asks the hand controller for the current requested (hand controller) polarity status.
     * @return a polarity enum based on the requested polarity from the switch.
     */
    public Polarity getPolarity();
    
    /**
     * getPowerState asks the hand controller for the current requested (hand controller) power state.
     * @return a boolean representing the requested power state.
     */
    public boolean getPowerState();
}
