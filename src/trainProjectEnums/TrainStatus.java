package trainProjectEnums;

/**
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 * 
 * TrainStatus represents the train's  status. There are 3 possible
 * selections here:
 * stopped -> ITrain is stopped.
 * running CW -> ITrain is running in a clockwise direction
 * running CCW -> ITrain is running in a counter-clockwise direction
 */
public enum  TrainStatus {
		/**
     * Running Status
     */
    running,
		/**
     * Stopped Status
     */
    stopped
	}
