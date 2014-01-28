/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProject;

import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static trainProject.HardwareInterface.s;
import trainProjectEnums.HardwareCommand;
/**
 *
 * @author Mark
 */
public class HardwareIOExample {
    static Socket s;
    static InputStreamReader isr;
    static BufferedReader br;
    static String str;
    static PrintWriter output;
    static Scanner input;
    
    /***
     * handleString is the method that runs when there is a message sent through
     * the event bus. These strings include any chatter, and all *S{n} messages, so
     * you will  have to filter them. Here, we only print out a message if they are a proper 
     * signal.
     * @param messageFromHW 
     */
    @EventHandler
    public void handleString(String messageFromHW)
    {
        String properMessage = "*S{";
        if (messageFromHW.contains(properMessage))
        {
            System.out.println("This is a message from the sensors:");
            System.out.println(messageFromHW);
        }
    }
    
    
    //to use the hardware interface, you must initalize it. Here's how you do that:
    public HardwareIO initHardware(String ipAddy)
    {
        HardwareIO ts = null;
        try
        {
            //Create a new socket, and point it to the train server.
            s = new Socket(ipAddy, 6543);
            //aim a scanner at the socket to get the data form the server.
            input = new Scanner(s.getInputStream());
            //aim a reader at the socket.
            isr = new InputStreamReader(s.getInputStream());
            //get the buffered reader from the ISR
            br = new BufferedReader(isr);
            //set the output up for the IO interface.
            output = new PrintWriter(s.getOutputStream(), true);
            //create a new hardwareIO object. It is now running
            ts = new HardwareIO(s, input, isr, br, output);
            // ArrayList<TestSocket>ArrayComms = new ArrayList<TestSocket>();
            //ArrayComms.add(new TestSocket(s,input,isr,br,output));
            //Make sure you register the newly created HardwareIO with the event bus service:
            EventBusService.subscribe(ts);

            System.out.print("Socket connect ok");
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(HardwareIOExample.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HardwareIOExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ts;
            
    }
    //The other thing you have to do is send the machine some commands. Here are some Examples:
    public void sendBlockCWCmd(int blockNum, boolean isPoweredOn)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, true, isPoweredOn, blockNum));
    }
    public void sendBlockCCWCmd(int blockNum, boolean isPoweredOn)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, false, isPoweredOn, blockNum));
    }
    public void sendSwitch90Cmd(int switchNum)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(true, false, true, switchNum));
    }
    public void sendSwitch180CMD(int switchNum )
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, true, true, switchNum));
    }
    
    //because this 
    public static void main(String[] args)
    {
        //create an instance of this class so we can play with it.
        HardwareIOExample internalRep = new HardwareIOExample();
        //here we creawte the hardwareIO comms.
        HardwareIO hardwareSystem = internalRep.initHardware("localhost"); 
        
        //and we need to register the newly created classes with the eventbus:
        EventBusService.subscribe(hardwareSystem);
        EventBusService.subscribe(internalRep);
        //lets send it some commands as a test:
        internalRep.sendBlockCCWCmd(2, true);
        internalRep.sendBlockCCWCmd(1, true);
        internalRep.sendBlockCCWCmd(7, true);
        internalRep.sendBlockCCWCmd(8, true);
        internalRep.sendBlockCCWCmd(9, true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HardwareIOExample.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Done sleeping, doing CW commands now.");
        internalRep.sendBlockCWCmd(2, true);
        internalRep.sendBlockCWCmd(1, true);
        internalRep.sendBlockCWCmd(7, true);
        internalRep.sendBlockCWCmd(8, true);
        internalRep.sendBlockCWCmd(9, true);
    }
}
