/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trainProject;

import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import graphDataStructure.ITrack;
import java.net.InetAddress;
import trainProjectEnums.CommandType;
import trainProjectEnums.Polarity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import trainProjectEnums.TPMessageType;

/**
 *
 * @author Mark
 */
public class HardwareInterface {

    static Socket s;
    static InputStreamReader isr;
    static BufferedReader br;
    static String str;
    static PrintWriter output;
    static Scanner input;
    Thread t;
    static boolean isRunning;

    public static void main(String args[]) {

        try {

            s = new Socket("localhost", 6543);
            input = new Scanner(s.getInputStream());
            isr = new InputStreamReader(s.getInputStream());
            br = new BufferedReader(isr);
            output = new PrintWriter(s.getOutputStream(), true);
            HardwareIO ts = new HardwareIO(s, input, isr, br, output);
            // ArrayList<TestSocket>ArrayComms = new ArrayList<TestSocket>();
            //ArrayComms.add(new TestSocket(s,input,isr,br,output));

            System.out.print("Socket connect ok");

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
 
    public void sendCommand(CommandType cmdType, int valueOrPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void sendCommand(String command)
    {
        
    }

    public void sendCommand(CommandType cmdType, Polarity trackPolarity, int valueOrPosition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void queryHardware() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

