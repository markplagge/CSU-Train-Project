/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProjectEnums;

/**
 *
 * @author csu
 */
public class HardwareCommand {
    CommandType myCommandType;
    int blockNum;
    boolean powerOrSwitched;
    boolean straightCW;

    public HardwareCommand() {
    }

    public HardwareCommand(CommandType myCommandType) {
        this.myCommandType = myCommandType;
    }
    
    public HardwareCommand(boolean isSwitchCommand, boolean straightSwitchOrCW, boolean blockIsOn, int switchOrBlockNumber)
    {
        
        if (isSwitchCommand)
        {
            this.myCommandType = CommandType.SetTurnout;
        }
        else if (blockIsOn)
        {
        this.myCommandType = CommandType.SetBlock;
        }
        else
        {
        this.myCommandType = CommandType.StopBlock;
        }
        
        
        this.blockNum = switchOrBlockNumber;
        this.powerOrSwitched = isSwitchCommand;
        this.straightCW = straightSwitchOrCW;
    }
    
    
    
    public String getCommand()
    {
        String command;
        command = myCommandType.generateCommand(straightCW, blockNum);
        //command = myCommandType.generateCommand(powerOrSwitched, blockNum);
        return command;
    }
     
}
