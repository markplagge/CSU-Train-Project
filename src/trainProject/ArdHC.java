package trainProject;

import java.util.Scanner;

/**
 * ArdHC - Uses the arduino hand control to talk to the train.
 * @author Mark Plagge
 * @author Ian Blake-Knox
 * @author Richard Pike
 */
public class ArdHC implements Runnable {

    public static Scanner arduinoKBD;
    Thread t;
    public ArdHC()
    {
      this.arduinoKBD = new Scanner(System.in);  
       t = new Thread(this,"RDRTH");
       t.start();
    }
    @Override
    public void run() {
        String inputLine;
        //hrow new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        while(arduinoKBD.hasNext())
        {
            inputLine = arduinoKBD.nextLine();
            if (inputLine.startsWith("*"))
            {
                System.out.println("match:" + inputLine);
            }
        }
    }
    public static void main(String[] args) {
        ArdHC ad = new ArdHC();
        while(true)
        {
        }
        }
}
