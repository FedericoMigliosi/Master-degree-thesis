package it.polimi.deib.federicomigliosi.SecurityAnalyses;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.xml.sax.SAXException;

import it.polimi.deib.federicomigliosi.InformationAnalyses.*;
import it.polimi.deib.federicomigliosi.InformationStructure.*;

/**
 * The class SecurityAnalysis contains methods 
 * that perform security analysis on the BPMN diagram;
 * this is based upon security annotations in the diagram.
 * @author Federico Migliosi
 *
 */
public class SecurityAnalysis {
	
	
	/**
	 * The method attackHarmDetectionValidationDataObject checks if
	 * the information contained in the data object always have the 
	 * attackHarmDetection annotation everywhere it is located
	 * @param XMLfile
	 * @param BPMNfile
	 * @param DataObject
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean attackHarmDetectionValidationDataObject(String XMLfile, String BPMNfile, String DataObject) throws ParserConfigurationException, SAXException, IOException {
		
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Checks if the data object exists or not
		if(modelInstance.getModelElementById(DataObject)==null) {
			return false;
		}
		
		//Calculating the information contained in the input data object
		List<String> informationList = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, DataObject);
		
		
		/*
		 * The previously calculated information are spread around
		 * multiple data objects, we calculate those multiple data objects
		 */
		HashSet<String> dataObjectList = new HashSet<String>();		
		for(String information: informationList) {
			dataObjectList.addAll(XMLprocessing.fromInformationToDataObjectMultiple("input.xml", information));
		}
		
		
		//Calculating if there is a data object without the AttackHarmDetection annotation
		for(String dataObjectID: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObjectID,modelInstance,"AttackHarmDetection")) {
				return false;
			}
		}
			
		//Case in which every data object has the AttackHarmDetection annotation
		return true;
	}
	
		
	/**
	 * The method attackHarmDetectionValidationPool checks if 
	 * every information inside the participant pool is 
	 * guaranteed to have AttackHarmDetection
	 * @param XMLfile
	 * @param BPMNfile
	 * @param participantName
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean attackHarmDetectionValidationPool(String XMLfile, String BPMNfile, String participantName) throws ParserConfigurationException, SAXException, IOException {
		
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating all the information read by the participant		
		HashSet<String> informationRead = InformationAnalysis.getInformationReadByParticipant(XMLfile, BPMNfile, participantName);
		
		//Calculating all the information written by the participant
		List<String> informationWritten = InformationAnalysis.getInformationWrittenByParticipant(XMLfile, BPMNfile, participantName);
		
		//Global list of all the information managed by the participant pool
		HashSet<String> informationList = new HashSet<String>();
		informationList.addAll(informationRead);
		informationList.addAll(informationWritten);
		
		/*
		 * The previously calculated information are spread around
		 * multiple data objects, we calculate those multiple data objects
		 */
		HashSet<String> dataObjectList = new HashSet<String>();		
		for(String information: informationList) {
			dataObjectList.addAll(XMLprocessing.fromInformationToDataObjectMultiple("input.xml", information));
		}
		
		//Calculating if there is a data object without the AttackHarmDetection annotation		
		for(String dataObjectID: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObjectID,modelInstance,"AttackHarmDetection")) {
				return false;
			}
		}
			
		//Case in which every data object has the AttackHarmDetection annotation
		return true;
	}
	
	
		
	/**
	 * The method attackHarmDetectionValidationActivity checks if
	 * every information read by the input activity resides in
	 * a data object with AttackHarmDetection
	 * @param XMLfile
	 * @param BPMNfile
	 * @param Activity
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean attackHarmDetectionValidationActivity(String XMLfile, String BPMNfile, String Activity) throws ParserConfigurationException, SAXException, IOException {
		
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Checks if the activity exists or not
		if(modelInstance.getModelElementById(Activity)==null) {
			return false;
		}
		
		//Calculating the information read by the activity
		List<String> informationList = InformationAnalysis.getInformationReadByActivity(XMLfile, BPMNfile, Activity);
		
		//Return false if the activity does not read information
		if(informationList.isEmpty()){
			return false;
		}
		
		
		/*
		 * The previously calculated information are spread around
		 * multiple data objects, we calculate those multiple data objects
		 */
		HashSet<String> dataObjectList = new HashSet<String>();		
		for(String information: informationList) {
			dataObjectList.addAll(XMLprocessing.fromInformationToDataObjectMultiple("input.xml", information));
		}
		//Calculating if there is a data object without the AttackHarmDetection annotation		
		for(String dataObjectID: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObjectID,modelInstance,"AttackHarmDetection")) {
				return false;
			}
		}
			
		//Case in which every data object has the AttackHarmDetection annotation
		return true;
		
	}
	
	/**
	 * The method attackHarmDetectionValidationMessageFlow checks if the
	 * information passing through the message flow is always stored
	 * in a data object with AttackHarmDetection annotation
	 * @param XMLfile
	 * @param BPMNfile
	 * @param MessageFlowID
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean attackHarmDetectionValidationMessageFlow(String XMLfile, String BPMNfile, String MessageFlowID) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the information passing through the message flow
		HashSet<String> informationList = InformationAnalysis.getMessageFlowInformation(XMLfile, BPMNfile, MessageFlowID);
		
		/*
		 * The previously calculated information are spread around
		 * multiple data objects, we calculate those multiple data objects
		 */
		HashSet<String> dataObjectList = new HashSet<String>();		
		for(String information: informationList) {
			dataObjectList.addAll(XMLprocessing.fromInformationToDataObjectMultiple("input.xml", information));
		}
		
		//Calculating if there is a data object without the AttackHarmDetection annotation		
		for(String dataObjectID: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObjectID,modelInstance,"AttackHarmDetection")) {
				return false;
			}
		}
		return true;
	}
	
		
	/**
	 * The method integrityValidationDataObject checks if the information
	 * stored in a data object is always stored in a place with Integrity
	 * @param XMLfile
	 * @param BPMNfile
	 * @param DataObject
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean integrityValidationDataObject(String XMLfile, String BPMNfile, String DataObject) throws ParserConfigurationException, SAXException, IOException {
		
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Checks if the data object exists or not
		if(modelInstance.getModelElementById(DataObject)==null) {
			return false;
		}
		
		//Calculating the information contained in the input data object
		List<String> informationList = XMLprocessing.fromDataObjectToInformationSingle(XMLfile, DataObject);
		
		
		/*
		 * The previously calculated information are spread around
		 * multiple data objects, we calculate those multiple data objects
		 */
		HashSet<String> dataObjectList = new HashSet<String>();
		
		for(String information: informationList) {
			dataObjectList.addAll(XMLprocessing.fromInformationToDataObjectMultiple("input.xml", information));
		}
		
		
		//Check if there is a data object without the Integrity annotation		
		for(String dataObjectID: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObjectID,modelInstance,"Integrity")) {
				return false;
			}
		}
			
		//Case in which every data object has the Integrity annotation		
		return true;
	}
	
	
	
	/**
	 * The method integrityValidationMessageFlow checks, given a message flow,
	 * if the information passing through passes only in message flows
	 * with Integrity
	 * @param XMLfile
	 * @param BPMNfile
	 * @param MessageFlowID
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean integrityValidationMessageFlow(String XMLfile, String BPMNfile, String MessageFlowID) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculating the information passing through the message flow
		HashSet<String> informationList = InformationAnalysis.getMessageFlowInformation(XMLfile, BPMNfile, MessageFlowID);
		
		/*
		 * For each information, calculate all the other message flows
		 * in which it transit. 
		 */
		HashSet<String> globalMessageFlowList = new HashSet<String>();		
		for(String information: informationList) {
			HashSet<String> messageFlowList = InformationAnalysis.fromInformationToMessageFlow(XMLfile, BPMNfile, information);
			globalMessageFlowList.addAll(messageFlowList);
		}
		
		//Checking if all the message flows have the Integrity annotation
		for(String messageFLow: globalMessageFlowList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationMessageFlow(messageFLow, modelInstance, "Integrity")) {
				return false;
			}
		}
		
		//Case for which every message flow has the Integrity annotation
		return true;
	}
	
	
	
		
	/**
	 * The method getInformationWithNonRepudiation calculates the information
	 * that pass through message flows with NonRepudiation
	 * @param XMLfile
	 * @param BPMNfile
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> getInformationWithNonRepudiation(String XMLfile, String BPMNfile) throws ParserConfigurationException, SAXException, IOException{
		HashSet<String> output = new HashSet<String>();
		
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		//Calculatin all the message flows
		Collection<ModelElementInstance> messageFlowCollection = InformationSupportMethods.getElementsByType(modelInstance, MessageFlow.class);
		
		//Calculating all the message flows with NonRepudiation
		HashSet<String> nonRepudiationMessageFlow = new HashSet<String>();
		for(ModelElementInstance modelElementInstance: messageFlowCollection) {
			String messageFlowID = modelElementInstance.getAttributeValue("id");
			if(SecuritySupportMethods.hasSecurityAnnotationMessageFlow(messageFlowID, modelInstance, "NonRepudiation")) {
				nonRepudiationMessageFlow.add(messageFlowID);
			}
		}
		
		/*
		 * For each NonRepudiation message flow, the information
		 * passing though is calculated
		 */
		for(String messageFlowID: nonRepudiationMessageFlow) {
			output.addAll(InformationAnalysis.getMessageFlowInformation(XMLfile, BPMNfile, messageFlowID));
		}
		return output;
	}
	
	/**
	 * The method integrityValidationInformation checks if the input
	 * information is contained only in data objects with integrity and
	 * passes throw message flows with integrity
	 * @param XMLfile
	 * @param BPMNfile
	 * @param information
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Boolean integrityValidationInformation(String XMLfile, String BPMNfile, String information)throws ParserConfigurationException, SAXException, IOException {
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		//Data Objects containing the information
		HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectMultiple(XMLfile, information);
		//Checking if all the data objects have the integrity annotation
		for(String dataObject: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObject, modelInstance, "Integrity")) {
				return false;
			}
		}
		//MessageFlow where the information pass
		HashSet<String> messageFlowSet = InformationAnalysis.fromInformationToMessageFlow(XMLfile, BPMNfile, information);
		//Checking if all the MessageFlows have the integrity annotation
		for(String messageFlow: messageFlowSet) {
			if(!SecuritySupportMethods.hasSecurityAnnotationMessageFlow(messageFlow, modelInstance, "Integrity")) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * The method attackHarmDetectionValidationInformation checks if the input
	 * information is contained only in data objects with AttackHarmDetection and
	 * only passes throw message flows with AttackHarmDetection
	 * @param XMLfile
	 * @param BPMNfile
	 * @param information
	 * @return true/false
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean attackHarmDetectionValidationInformation(String XMLfile, String BPMNfile, String information)throws ParserConfigurationException, SAXException, IOException {
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		//Data Objects containing the information
		HashSet<String> dataObjectList = XMLprocessing.fromInformationToDataObjectMultiple(XMLfile, information);
		//Checking if all the data objects have the attackHarmDetection annotation
		for(String dataObject: dataObjectList) {
			if(!SecuritySupportMethods.hasSecurityAnnotationDataObject(dataObject, modelInstance, "AttackHarmDetection")) {
				return false;
			}
		}
		//MessageFlow where the information pass
		HashSet<String> messageFlowSet = InformationAnalysis.fromInformationToMessageFlow(XMLfile, BPMNfile, information);
		//Checking if all the MessageFlows have the attackHarmDetection annotation
		for(String messageFlow: messageFlowSet) {
			if(!SecuritySupportMethods.hasSecurityAnnotationMessageFlow(messageFlow, modelInstance, "AttackHarmDetection")) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * The method privacyValidation checks if a Pool, Lane or Group
	 * has the Privacy annotation
	 * @param elementID
	 * @param BPMNfile
	 * @return true/false
	 */
	public static Boolean privacyValidation(String elementID, String BPMNfile) {
		File file = new File(BPMNfile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		return SecuritySupportMethods.hasSecurityAnnotationSwimlane(elementID, modelInstance, "Privacy");
		
	}
	

	
}
