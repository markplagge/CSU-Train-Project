/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProject;
import com.adamtaft.eb.EventBus;
import com.adamtaft.eb.EventBusService;
import trainProjectEnums.CommandType;
import trainProjectEnums.HardwareCommand;
import trainProjectEnums.TPMessageType;


/**
 * TPMessage - Represents a messageType being sent to the simple bus, and contains the data being passed around.
 * This object has an enum in it that determines the type of messageType.
 * This object has a String that holds the messageType data.
 * @author Mark Plagge
 */
public class TPMessage {
    
    private final TPMessageType messageType;
    private final String messageData;
    private final int numberData;
    private HardwareCommand hardwareCommand;
    
    public TPMessage()
    {
        messageType = null;
        messageData = null;
        numberData = -2;
        hardwareCommand = null;
    }
    public TPMessage(TPMessageType MT, String mData)
    {
        messageType = MT;
        messageData = mData;
        numberData = -1;
        hardwareCommand = null;
    }
    /**

     */
    public TPMessage(TPMessageType MT, String mData, int numberData)
    {
        messageType = MT;
        messageData = mData;
        this.numberData = numberData;
        hardwareCommand = null;
    }

    public TPMessage(TPMessageType messageType, String messageData, int numberData, HardwareCommand hardwareCommand) {
        this.messageType = messageType;
        this.messageData = messageData;
        this.numberData = numberData;
        this.hardwareCommand = hardwareCommand;
    }
    /**
     * The  GenerateHardwareMessage static method generates a hardware relevant message, using the HardwareCommand class.
     * It then sends this message to the event bus.
     * @param HWCommand
     * 
     */
    public static TPMessage GenerateHardwareMessage(HardwareCommand HWCommand)
    {  
        TPMessage fmg = new TPMessage(TPMessageType.UpdateHardware, HWCommand.getCommand(),1,HWCommand);
        EventBusService.publish( fmg );
        return fmg;
    }
    
    
    
    
    

    public TPMessageType getMessageType() {
        return messageType;
    }

    public String getMessageData() {
        return messageData;
    }

    public int getNumberData() {
        return numberData;
    }
    public HardwareCommand getHardwareCommand()
    {
        return hardwareCommand;
    }
    
   
 
    
    
   
}
