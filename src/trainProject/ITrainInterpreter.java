package trainProject;

import graphDataStructure.ITrack;
import trainProjectEnums.Polarity;

/**
 *The ITrainInterpreter class of the program will control the state of the train while 
 * the program is running.
 * The methods of the program will set the train in motion as well as stop the 
 * train from motion. It will monitor the current direction of the train and 
 * pass the direction information to the client class to allow it to process the
 * state of the track segments.
 * 
 * @author Mark Plagge
 */
public interface ITrainInterpreter {

	/**
	 * this method is for the start of the program. This is a method that sets up the listener
	 * and establishes the track and hardware interface commands.
	 * @param theTrack pointer to the main track object
	 * @param theController link to the hardware interface.
	 */
	//public void onStartControl(ITrack theTrack, TrainReceiver theController);
        /**
     * Stop sends commands to stop the current train at @trainNumber and @trackNumber
     * @param trainNumber train to stop (used to keep track of the trains)
     * @param trackNumber track number to request stoppage.
     */
    public void stop(int trainNumber, int trackNumber);
        
        /**
     * go starts the train out on the track:
     * @param trainNumber train to start (used to keep track of the trains)
     * @param trackNumber track number to request stoppage.
     */
    public void go(int trainNumber, int trackNumber);
        
        /**
     *getCurrentDirection returns the polarity of the current train:
     * @param trainNumber what train is it?
     * @return the Polarity representation of the polarity of the current train.
     */
    public Polarity getCurrentDirection(int trainNumber);
        
	/**
	 * @param newPolarity  New polarity (direction) for the train to go in
	 */
	public void setDirection(Polarity newPolarity);
	
        /**
     *getTrackMap returns the track map of the system. 
     * @return ITrack object. Singleton object- refrence to the track class shared
     * by all of this program.
     */
    public ITrack getTrackMap();
}