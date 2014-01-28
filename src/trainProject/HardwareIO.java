package trainProject;


import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import trainProject.BuildTrack;
import trainProject.TPMessage;
import trainProjectEnums.TPMessageType;

//Split up this class into two seperate threads - input and output.
//One class, two methods.
/**
 * HardwareIO - Inner Helper class that handles the hardware IO Q.
 *
 * @author Mark
 */
public class HardwareIO implements Runnable {

    /**
     * mainTrack holds the track data structure.
     */
    BuildTrack mainTrack;
    Socket tcpSock;
    /**
     * trainAddress holds the train's IP address
     */
    InetAddress trainAddress;
    /**
     * CurrentStates reflects the current states of the hardware.
     */
    Object currentStates;
    private boolean stop;
    public boolean wait_for_response;
    private BufferedReader br;
    private int mode;
    Socket s;
    InputStreamReader isr;
    String str;
    PrintWriter output;
    Scanner input;
    public static boolean running = true;
    public ArrayList<String> wig;
    Thread t;
    LinkedBlockingQueue<String> outputQ;
    LinkedBlockingQueue<String> inputQ;
    boolean isReading;
    boolean isWriting;
    boolean justWrote;
    boolean justRead;
    Scanner arduinoReader;
    //Timer vars:
    public long startTime = 0;
    public long lapTime;
    public long timeoutFactor = 500; // set here for timeout
    
    enum HardwareStates
    {
        canWrite,
        waitingForOK,
        reading        
    }
    HardwareStates hardwareState = HardwareStates.canWrite;

    HardwareIO(Socket s, Scanner input, InputStreamReader isr, BufferedReader br, PrintWriter output) {
        this.s = s;
        this.input = input;
        this.isr = isr;
        this.br = br;
        this.output = output;
        wig = new ArrayList<String>();
        outputQ = new LinkedBlockingQueue<>();
        inputQ = new LinkedBlockingQueue<>();

        isReading = false;
        isWriting = false;
        justWrote = false;
        justRead = false;
        stop = false;
        arduinoReader = new Scanner(System.in);
        t = new Thread(this, "T1");
        
        System.out.println("Child thread: " + t);
        t.start();
    }

    public synchronized void addToInput(String cmd) {
        String theCommand = new String(cmd);
        inputQ.add(theCommand);
    }

    public synchronized String getOutput() {
        return outputQ.poll();
    }
    
    /**
     * This is an event handler from the simple event handler class. Due to the group's discussion and 
     * programming time constraints, we have decided not to use EventHandler! However, this
     * debug code is here.
     * @param message 
     */
    @EventHandler
    public void handleString(String message)
    {
        System.out.println("Message received - txt");
        System.out.println(message);
    }
       /**
     * The handleHardwareEvent message is how we are managing input from the outside world. Messages come in and are parsed by this method.
     * @param message the message from the event bus.
     */
    @EventHandler
    public void handleHardwareEvent(TPMessage message)
    {
        if (message.getMessageType() == TPMessageType.UpdateHardware || message.getHardwareCommand() != null)
        {
            //If the message that we get in is hardware related, then we are interested in listening to it.
            addToInput(message.getHardwareCommand().getCommand());
            isWriting = true;
        }
            
    }

 
    /**
     * RUN: This method is the runner for the train hardware system, 
     * sending input and output to the hardware. This method will check to see
     * if it should write to the hardware, write to the hardware, read the response,
     * and if the response is good, then it will set the isWriting to ok.
     */
    public static final int mechaDelay = 200;
    @Override
    public void run() {
         startTime = System.nanoTime();
        try {
            //output.println("*U1{?}");
            // output.println( "*U2{?}");
            while (running) {
                //System.out.println("HardwareInterface Thread, Checking in!");
                Thread.sleep(mechaDelay);
                
//                getCommandsFromArduinoKeyboard();

                if (hardwareState == HardwareStates.canWrite) {
                    doWritingToHardware();
                }
                
                    doReadingFromHardware();
               
                //Next, announce an output.
                if (outputQ.size() > 0) {
                    //announce that we have changed.
                    EventBusService.publish(getOutput());
                }

            }
        } catch (InterruptedException e) {
            System.out.println("Child int.");

        } finally {
            System.out.println("Exiting Child Thread");
        }
    }

    public boolean isTimeout()
    {
       lapTime = startTime - System.nanoTime();
        // see if there was a timeout:
        return (lapTime % timeoutFactor != 0);

    }

    public void resetTimer()
    {
      startTime = System.nanoTime();
    }
    /**
     * DoInput will get stuff from the hardware, parse it, then add it to the
     * input queue
     */
    public void doReadingFromHardware() {
            //Next, read the input from the command console until there is no more:
            String in = input.nextLine();
            //Check to see if it is valid:
            hardwareState = HardwareStates.reading;
            try {
                if (in.contains("OK"))
                {
                    hardwareState = HardwareStates.canWrite;
                    System.out.println("OK Received from CTHREAd");
                }
                
                else if (in.length() > 0) {
                    outputQ.add(in);
                   // System.out.println(in);
                    hardwareState = HardwareStates.canWrite;
                }
            } catch (Exception e) {
                System.out.println("Null string returned - Nothing going on.");
            }
            
            if (isTimeout()) // for timer timeout
        {
            hardwareState = HardwareStates.canWrite;
        }

        
    }

    /**
     * DoOutput will take instructions from the queue and send them to the
     * train.
     */
    public void doWritingToHardware() {
        if (inputQ.isEmpty())
        {
            hardwareState = HardwareStates.reading;
        }
        if (hardwareState == HardwareStates.canWrite) {
            System.out.println("Doing an output command.");
            try {
                String command = inputQ.poll();
                if (!command.isEmpty()) {
                   output.println(command);
                    justWrote = true;
                }
            } catch (Exception e) {
                System.out.println("Exception thrown from output:" + e);
                justWrote = true;
            }

        }
    }

    private void doGetOk() {
        boolean isNotOkay = true;
        String recentInput = "";
        Date giveUpAlready = new Date();
       
            Date shouldIgiveUp = new Date();
            recentInput = input.nextLine();
            if (recentInput.contains("OK") || recentInput.contains("{E}"))
            {
                    isNotOkay = false;
                    System.out.println("debug recentInput" + recentInput);
            }
            if (recentInput.contains("*{S"))
            {
                outputQ.add(recentInput);
            }
            
            //reset the justWrote / waiting 
            
            
        
        this.justWrote = false;
        this.isReading = true;
    }
}
/**
 * Main Class - Info and work Stuff.
 *
 * @author Mark
 */
//    
//    public HardwareInterface(Socket socket, BuildTrack theTrack)
//    {
//        tcpSock = socket;
//        
//    }
//    @Override
//    public void sendCommand(CommandType cmdType, int valueOrPosition) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void sendCommand(CommandType cmdType, Polarity trackPolarity, int valueOrPosition) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void queryHardware() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//      /**
//     * The Run function takes no parameters and runs the thread and loop
//     * system, checking the state of the train, and sending commands as needed.
//     * All hardware update functions and train update functions are
//     * called through the main loop.
//     *
//     */
//    public void setMode(int mode)
//    {      
//        this.mode = mode;
//    }
//    
//    /**
//     * end - for the end of execution.
//     */
//     public void end(){
//       try{
//           wait_for_response = false;
//           br.close();
//       }
//       catch(IOException ioe){
//           System.out.println("ending " + ioe);
//        }
//       stop = true;
//    }
//     
//      
////     private void parser(String response){
////        if(response.compareTo("*S{11}\r") == 0 && mode == TrainControlProgram.BLOCK4TOP){
////            System.out.println("I have been Summoned.");
////          this.transact("*T1{180}");
////        }
////    }
////     /**
////      * reads a line - readLine. This method is from TR - and will
////      * read the line I think.
////      * @param br
////      * @return
////      * @throws Exception 
////      */
////    @SuppressWarnings("empty-statement")
////      private String readLine(BufferedReader br) throws IOException{
////        String strtemp = "";
////        char x;
////        boolean breaker = false;
////        
////        while(breaker==false && stop==false){
////            while(!br.ready() && stop==false);
////            while(br.ready() && stop==false){
////                x = (char)br.read();                
////                strtemp += x;
////
////                if(x == '}'){
////                    breaker = true;
////                    break;
////                }
////            }
////        }
////        return strtemp;
////    }   
////      
////      
////    @Override
////    public void run() {
////       try{
////            br = new BufferedReader(new InputStreamReader(tcpSock.getInputStream()));
////            stop = false;
////            while(stop == false){
////                String line = readLine(br);
////                System.out.println(line);
////                parser(line);
////                wait_for_response = false;
////                Thread.sleep(1);
////            }
////        }
////        catch (InterruptedException ie){
////            System.out.println(ie);
////            System.out.println("I was interrupted");
////        }
////        catch (Exception e){
////            System.out.println(e);
////        }
////    }
//
//    @Override
//    public void run() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//   
//    
//}
