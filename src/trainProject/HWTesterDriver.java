/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProject;

import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import static trainProject.HardwareInterface.s;
import trainProjectEnums.HardwareCommand;

/**
 *
 * @author Mark
 */

public class HWTesterDriver {
    
    static Socket s;
    static InputStreamReader isr;
    static BufferedReader br;
    static String str;
    static PrintWriter output;
    static Scanner input; 
    Thread t;
    
    @EventHandler
    public void handleString(String messageFromHW)
    {
        System.out.println("String from hw");
        System.out.println(messageFromHW);
    }
    
    public static void main(String[] args) {
        

        try {
            HWTesterDriver ttd = new HWTesterDriver();
            EventBusService.subscribe(ttd);

            s = new Socket("localhost", 6543);
            input = new Scanner(s.getInputStream());
            isr = new InputStreamReader(s.getInputStream());
            br = new BufferedReader(isr);
            output = new PrintWriter(s.getOutputStream(), true);
            HardwareIO ts = new HardwareIO(s, input, isr, br, output);
            // ArrayList<TestSocket>ArrayComms = new ArrayList<TestSocket>();
            //ArrayComms.add(new TestSocket(s,input,isr,br,output));
            EventBusService.subscribe(ts);

            System.out.print("Socket connect ok");
            boolean running = true;
            Scanner userInput = new Scanner(System.in);
            //Activate all power:
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 0));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 1));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 2));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 3));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 4));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 5));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 6));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 7));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 8));
            TPMessage.GenerateHardwareMessage(new HardwareCommand(false,false, true, 9  ));
            HardwareCommand hw;
            while(running)
            {
                
                System.out.println("Please enter 1 for block 2 for switch, 0 to quit");
                int swtp = userInput.nextInt();
                if (swtp == 2)
                {
                    System.out.println("Please enter 0 for 90, 1 for 180");
                    int swpol = userInput.nextInt();
                    boolean straight;
                    if (swpol == 0)
                        straight = true;
                    else
                        straight = false;
                    System.out.println("Please e    nter switch number.");
                    int swnum = userInput.nextInt();
                    hw = new HardwareCommand(true, straight,true,swnum);
                    TPMessage.GenerateHardwareMessage(hw);
                    System.out.println("Message sent.");
                }   
                else if(swtp == 0)
                {
                    running = false;
                    ts.running = false;
                }
                else if(swtp == 1)
                {
                    EventBusService.publish("Hi there!");
                }
            }
            
            
            for (int x = 0; x < 20; x++) {

                System.out.println("Asking about the " + x + "th or so circuit:");
                Thread.currentThread().sleep(1000);
                ts.outputQ.add("*B" + x + "{?}");


            }
            ts.running = false;
            // Thread.currentThread().yield();
            //Thread.currentThread().join(1000);
            Thread.currentThread().sleep(5000);
            System.out.println("Should be done.");
            for (long x = 0; x < 1000000000; x++) {
                double iak = Math.sin(1);
            }
        } catch (Exception e1) {
            System.out.println("Main Thread Exception: " + e1);
        }
    
    }
    
}
