package trainProject;

/**
 *
 * @author Mark Plagge, Ian Blake-Knox, Richard Pike
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import com.adamtaft.eb.EventBusService;
import com.adamtaft.eb.EventHandler;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static trainProject.HardwareInterface.s;
import trainProjectEnums.HardwareCommand;

public class TrainControlProgram extends Thread {
    /*
     * Class Variables
     */

    private static Socket s;
    private static BufferedReader br;
    private static TrainControlProgram tcp;
    public static final int STARTUP = 00;
    public static final int STOPALL = 99;
    public static final int EXIT = 100;
    private boolean stop;
    private boolean wait_for_response;
    private boolean menuLoop;
    private String processedSensorNumber;
    private int snh;
    private int index;
    private int limit;
    //private int mode;
    private int placeHolder;
    private int sensorNumber;
    private int sensorHits[] = {3, 2, 1000, //start-up
        1, 10, 14, 6, 1, 10, 14, 6, 1, //outer loop clockwise
        9, 12, 5, 1, 9, 12, 5, 1, 9, //inner loop clock wise
        11, 7, 2, //turn around
        6, 14, 10, 1, 6, 14, 10, 1, //outer loop counter-clockwise
        5, 12, 9, 1, 5, 12, 9, 1, 2000, //inner loop counter-clockwise
        1, 9, 11, 8, 13, 0};
    //shut-down
    //Command Array: This structure contains the commands for the train, 
    //based on each sensor hit. While I would have prefered to use a dictionary,
    //we decided on an array for simplicity.
    private String[][] commands = {
        {"T2{90}", "T1{90}", "B3{CW,7}", "B4{CW,7}"}, //start up
        {"B3{CW,0}", "B1{CCW,7}"}, //sensor 3
        {}, //sensor 2

        {"B1{CW,7}", "B2{CW,7}"}, //switch direction

        {"T5{180}", "B9{CW,7}", "B1{CW,0}"}, //sensor 1	begin outer loop 1 clockwise
        {"B8{CW,7}", "", "B2{CW,0}"}, //sensor 10
        {"T3{180}", "B1{CW,7}", "B9{CW,0}"}, //sensor 14
        {"T1{180}", "B2{CW,7}", "", "B8{CW,0}"}, //sensor 6
        {"T5{180}", "B9{CW,7}", "B1{CW,0}"}, //sensor 1	begin outer loop 2 clockwise
        {"B8{CW,7}", "", "B2{CW,0}"}, //sensor 10
        {"T3{180}", "B1{CW,7}", "B9{CW,0}"}, //sensor 14
        {"T1{180}", "B2{CW,7}", "", "B8{CW,0}"}, //sensor 6
        {"T5{90}", "B7{CW,7}", "B1{CW,0}"}, //sensor 1	begin inner loop 1 clockwise
        {"T6{180}", "B6{CW,7}", "B2{CW,0}"}, //sensor 9
        {"T3{90}", "T5{180}", "B1{CW,7}", "B7{CW,0}"}, //sensor 12
        {"T1{180}", "", "B2{CW,7}", "B6{CW,0}"}, //sensor 5
        {"T5{90}", "T3{180}", "B7{CW,7}", "B1{CW,0}"}, //sensor 1	begin inner loop 2 clockwise
        {"T6{180}", "B6{CW,7}", "B2{CW,0}"}, //sensor 9
        {"T3{90}", "T5{180}", "B1{CW,7}", "B7{CW,0}"}, //sensor 12
        {"T1{180}", "B2{CW,7}", "", "B6{CW,0}"}, //sensor 5
        {"T5{90}", "T3{180}", "B7{CW,7}", "B1{CW,0}"}, //sensor 1	begin turn around
        {"T6{90}", "B4{CW,7}", "B2{CW,0}", "T4{180}"}, //sensor 9
        {"T2{180}", "T5{180}", "B7{CW,0}"}, //sensor 11
        {"T1{90}", "T6{180}", "B1{CCW,7}"}, //sensor 7
        {"T3{180}", "B8{CCW,7}", "", "B4{CW,0}"}, //sensor 2	begin outer loop 1 counter clockwise
        {"B9{CCW,7}", "T1{180}", "B1{CCW,0}"}, //sensor 6
        {"T5{180}", "B2{CCW,7}", "", "B8{CCW,0}"}, //sensor 14
        {"B1{CCW,7}", "", "", "B9{CCW,0}"}, //sensor 10
        {"B8{CCW,7}", "T3{180}", "", "B2{CCW,0}"}, //sensor 1	begin outer loop 2 counter clockwise
        {"B9{CCW,7}", "", "B1{CCW,0}"}, //sensor 6
        {"T5{180}", "B2{CCW,7}", "", "B8{CCW,0}"}, //sensor 14
        {"T1{180}", "B1{CCW,7}", "", "B9{CCW,0}"}, //sensor 10
        {"T3{90}", "B6{CCW,7}", "", "B2{CCW,0}"}, //sensor 1	begin inner loop 1 counter clockwise
        {"T6{180}", "B7{CCW,7}", "B1{CCW,0}"}, //sensor 5
        {"T5{90}", "B2{CCW,7}", "T3{180}", "B6{CCW,0}"}, //sensor 12
        {"T1{180}", "B1{CCW,7}", "", "B7{CCW,0}"}, //sensor 9
        {"T3{90}", "B6{CCW,7}", "T5{180}", "B2{CCW,0}"}, //sensor 1	begin inner loop 2 counter clockwise
        {"T6{180}", "B7{CCW,7}", "B1{CCW,0}"}, //sensor 5
        {"T5{90}", "B2{CCW,7}", "T3{180}", "B6{CCW,0}"}, //sensor 12
        {"T1{180}", "B1{CCW,7}", "", "B7{CCW,0}"}, //sensor 9
        {"B1{CW,7}"}, //sensor 1

        {"T1{180}", "B1{CW,7}", "B2{CW,7}"}, //switch directions again	(41)

        {"T5{90}", "", "B7{CW,7}", "", "B1{CW,0}"}, //sensor 1	begin shut-down
        {"T6{90}", "B4{CW,7}", "B2{CW,0}", "T4{90}"}, //sensor 9
        {"T4{90}", "B5{CW,7}", "B7{CW,0}"}, //sensor 11
        {"T6{180}", "", "B4{CW,0}"}, //sensor 8
        {"B5{CW,0}", "T4{180}"}};							//sensor 13, stop
    //Control for IO control data - vars go here.
    
    

    static InputStreamReader isr;
   // static BufferedReader br;
    static String str;
    static PrintWriter output;
    static Scanner input;
    /**
     * this inits the hardwareIO class, and preps it for communication with the train.
     * @param ipAddy
     * @return 
     */
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
    public static void sendBlockCWCmd(int blockNum, boolean isPoweredOn)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, true, isPoweredOn, blockNum));
    }
    public static void sendBlockCCWCmd(int blockNum, boolean isPoweredOn)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, false, isPoweredOn, blockNum));
    }
    public static void sendSwitch90Cmd(int switchNum)
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(true, false, true, switchNum));
    }
    public static void sendSwitch180CMD(int switchNum )
    {
         TPMessage.GenerateHardwareMessage(new HardwareCommand(false, true, true, switchNum));
    }
    
    /**
     * Initialize class variable that need an initial setting for each run of
     * the program. Create the socket to be used during program execution. Start
     * the program.
     */
    private void init() {
        //new BuildTrack();
        snh = 0;
        index = 0;
        limit = 300;
        sensorNumber = 0;
       
        HardwareIO hardwareSystem = initHardware("192.168.1.1"); //set IP address here.
        EventBusService.subscribe(hardwareSystem);
               
/* OLD INIT CODE 
        snh = 0;
        index = 0;
        limit = 300;
        sensorNumber = 0;

        try {
            s = new Socket("172.26.205.216", 6543);
            //s = new Socket("localhost",6543);

            tcp.start();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }*/
    }

    /**
     * Contains the procedures to start the train on one of the dead-leg
     * segments and automate control of the train until the train has been
     * situated on the main track.
     *
     * The method calls for the thread to pause for 3 seconds to allow any
     * messages that may be left over from previous runs of programs to be
     * cleared from the train hardware.
     *
     * Calls for the initial array index to execute commands.
     */
    private void startUpProcedure() {
        String singleCommand1;

        System.out.println("Clearing");

        pause(3000);

        for (int i = 0; i < commands[0].length; i++) {
            singleCommand1 = "*" + commands[0][i];

            sendCommand(singleCommand1);
        }
    }

    /**
     * Calls a series of commands when the train is located on the main track to
     * allow the train to stop counterclockwise travel.
     *
     * After a brief pause, the train resumes travel on the main track in a
     * clockwise direction.
     */
    public void switchDirection1() {
        String singleCommand2;

        snh = 1000;

        sendCommand("*B4{CW,0}");

        sendCommand("*T2{180}");

        sendCommand("*B1{CCW,0}");

        sendCommand("*T1{180}");

        for (int i = 0; i < commands[3].length; i++) {
            singleCommand2 = "*" + commands[3][i];

            sendCommand(singleCommand2);
        }
    }

    /**
     * Upon completion of the assigned course, this methods calls a brief pause
     * to the thread to not accept input from the hardware. Then the method
     * stops the train and calls commands to resume travel in a clock wise
     * direction to begin the shut down procedure.
     */
    public void switchDirection2() {
        String singleCommand3;

        snh = 2000;

        pause(2000);

        sendCommand("*B2{CCW,0}");

        sendCommand("*B1{CCW,0}");

        sendCommand("*T1{180}");

        for (int i = 0; i < commands[41].length; i++) {
            singleCommand3 = "*" + commands[41][i];

            sendCommand(singleCommand3);
        }
    }

    /**
     * This methods acts as a filter to only accept sensor reading that are
     * needed for the automated train to continue to traverse the assigned
     * course. It takes one parameter. The parameter accepted is the sensor
     * number of a sensor that is passed by any train. If the sensor read
     * matches the next sensor needed to continue the assigned course, the next
     * set of commands is retrieved from the commands array. If the next sensor
     * is not the needed sensor it is ignored.
     *
     * Two special cases are handled here. The special cases are when the train
     * first enters the main track from initial start up (marked as 1000) The
     * second special case is when the train completes the assigned course and
     * begins the shut down procedure (marked as 2000).
     *
     * @param key
     */
    public void wtf(int key) {
        String singleCommand1;

        System.out.println("key: " + key);
        System.out.println("sensor hits: " + sensorHits[index]);

        if (key == sensorHits[index]) {

            try {
                index++;
                placeHolder++;
            } catch (Exception e) {
                System.out.println("end of pointer array");
            }

            if (sensorHits[index] == 1000) {
                switchDirection1();
                index++;
                placeHolder++;
            } else if (sensorHits[index] == 2000) {
                switchDirection2();
                index++;
                placeHolder++;
            } else if (sensorHits[index] == 8) {
                pause(2000);

                sendCommand("*B5{CW,0}");

                sendCommand("*T4{180}");
            } else {

                for (int j = 0; j < commands[placeHolder].length; j++) {
                    singleCommand1 = "*" + commands[placeHolder][j];

                    sendCommand(singleCommand1);
                }
            }
        }
    }

    /**
     * This methods continually monitors for input from the keyboard while the
     * program is running. While in the loop processing stops at getInut() and
     * waits for keyboard input to be entered at any time.
     *
     * menuLoop is set to false when (100) is entered from the keyboard and the
     * program terminates.
     */
    private void execute() {
        try {

            menuLoop = true;

            while (menuLoop == true) {
                System.out.println("Communicating with MTC101 Controller");
                System.out.println("00.) Start Train");
                System.out.println("99.) Stop All");
                System.out.println("100.) Exit");

                int keyFromConsole = getInput();
                upDateTrack(keyFromConsole);

                System.out.println("----------------------------------------------------------------");

            }
            System.out.println("ending and closing");
            tcp.end();
            s.close();
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    /**
     * This method validates the keyboard input. If the input from the keyboard
     * is not valid it prompts for a different valid input to be entered.
     *
     * @return
     */
    public int getInput() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        do {
            try {
                System.out.print("Enter selection: ");
                System.out.flush();
                input = in.readLine();


            } catch (IOException nfe) {
                System.out.println("Invalid entry, try again");
                //transact(s, input);
            }
            try {
                Integer val = Integer.parseInt(input);

                //return Integer.parseInt(in.readLine());
                return Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("RC E");
                transact(s, input);
            }
            //catch(IOException ioe){
            //    System.out.println(ioe);
            // }
        } while (true);
    }

    /**
     * This method takes one parameter. The parameter is input from the key
     * board or a sensor reading. If the keyInput is a valid input from the key
     * board the program processes the desired command. Otherwise, the program
     * passes the sensor reading to wtf() to be processed for train traversal.
     *
     * @param keyInput
     */
    private void upDateTrack(int keyInput) {

        switch (keyInput) {
            case STARTUP: //begin start up procedure
                startUpProcedure();
                break;

            case STOPALL: //stop all
                System.out.println("stop all");

                //turn off these blocks
                tcp.stopTrain();
                waitForResponse();
                break;

            case EXIT: //exit
                System.out.println("exit");

                sendCommand("*T1{180}");
                sendCommand("*T2{180}");
                sendCommand("*T3{180}");
                sendCommand("*T4{180}");
                sendCommand("*T5{180}");
                sendCommand("*T6{180}");

                menuLoop = false;
                break;

            default:
                wtf(keyInput);
        }
    }

    /**
     * This method is a simple while loop that eats up time. It is intended to
     * allow the thread to continue to accept sensor reading to be processed.
     */
    /*public void hold(){
     int counter = 0;
     boolean hold = false;
		
     while(hold == false){
     if(counter == 10000){
     hold = true;
     }
     counter++;
     }
     }*/
    /**
     * This method is a simple while loop that eats up more time than the
     * previous hold(). It is intended to allow the thread to continue to accept
     * sensor reading to be processed.
     */
    /*public void hold2(){
     int counter = 0;
     boolean hold = false;
		
     while(hold == false){
     if(counter == 20000000000){
     hold = true;
     }
     counter++;
     }
     }*/
    /**
     * A single point to send commands to the HardwarIO class. This method
     * allows all commands from this class to be sent to the Arduino Controller.
     *
     * The Arduino controller will then process the commands as if they were
     * entered from the hand controller.
     *
     * @param command
     */
    public void sendCommand(String command) {
        //what command is it?
        boolean isCCW = false;
        boolean is90 = false;
        boolean isBlockCommand = false;
        //int devNumber = 0;
        String tempCommand = new String(command);
        //get the block number:
        Matcher matcher = Pattern.compile("\\d+").matcher(tempCommand);
        matcher.find();
        int devNumber = Integer.valueOf(matcher.group());
        
        
        
        if(command.contains("B"))
            isBlockCommand = true;
        else
            isBlockCommand = false;
        
        if(isBlockCommand)
        {
            if(command.contains("CCW")) // CCW Command
                //we are a CCW Block:
            {
            TrainControlProgram.sendBlockCCWCmd(limit, is90);
            
            }
 
           
        tcp.transact(s, command);
        pause(250);
        waitForResponse();
        }
        
        
    }

    /**
     * This method takes the output from the train hardware and pulls the number
     * out of it. The output from the train hardware is formatted in this
     * manner, S{5}. This methods sets the sensorNUmber variable to (5).
     *
     * @param string
     */
    public void setSensorInput(String string) {
        processedSensorNumber = string.substring(string.indexOf("{") + 1, string.indexOf("}"));

        try {
            sensorNumber = Integer.parseInt(processedSensorNumber);
            if (sensorNumber != snh) {
                upDateTrack(sensorNumber);
                wait_for_response = false;
            }
        } catch (NumberFormatException e) {
            if (processedSensorNumber.equals("OK")) {
                wait_for_response = false;
            }
        }
    }

    /*public int getSensorInput(){
     return sensorNumber;
     }*/
    public void run() {
        try {
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            stop = false;

            while (stop == false) {
                String line = readLine(br);
                setSensorInput(line);
                System.out.println(line);		//This is where the sensor messages print to the console
                //parser(line);
                wait_for_response = false;
                pause(1);
            }
        } catch (InterruptedException ie) {
            System.out.println(ie);
            System.out.println("I was interrupted");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void waitForResponse() {
        int counter = 0;

        while (wait_for_response == true) {
            if (counter == limit) {
                wait_for_response = false;
            }
            pause(1);
            counter++;
        }
    }

    public void transact(Socket s, String message) {
        try {
            wait_for_response = true;
            PrintStream osw = new PrintStream(s.getOutputStream(), true);
            osw.println(message);
            System.out.println(message);
            waitForResponse();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
    }

    public void stopTrain() {
        transact(s, "*B1{CCW,0}");
        transact(s, "*B2{CCW,0}");
        transact(s, "*B3{CCW,0}");
        transact(s, "*B4{CCW,0}");
        transact(s, "*B5{CCW,0}");
        transact(s, "*B6{CCW,0}");
        transact(s, "*B7{CCW,0}");
        transact(s, "*B8{CCW,0}");
        transact(s, "*B9{CCW,0}");
        transact(s, "*L{170}");
    }

    /*public void setMode(int mode){
     this.mode = mode;
     }*/

    /*private void parser(String response){
     if(response.compareTo("*S{11}\r") == 0 && mode == TrainControlProgram.BLOCK4TOP){
     System.out.println("I have been Summoned.");
     tcp.transact(s,"*T1{180}");
     }
     }*/
    private String readLine(BufferedReader br) throws Exception {
        String strtemp = "";
        char x;
        boolean breaker = false;

        while (breaker == false && stop == false) {
            while (!br.ready() && stop == false);
            while (br.ready() && stop == false) {
                x = (char) br.read();
                strtemp += x;

                if (x == '}') {
                    breaker = true;
                    break;
                }
            }
        }
        return strtemp;
    }

    public void end() {
        try {
            wait_for_response = false;
            br.close();
        } catch (IOException ioe) {
            System.out.println("ending " + ioe);
        }
        stop = true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        tcp = new TrainControlProgram();
        tcp.init();
        tcp.execute();
    }
}
