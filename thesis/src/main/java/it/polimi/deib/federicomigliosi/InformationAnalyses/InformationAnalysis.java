package it.polimi.deib.federicomigliosi.InformationAnalyses;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.DataInputAssociation;
import org.camunda.bpm.model.bpmn.instance.DataOutputAssociation;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InteractionNode;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.ItemAwareElement;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.xml.sax.SAXException;

import it.polimi.deib.federicomigliosi.InformationStructure.*;

/**
 * The InformationAnalysis class contains methods that analyze the 
 * information inside a BPMN process
 * @author Federico Migliosi
 *
 */
public class InformationAnalysis {
	
	/**
	 * The method getActivityThatReadInformation returns a list of activities
	 * that read the input information
	 * @param XMLfile
	 * @param BPMNFile
	 * @param information
	 * @param single
	 * @return A list of activities
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getActivityThatReadInformation(String XMLfile, String BPMNFile, String information, Boolean single) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		if(single) {
			//Case in which we consider a single information node
			HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectSingle(XMLfile, information);
			
			for(String dataObject: dataObjectList) {
				List<String> activities = InformationSupportMethods.getActivitiesRead(modelInstance, dataObject);
				for(String activity: activities) {
					output.add(activity);
				}
			}
		}else {
			//Case in which we consider the single information node plus all the subtree
			HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectMultiple(XMLfile, information);
			
			for(String dataObject: dataObjectList) {
				List<String> activities = InformationSupportMethods.getActivitiesRead(modelInstance, dataObject);
				for(String activity: activities) {
					output.add(activity);
				}
		}
	}
		return output;
	}
	
	
	/**
	 * The method getParticipantThatReadInformation returns all the participants
	 * that read the input information
	 * @param XMLfile
	 * @param BPMNFile
	 * @param information
	 * @param single
	 * @return A list of participant
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> getParticipantThatReadInformation(String XMLfile, String BPMNFile, String information, Boolean single) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Retrieving all the activities that read the input information
		List<String> activities = getActivityThatReadInformation(XMLfile,BPMNFile,information,single);
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//For each activity the participant is calculated
		for(String activity: activities) {
			String participant = InformationSupportMethods.getParticipant(modelInstance, activity);
			output.add(participant);
		}
		
		return output;
	}
	
	
	
	/**
	 * The getActivityThatWriteInformation method returns a list of activities 
	 * that writes the input information
	 * @param XMLfile
	 * @param BPMNFile
	 * @param information
	 * @param single
	 * @return A list of activities
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getActivityThatWriteInformation(String XMLfile, String BPMNFile, String information, Boolean single) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		if(single) {
			//Case in which we consider a single information node
			HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectSingle(XMLfile, information);
			
			for(String dataObject: dataObjectList) {
				List<String> activities = InformationSupportMethods.getActivitiesWrite(modelInstance, dataObject);
				for(String activity: activities) {
					output.add(activity);
				}
			}
		}else {
			//Case in which we consider the single information node plus all the subtree
			HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectMultiple(XMLfile, information);
			
			for(String dataObject: dataObjectList) {
				List<String> activities = InformationSupportMethods.getActivitiesWrite(modelInstance, dataObject);
				for(String activity: activities) {
					output.add(activity);
				}
		}
	}
		return output;
	}
	
	/**
	 * The getParticipantThatWriteInformation method returns a list of participant
	 * that writes the input information
	 * @param XMLfile
	 * @param BPMNFile
	 * @param information
	 * @param single
	 * @return A list of participant
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getParticipantThatWriteInformation(String XMLfile, String BPMNFile, String information, Boolean single) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();
		
		//Retrieving all the activities that writes the input information
		List<String> activities = getActivityThatWriteInformation(XMLfile,BPMNFile,information,single);
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//For each activity the participant is calculated
		for(String activity: activities) {
			String participant = InformationSupportMethods.getParticipant(modelInstance, activity);
			output.add(participant);
		}
		
		return output;
	}
	
	/**
	 * The getInformationReadByActivity method returns the list of information
	 * read by the input activity
	 * @param XMLfile
	 * @param BPMNFile
	 * @param ActivityID
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getInformationReadByActivity(String XMLfile,String BPMNFile, String ActivityID) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();

		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
				
		//Retrieving the data objects read by the input activity
		List<String> dataObjects = InformationSupportMethods.getReadDataObjects(modelInstance, ActivityID);
		
		//For each data object we calculate the associated information
		for(String dataObject: dataObjects) {
			List<String> information = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, dataObject);
			for(String info: information) {
				output.add(info);
			}
		}
		return output;
	}
	
	/**
	 * The method getInformationReadByParticipant return a list of information
	 * read by the input participant
	 * @param XMLfile
	 * @param BPMNFile
	 * @param participantName
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> getInformationReadByParticipant(String XMLfile,String BPMNFile, String participantName) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the activities performed by the input participant
		List<String> activities =  InformationSupportMethods.getActivities(modelInstance, participantName);
		
		//Calculating the information read by the input participant
		for(String activity: activities) {
			
			List<String> information = getInformationReadByActivity(XMLfile,BPMNFile,activity);
			
			for(String info: information) {
				output.add(info);
			}
			
		}
		return output;
	}
	
	/**
	 * The getInformationWrittenByParticipant method returns
	 * the information written by the input participant
	 * @param XMLfile
	 * @param BPMNFile
	 * @param participantName
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getInformationWrittenByParticipant(String XMLfile,String BPMNFile, String participantName) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the activities performed by the input participant
		List<String> activities =  InformationSupportMethods.getActivities(modelInstance, participantName);
			
		//Calculating the data objects written by the participant
		for(String activity: activities) {
			List<String> dataObjects = InformationSupportMethods.getWrittenDataObjects(modelInstance,activity);
			
			//Calculating the information written by the participant
			for(String dataObject: dataObjects) {
				List<String> information = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, dataObject);
				for(String info: information) {
					output.add(info);
				}
			}
		}
		
		return output;
	}
	
	/**
	 * The method getExitingInformation returns the information exiting
	 * a participant pool
	 * @param XMLfile
	 * @param BPMNFile
	 * @param participantName
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> getExitingInformation(String XMLfile,String BPMNFile, String participantName) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the exiting data objects
		List<String> exitingDataObjects = InformationSupportMethods.getExitingDataObject(modelInstance, participantName);
		
		//For each data object we calculate the associated information
		for(String dataObject: exitingDataObjects) {
			List<String> information = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, dataObject);
			for(String info: information) {
				output.add(info);
			}
		}
		
		return output;
	}
	
	/**
	 * The method getEnteringInformation returns the information entering
	 * a participant pool
	 * @param XMLfile
	 * @param BPMNFile
	 * @param participantName
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> getEnteringInformation(String XMLfile,String BPMNFile, String participantName) throws ParserConfigurationException, SAXException, IOException{
		List<String> output = new ArrayList<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the entering data objects	
		List<String> enteringDataObjects = InformationSupportMethods.getEnteringDataObject(modelInstance, participantName);
		
		//For each data object we calculate the associated information
		for(String dataObject: enteringDataObjects) {
			List<String> information = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, dataObject);
			for(String info: information) {
				output.add(info);
			}
		}
		return output;
	}
	
	/**
	 * The method retrievePreviouslyReadInformation returns all the information
	 * read by the previous activities that might get propagated to the
	 * current input activity
	 * @param XMLfile
	 * @param BPMNFile
	 * @param ActivityID
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> retrievePreviouslyReadInformation(String XMLfile,String BPMNFile, String ActivityID) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Retrieving the input activity as a flow node
		FlowNode activity = modelInstance.getModelElementById(ActivityID);
		
		//Calculating all the previous activities
		List<String> previousActivitiesList = InformationSupportMethods.getPreviousActivities(activity);
		
		//For each previous activity we calculate the read information
		for(String activityString: previousActivitiesList) {
			output.addAll(InformationAnalysis.getInformationReadByActivity(XMLfile, BPMNFile, activityString));
		}
		
		//Calculating all the previous catch events
		List<String> previousCatchEventsList = InformationSupportMethods.getPreviousCatchEvents(activity);
		
		//For each previous catch event we calculate the information transferred
		for(String catchEventID: previousCatchEventsList) {
			IntermediateCatchEvent catchEvent = modelInstance.getModelElementById(catchEventID);
			Collection<DataOutputAssociation> out = catchEvent.getDataOutputAssociations();
			for(DataOutputAssociation di: out) {						
				String dataObjectID = di.getTarget().getId();
				output.addAll(XMLprocessing.fromDataObjectToInformationMultiple(XMLfile, dataObjectID));
			}
		}
		
		
		return output;
		
	}
	
	/**
	 * The method getMessageFlowInformation returns a list of information
	 * that pass through a message flow
	 * @param XMLfile
	 * @param BPMNFile
	 * @param MessageFlowID
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> getMessageFlowInformation(String XMLfile,String BPMNFile, String MessageFlowID) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		MessageFlow messageFlow = modelInstance.getModelElementById(MessageFlowID);
		
		InteractionNode sourceThrowEvent = messageFlow.getSource();
		
		//Calculating the throw event input data objects
		HashSet<String> dataObjectList = new HashSet<String>();
		Collection<DataInputAssociation> out = ((IntermediateThrowEvent) sourceThrowEvent).getDataInputAssociations();
		for(DataInputAssociation di: out) {
			for(ItemAwareElement iae :di.getSources()) {
				dataObjectList.add(iae.getId());
			}
		}
		
		//For each data object we calculate the associated information
		for(String dataObject: dataObjectList) {
			output.addAll(XMLprocessing.fromDataObjectToInformationSingle(XMLfile, dataObject));
		}
		
		return output;

	}
	
	/**
	 * The method fromInformationToMessageFlow returns a list of message flows
	 * where the input information transit
	 * @param XMLfile
	 * @param BPMNFile
	 * @param Information
	 * @return A list of message flow
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> fromInformationToMessageFlow(String XMLfile,String BPMNFile, String Information) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		//Creating the model instance from the .bpmn file
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating all the message flows in the BPMN diagram
		Collection<ModelElementInstance> messageFlowCollection = InformationSupportMethods.getElementsByType(modelInstance,MessageFlow.class);
		
		for(ModelElementInstance modelElementInstance: messageFlowCollection) {
			MessageFlow messageFlow= (MessageFlow) modelElementInstance;
			
			//Calculating the information passing through the message flow
			HashSet<String> informationList = InformationAnalysis.getMessageFlowInformation(XMLfile, BPMNFile, messageFlow.getId());

			for(String information: informationList) {
				//If the information passing through is the same as the input one
				if(information.equals(Information)) {
					output.add(messageFlow.getId());
				}
			}
		
		}
		
		return output;
	}

	
}
