package trainProject;

import graphDataStructure.ITrack;


/**
 * IHandControllerInterpreter: This class will receive and interpret the hand controller's state. 
 * This class works through an internal loop that runs in a thread or timer. The methods inside
 * this object poll the hardware interface, and then talk to the hand controller object
 * for further communication.
 * 
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 */
public interface IHandControllerInterpreter {
    
      
    /**
     * onStartControl: method to run once the thread starts executing. This is the
     * method will be called by the runnable method. This method initiates the polling
     * of the hand controller hardware interface over and over again.
     * @param theTrack Reference to the track object for this run of the program
     * @param theController is a  pointer to the hand controller hardware management class.
     */
    public void onStartControl(ITrack theTrack, IHandControllerReceiver theController);
    
    /**
     * pollHandController polls the hardware and updates this class' information.
     */
    public void pollHandController();
    
    /**
     * onEndControl ends the control of the hand controller gracefully.
     */
    public void onEndControl();
    
    /**
     * This method updates the train and track based on the hand controller configuration.
     */
    public void updateTrain();
    
    /**
     * Registers a specific train for control. This train is stored 
     * @param theTrain
     */
    public void controlTrain(ITrain theTrain);
           
    
}
