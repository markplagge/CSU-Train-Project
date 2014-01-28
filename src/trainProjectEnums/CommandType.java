package trainProjectEnums;

/**
 * This class encapsulates the possible commands sent to the hardware device. The
 * command is contained within the class so that the hardware interface merely needs
 * to send the command to the data. 
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 * Feb 17, 2013
 */
public enum CommandType {
    StopBlock,
    /**
     * Represents the Set Block command.
     */
    SetBlock, 
    /**
     * Represents the Get Block Command.
     */
    GetBlock,
    /**
     * Represents the Set Turnout command.
     */
    SetTurnout,
    /**
     *  Represents the Get Turnout command.
     */
    GetTurnout,
    /**
     *  Represents the Toggle Turnout command
     */
    ToggleTurnout,
    /**
     *  Represents the SetLED command
     */
    SetLED,
    /**
     * Represents the GetLED command
     */
    GetLED,
    /**
     *Represents the Get User Control command
     */
    GetUserControl,
    /**
     *Represents the Sensor Trigger command
     */
    SensorTrigger,
    /**
     *Represents the Stop command
     */
    Stop,
    /**
     * Represents the Error command
     */
    Error;
    /**
     * generateCommand -> Based on the state of the commandTpe, the enum
     * will generate a command string for the hardware interface.
     * @param p Polarity of the track
     * @param speed Speed desired
     * @return 
     */
    
    String generateCommand(boolean isCWOrStraight, int switchOrBlockNumber)
    {
        
        switch (this)
            {
            case SetBlock: 
                String direction = "CCW";
                if (isCWOrStraight)
                    direction = "CW";
                return "*B" + switchOrBlockNumber + "{" + direction + ",6}";
                
            case SetTurnout:
                String turnoutSwitch = "180";
                if (isCWOrStraight)
                    turnoutSwitch = "90";
                return "*T" + switchOrBlockNumber + "{" + turnoutSwitch + "}";
            
            case StopBlock:
                String dir = "CCW";
                return "*B" + switchOrBlockNumber + "{" + dir + ",0}";
                                 
            default:
                throw new AssertionError(this.name());
            }        
    }


    			
    	
    /**
     * This is the single value variant. Specific commands require only one value
     * for the hardware to processes properly. This command will generate the one
     * valued command for the hardware interface.
     * @param positionOrValue  The position or value function - Referenced  via the software commands chart. 
     * Please see the chart for full reference.
     * @return 
     */
    String generateCommand(int positionOrValue)
    {
        switch (this)
        {
            case GetBlock: return "*B" + positionOrValue + "{?}";
                
            case GetTurnout: return "*T" + positionOrValue + "{?}";
            case ToggleTurnout: return "*T" + positionOrValue + "{T}";              
            case SetLED: return "Fuck LEDs";                
            case GetLED: return "Fuck LEDs";                
            case GetUserControl: return "*U" + positionOrValue + "{?}";               
            case Stop: return "*M{STOP}";                 
            case Error: return "ERROR";
                
            default:
                throw new AssertionError(this.name());
        }
       
    }
    
}
