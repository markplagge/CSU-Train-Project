package graphDataStructure;

import java.util.Arrays;
import java.util.Observable;

import trainProjectEnums.Polarity;

/**
 * This class creates a virtual representation of the train track. It consists
 * of two arrays. A single dimension array holds all of the switches 
 * contained in the track. A two-dimension array holds the Block numbers
 * of the track. 
 * 
 * The text representation of the switches array from switchesToString():
 * 
 * switches: [ID: 1, ID: 2, ID: 3, ID: 4, ID: 5, ID: 6, ID: 7, ID: 8, ID: 9]
 * 
 * The text representation of the Blocks Adjacency Matrix:
 * 
 *     Adjacency Matrix:
 *  
 *     1. 2. 3. 4. 5. 6. 7. 8. 9.
 *     
 * 1.  0  4  1  0  2  0  0  0  0
 * 2.  4  0  0  4  0  0  0  0  3
 * 3.  1  0  0  0  0  6  8  0  0
 * 4.  0  4  0  0  0  4  0  5  0
 * 5.  2  0  0  0  0  7  9  0  0
 * 6.  0  0  6  4  7  0  0  0  0
 * 7.  0  0  8  0  9  0  0  0  0
 * 8.  0  0  0  5  0  0  0  0  0
 * 9.  0  3  0  0  0  0  0  0  0
 * 
 * The rows represent the fromSwitch. The columns represent the
 * toSwitch. The number in the matrix that intersects the fromSwitch
 * and the toSwitch is the Block number used to traverse from 
 * fromSwitch to toSwitch. A zero indicates that there is not 
 * a Block that can traverse fromSwitch to toSwitch.
 * 
 * @author Mark Plagge, Ian Blake-Knox, Richard Pike
 */

public class Track extends Observable {
	/*
	 * Class Variables
	 */
	public static final int NULL_EDGE = 0;
	private TrackSwitch[] switches;
	private int numSwitches = 0;
	private int[][] adjecencyMatrix;
	private Polarity direction;
	private int nextBlock;
	private int currentBlock;

	/**
	 * Default constructor.
	 * Instantiates a graph with capacity zero vertices.
	 */
	public Track() {
		 switches = new TrackSwitch[0];
		 adjecencyMatrix = new int[0][0];
	}
	/**
	 * Constructor.
	 * Instantiates a graph with capacity of the number of switches present
	 * in the XML file.
	 * 
	 * @param maxV
	 */
	public Track(int numOfSwitches) {
		switches = new TrackSwitch[numOfSwitches];
		adjecencyMatrix = new int[numOfSwitches][numOfSwitches];
	}
	/**
	 * Adds a TrackSwitch to an array that holds the switches.
	 * 
	 * @param trackSwitch
	 */
	public void addTrackSwitch(TrackSwitch trackSwitch) {
		 switches[numSwitches] = trackSwitch;
		 
		 for (int i = 0; i < switches.length; i++) {
			   adjecencyMatrix[numSwitches][i] = NULL_EDGE;
			   adjecencyMatrix[i][numSwitches] = NULL_EDGE;
		 }
		 numSwitches++;
	}
	/**
	 * Used in creating the data structure only. This method uses the originating switch (fromSwitch)
	 * and the destination switch (toSwitch) and the Block id to build an adjacency matrix.
	 * The matrix is used during program processing to access the Block used to get from
	 * fromSwitch to toSwitch.
	 * 
	 * @param fromSwitch
	 * @param direction
	 * @param toSwitch
	 * @param blockID
	 */
	public void addBlock(TrackSwitch fromSwitch, Polarity direction, TrackSwitch toSwitch, int blockID) {
		// Adds an edge with the specified weight from fromSwitch to toSwitch.
		
		 int row = 0;
		 int column = 0;
		
		 row = switchIndexIs(fromSwitch);
		 column = switchIndexIs(toSwitch);
		 adjecencyMatrix[row][column] = blockID;
	}
	/**
	 * Returns the index of a switch from the TrackSwitch array. The method takes a TrackSwitch
	 * parameter to find what its index is in the TrackSwitch array.
	 * 
	 * @param trackSwitch
	 * @return  int
	 */
	public int switchIndexIs(TrackSwitch trackSwitch) {
		 int index = 0;
		 
		 while (!switches[index].equals(trackSwitch)) {
			 index++;
		 }
		 return index;
	}
	/**
	 * parameter passed must be an ID of a switch. The method uses the int id to
	 * return a TrackSwitch.
	 * 
	 * @param id
	 * @return trackSwitch
	 */
	public TrackSwitch getSwitchByID(int id) {
		int index = 0;
		
		while (id != switches[index].getSwitchID()) {
			//System.out.println("getSwitchByID index" + index);
			index++;
		}
		return switches[index];
	}
	/**
	 * Sets the polarity of the current block. This will be used for both of 
	 * the blocks that have power. A special case arise when the train is 
	 * coming out of Block4 at switch1 where the polarity will have to be
	 * set opposite on Block1. 
	 * 
	 * @param dr
	 */
	public void setCurrentPolarity(Polarity dr) {
		this.direction = dr;
	}
	/**
	 * Returns the current direction set for the Blocks. A special case exists
	 * where the train comes out of Block4 and enters Block1. This case must be
	 * addressed in the processing code.
	 * 
	 * @return
	 */
	public Polarity getCurrentPolarity() {
		return direction;
	}
	/**
	 * Returns the entire 2D adjacency matrix if needed.
	 * 
	 * @return adjacencyMatrix
	 */
	public int[][] getAdjacencyMatrix() {
		return adjecencyMatrix;
	}
	/**
	 * If a Block fromSwitch to toSwitch exists, return the number of the 
	 * Block; otherwise returns 0, which indicates that there is not
	 * a Block that exists between fromSwitch to toSwitch.
	 * 
	 * @param fromVertex
	 * @param toVertex
	 * @return Block Number
	 */
	public int getBlock(TrackSwitch fromSwitch, TrackSwitch toSwitch) {
		int row;
		int column;
		
		row = switchIndexIs(fromSwitch);
		column = switchIndexIs(toSwitch);
		 
		return adjecencyMatrix[row][column];
	}
	
	/**
	 * This method uses the "oldNextBlock" to set the current Block.
	 * Triggered when the train crosses a sensor, the method needs
	 * to be called to update the current Block. On sensor fire,
	 * the nextBlock becomes the current Block.
	 * 
	 * @param oldNextBlock
	 */
	public void setCurrentBlock(int oldNextBlock) {
		currentBlock = oldNextBlock;
	}
	/**
	 * Returns the current Block. This is where the train is currently.
	 * 
	 * @return
	 */
	public int getCurrentBlock() {
		return currentBlock;
	}
	/**
	 * Sets the Block that the train will traverse next. This method will
	 * need to be called/updated each time the switch state to the ToSwitch
	 * is changed.
	 * 
	 * @param toSwitch
	 */
	public void setNextBlock(TrackSwitch toSwitch) {
		//TODO testing
		
		Polarity dir = getCurrentPolarity();
		int nextSwitchNum;
		
		if(dir.equals(Polarity.CW)) {
			if(toSwitch.getSwitchState() == 90) {
				nextSwitchNum = toSwitch.getCW_90_AdjSwitchNum();
			}else {
				nextSwitchNum = toSwitch.getCW_180_AdjSwitchNum();
			}
		}else {
			if(toSwitch.getSwitchState() == 90) {
				nextSwitchNum = toSwitch.getCCW_90_AdjSwitchNum();
			}else {
				nextSwitchNum = toSwitch.getCCW_180_AdjSwitchNum();
			}
		}
		nextBlock = getBlock(toSwitch, getSwitchByID(nextSwitchNum));
	}
	
	public int getNextBlock() {
		return nextBlock;
	}
	/**
	 * Used mainly for building and debugging purposes. This method will probably not
	 * be used during the execution of the program.
	 * 
	 * @return String of switch id's
	 */
	public String switchesToString() {
		return Arrays.toString(switches);
	}
	/**
	 * Used mainly for building and debugging purposes. This method will probably not
	 * be used during the execution of the program.
	 * 
	 * @return String representation of the adjacency matrix.
	 */
	public String matrixToString() {
		System.out.println();
		System.out.println(" Adjacency Matrix: ");
		System.out.println();
		
		for (int i = 0; i < adjecencyMatrix.length; i++) {
			for (int j = 0; j < adjecencyMatrix.length; j++) {
				System.out.print(" " + adjecencyMatrix[i][j]);
			}
			System.out.println();
		}
		return "";
	}
}