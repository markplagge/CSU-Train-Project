package trainProject;

import graphDataStructure.ITrack;

import java.net.InetAddress;
import java.util.Observable;

import trainProjectEnums.CommandType;
import trainProjectEnums.Polarity;

/**
 * The hardwareInterface class of the program will monitor the current state of
 * the train as well as control which portions of the track segments are on
 * while the train is in motion. This occurs through communication with the
 * hardware interfaces provided by the robotics system.
 *
 * @author Ian Blake-Knox
 * @author Mark Plagge
 * @author Richard Pike
 * @version 0.0
 */
public abstract class IHardwareInterface extends Observable {

  
    
    
   

    /**
     * sendCommand sends a command to the hardware interface.
     *
     * @param cmdType is defined as one of the commands accepted by the train
     * hardware interface.
     * @param valueOrPosition is the data passed to the hardware interface
     */
    public abstract void sendCommand(CommandType cmdType, int valueOrPosition);

    /**
     * sendCommand sends a command to the hardware interface. This is the two
     * parameter varient
     *
     * @param cmdType is defined as one of the commands accepted by the train
     * hardware interface.
     * @param trackPolarity Polarity of the block desired
     * @param valueOrPosition is the data passed to the hardware interface.
     */
    public abstract void sendCommand(CommandType cmdType, Polarity trackPolarity, int valueOrPosition);

    /**
     * queryhardware updates the state of this class to reflect the new current
     * state of the hardware. All of the track status is held inside class
     * variables. On queryHardware, the observers must be updated. Use the
     * following code to send update notifications to the observing objects:
     * <br>setChanged(); notifyObservers(currentStates)
     */
    public abstract void queryHardware();
}
