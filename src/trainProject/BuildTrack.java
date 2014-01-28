package trainProject;

/**
 * 
 * 
 * @author Mark Plagge, Ian Blake-Knox, Richard Pike
 */

import graphDataStructure.Track;
import trainProjectEnums.Polarity;
import graphDataStructure.TrackSwitch;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class BuildTrack {
/**
 * class variables
 */
	//static TrainProjectMain tpm;
	public Track track;
	private TrackSwitch ts;
	
	
	public BuildTrack() {
		createTrackFromXML();
	}
/**
 * reads the XML file representation of the track. The file must be read twice because of the
 * way the load matrix is set up. The loadMatrix methods use the current switch and the next 
 * adjacent switch to find the Block it needs to use to traverse to the next switch.
 * If the next switch is not loaded into the switch array the loadMatrix cannot use 
 * the switch to locate the Block.
 */
	private void createTrackFromXML(){
		//Class Variables
		int id;
		int switchState;
		String sensorNumber_180;
		String sensorNumber_90;
		int cw180_BlockID;
		int cw180_AdjSwitchNum;
		int cw90_BlockID;
		int cw90_AdjSwitchNum;
		int ccw180_BlockID;
		int ccw180_AdjSwitchNum;
		int ccw90_BlockID;
		int ccw90_AdjSwitchNum;
		
		try {
			File fXmlFile = new File("trackRepresentation2.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
			NodeList nList = doc.getElementsByTagName("switch");
			
			track = new Track(nList.getLength());		//Create the track with the correct number of switches alloted.
 
			//Load all the switches into an array
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					//System.out.println("current switch: " + eElement.getAttribute("id"));
					
					id 					= Integer.parseInt(eElement.getAttribute("id"));
					switchState 		= Integer.parseInt(eElement.getElementsByTagName("initialSwitchState").item(0).getTextContent());
					sensorNumber_180 	= eElement.getElementsByTagName("sensorNumber_180").item(0).getTextContent();
					sensorNumber_90 	= eElement.getElementsByTagName("sensorNumber_180").item(0).getTextContent();
					cw180_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("cw180_AdjacentSwitchNumber").item(0).getTextContent());
					cw90_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("cw90_AdjacentSwitchNumber").item(0).getTextContent());
					ccw180_AdjSwitchNum = Integer.parseInt(eElement.getElementsByTagName("ccw180_AdjacentSwitchNumber").item(0).getTextContent());
					ccw90_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("ccw90_AdjacentSwitchNumber").item(0).getTextContent());
					
					
					ts = new TrackSwitch(id, switchState, sensorNumber_180, sensorNumber_90, cw180_AdjSwitchNum,
							cw90_AdjSwitchNum, ccw180_AdjSwitchNum, ccw90_AdjSwitchNum);
					track.addTrackSwitch(ts);
				}
			}
			//Load the blocks into a clockwise adjacency matrix
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					//System.out.println("current switch: " + eElement.getAttribute("id"));
					
					cw180_BlockID 		= Integer.parseInt(eElement.getElementsByTagName("cw180_AdjacentBlockNumber").item(0).getTextContent());
					cw180_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("cw180_AdjacentSwitchNumber").item(0).getTextContent());
					cw90_BlockID 		= Integer.parseInt(eElement.getElementsByTagName("cw90_AdjacentBlockNumber").item(0).getTextContent());
					cw90_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("cw90_AdjacentSwitchNumber").item(0).getTextContent());
					ccw180_BlockID 		= Integer.parseInt(eElement.getElementsByTagName("ccw180_AdjacentBlockNumber").item(0).getTextContent());
					ccw180_AdjSwitchNum = Integer.parseInt(eElement.getElementsByTagName("ccw180_AdjacentSwitchNumber").item(0).getTextContent());
					ccw90_BlockID 		= Integer.parseInt(eElement.getElementsByTagName("ccw90_AdjacentBlockNumber").item(0).getTextContent());
					ccw90_AdjSwitchNum 	= Integer.parseInt(eElement.getElementsByTagName("ccw90_AdjacentSwitchNumber").item(0).getTextContent());
			
					if(cw180_AdjSwitchNum != 909){
						TrackSwitch ts1 = track.getSwitchByID(Integer.parseInt(eElement.getAttribute("id")));
						TrackSwitch ts2 = track.getSwitchByID(cw180_AdjSwitchNum);
						//System.out.println("switch 1: " + ts1.toString());
						//System.out.println("switch 2: " + ts2.toString());
						track.addBlock(ts1, Polarity.CW, ts2, cw180_BlockID);
					}
					if(cw90_AdjSwitchNum != 909){
						TrackSwitch ts1 = track.getSwitchByID(Integer.parseInt(eElement.getAttribute("id")));
						TrackSwitch ts2 = track.getSwitchByID(cw90_AdjSwitchNum);
						//System.out.println("switch 1: " + ts1.toString());
						//System.out.println("switch 2: " + ts2.toString());
						track.addBlock(ts1, Polarity.CW, ts2, cw90_BlockID);
					}
					if(ccw180_AdjSwitchNum != 909){
						TrackSwitch ts1 = track.getSwitchByID(Integer.parseInt(eElement.getAttribute("id")));
						TrackSwitch ts2 = track.getSwitchByID(ccw180_AdjSwitchNum);
						//System.out.println("switch 1: " + ts1.toString());
						//System.out.println("switch 2: " + ts2.toString());
						track.addBlock(ts1, Polarity.CCW, ts2, ccw180_BlockID);
					}
					if(ccw90_AdjSwitchNum != 909){
						TrackSwitch ts1 = track.getSwitchByID(Integer.parseInt(eElement.getAttribute("id")));
						TrackSwitch ts2 = track.getSwitchByID(ccw90_AdjSwitchNum);
						//System.out.println("switch 1: " + ts1.toString());
						//System.out.println("switch 2: " + ts2.toString());
						track.addBlock(ts1, Polarity.CCW, ts2, ccw90_BlockID);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("switches: " + track.switchesToString());
		//System.out.println(track.matrixToString());
	}							
}