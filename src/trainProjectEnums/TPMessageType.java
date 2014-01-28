/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProjectEnums;

/**
 *
 * @author csu
 */
public enum TPMessageType {
    SensorUpdate,
   
    /**
     * UpdateHardware command type is a command to update a switch to a particular setting.
     */
    UpdateHardware,
    /**
     * HC StateUpdate means that this message is an update from the hardware, intending to go to the hand controller object.
     */
    HCStateUpdate,
    
    /**
     * HCCommand means that this message is a command from the hand controller. 
     */
    HCCommand
}
