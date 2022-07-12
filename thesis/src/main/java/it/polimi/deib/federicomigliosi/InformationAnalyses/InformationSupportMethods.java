package it.polimi.deib.federicomigliosi.InformationAnalyses;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.IntermediateCatchEventImpl;
import org.camunda.bpm.model.bpmn.impl.instance.TaskImpl;
import org.camunda.bpm.model.bpmn.instance.Activity;
import org.camunda.bpm.model.bpmn.instance.DataInputAssociation;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.InteractionNode;
import org.camunda.bpm.model.bpmn.instance.IntermediateCatchEvent;
import org.camunda.bpm.model.bpmn.instance.IntermediateThrowEvent;
import org.camunda.bpm.model.bpmn.instance.ItemAwareElement;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.Participant;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.camunda.bpm.model.bpmn.instance.DataOutputAssociation;
//
/**
 * The InformationSupportMethods class contains methods that analyze
 * the BPMN diagram
 * @author Federico Migliosi
 *
 */
public class InformationSupportMethods {
	
	/**
	 * The method getName returns the name of a BPMN object
	 * from a diagram file
	 * @param BPMNFile
	 * @param objectID
	 * @return The name of the BPMN object
	 */
	public static String getName(String BPMNFile, String objectID) {
		
		File file = new File(BPMNFile);
		BpmnModelInstance modelInstance = Bpmn.readModelFromFile(file);
		
		String name = modelInstance.getModelElementById(objectID).getAttributeValue("name");
		
		return name;
	}
	
	
	/**
	 * The method getFlowingFlowNodes returns all the subsequent nodes 
	 * @param node (must be an Activity of Gateway)
	 * @return A Collection of FlowNode
	 */
	public static Collection<FlowNode> getFlowingFlowNodes(FlowNode node) {
		  Collection<FlowNode> followingFlowNodes = new ArrayList<FlowNode>();
		  for (SequenceFlow sequenceFlow : node.getOutgoing()) {
		    followingFlowNodes.add(sequenceFlow.getTarget());
		  }
		  return followingFlowNodes;
		}
	

	/**
	 * The method getElementsByType returns all the BPMN elements of the same type
	 * inside the BPMN diagram
	 * @param modelInstance
	 * @param element
	 * @return A collection with all the elements of the same type in the BPMN diagram
	 */
	public static Collection<ModelElementInstance> getElementsByType(BpmnModelInstance modelInstance,
			Class<? extends ModelElementInstance> element) {

		ModelElementType elementType = modelInstance.getModel().getType(element);

		Collection<ModelElementInstance> elementCollection = modelInstance.getModelElementsByType(elementType);

		return elementCollection;
	}

	
	/**
	 * The method getPartecipant returns the participant executing an activity
	 * @param modelInstance
	 * @param activityID
	 * @return The participant name
	 */
	public static String getParticipant(BpmnModelInstance modelInstance, String activityID) {
		try {
			//Retrieving the input activity
			Activity activity = modelInstance.getModelElementById(activityID);

			//Retrieving the ID of the process containing the activity
			String processID = activity.getScope().getAttributeValue("id");

			//Calculating all the Participant
			Collection<ModelElementInstance> participantSet = getElementsByType(modelInstance,
					Participant.class);

			for (ModelElementInstance element : participantSet) {
				Participant participant = (Participant) element;
				String idProcess = participant.getProcess().getId();
				if (idProcess.equals(processID)) {
					return participant.getAttributeValue("name");
				}
			}

		} catch (NullPointerException e) {
			System.out.println("ERROR");
			System.out.println("* The Activity ID is wrong or it does not exists");
		}
		return null;
	}
	
	
	/**
	 * The method getActivities return all the activities 
	 * associated with a participant
	 * @param modelInstance
	 * @param participantName
	 * @return A list of activities
	 */
	public static List<String> getActivities(BpmnModelInstance modelInstance, String participantName) {
		List<String> output = new ArrayList<String>();
		Collection<ModelElementInstance> activitySet = getElementsByType(modelInstance, Activity.class);
		Collection<ModelElementInstance> participantSet = getElementsByType(modelInstance, Participant.class);
		//Calculating the process ID associated with the input participant
		String process = null;
		for (ModelElementInstance p : participantSet) {
			if (p.getAttributeValue("name").equals(participantName)) {
				process = p.getAttributeValue("processRef");
			}
		}
		//Calculating all the activities of the process
		for (ModelElementInstance a : activitySet) {
			if (a.getParentElement().getAttributeValue("id").equals(process)) {
				output.add(a.getAttributeValue("id"));
			}
		}
		return output;
	}
	
	/**
	 * The method getReadDataObjects returns all the data objects read by the
	 * input activity
	 * @param modelInstance
	 * @param activityID
	 * @return A list of data objects
	 */
	public static List<String> getReadDataObjects(BpmnModelInstance modelInstance, String activityID){
		List<String> output = new ArrayList<String>();
		
		
		try {
			//Retrieving the input activity
		Activity activity = modelInstance.getModelElementById(activityID);
		
		Collection<DataInputAssociation> dataInputAssociations=activity.getDataInputAssociations();
		
		//For each data input association we calculate the source data object
		for(DataInputAssociation i: dataInputAssociations) {
			
			Collection<ItemAwareElement> sources=i.getSources();
			
			for(ItemAwareElement it: sources) {
				output.add(it.getId());
				
			}
		}
		FlowNode activityFlowNode = modelInstance.getModelElementById(activityID);
		
		//Calculating the data objects read through message flows
		for(SequenceFlow sf: activityFlowNode.getIncoming()) {
			//If the activity is connected to a catch event
			if(sf.getSource().getClass().equals(IntermediateCatchEventImpl.class)) {
				IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) sf.getSource();
				Collection<DataOutputAssociation> doaList = catchEvent.getDataOutputAssociations();
				//For each data input association we calculate the source data object
				for(DataOutputAssociation doa: doaList) {
					output.add(doa.getTarget().getId());
				}
				
			}
		}
		
		}catch (NullPointerException e) {
			System.out.println("ERROR");
			System.out.println("* The Activity ID is wrong or it does not exists");
		}
		
		return output;
	}
	
	/**
	 * The method getReadDataObjects returns all the data objects written by the
	 * input activity
	 * @param modelInstance
	 * @param activityID
	 * @return A list of data objects
	 */
	public static List<String> getWrittenDataObjects(BpmnModelInstance modelInstance, String activityID) {
		List<String> output = new ArrayList<String>();
		try {

			//Retrieving the input activity
			Activity activity = modelInstance.getModelElementById(activityID);
			
			Collection<DataOutputAssociation> dataOutputAssociations = activity.getDataOutputAssociations();
			
			for(DataOutputAssociation o: dataOutputAssociations) {
				
				output.add(o.getTarget().getId());
			}			

		} catch (NullPointerException e) {
			System.out.println("ERROR");
			System.out.println("* The Activity ID is wrong or it does not exists");
		}

		return output;
	}
	
	
	/**
	 * The method getActivitiesWrite returns all the activities that writes 
	 * on the given data object
	 * @param modelInstance
	 * @param dataObjectID
	 * @return A list of activities
	 */
	public static List<String> getActivitiesWrite(BpmnModelInstance modelInstance, String dataObjectID){
		List<String> output = new ArrayList<String>();

		//Retrieving all the activities in the BPMN file
		Collection<ModelElementInstance> activitySet = InformationSupportMethods.getElementsByType(modelInstance,
				Activity.class);
		
		//For every activity
		for(ModelElementInstance mei: activitySet) {
			Activity activity = (Activity) mei;
			//Calculating the data input association of each activity
			Collection<DataOutputAssociation> dataOutputAssociations = activity.getDataOutputAssociations();	
			for(DataOutputAssociation dataOutputAssociation: dataOutputAssociations) {
				String outputDataObjectID = dataOutputAssociation.getTarget().getId();
				if(outputDataObjectID.equals(dataObjectID)) {
					output.add(activity.getId());
				}
			}
		}
		return output;
	}
	
	
	
	/**
	 * The method getActivitiesRead returns all the activities that reads 
	 * the given data object
	 * @param modelInstance
	 * @param dataObjectID
	 * @return A list of activities
	 */
		public static List<String> getActivitiesRead(BpmnModelInstance modelInstance, String dataObjectID){
			List<String> output = new ArrayList<String>();

			//Retrieving all the activities in the BPMN file
			Collection<ModelElementInstance> activitySet = InformationSupportMethods.getElementsByType(modelInstance,
					Activity.class);
			
			//For every activity
			for(ModelElementInstance mei: activitySet) {
				Activity activity = (Activity) mei;
				
				Collection<DataInputAssociation> dataInputAssociations = activity.getDataInputAssociations();
			
				for(DataInputAssociation dataInputAssociation: dataInputAssociations) {
					Collection<ItemAwareElement> itemAwareElements = dataInputAssociation.getSources();
					for(ItemAwareElement itemAwareElement: itemAwareElements) {
						if(itemAwareElement.getId().equals(dataObjectID)) {
							output.add(activity.getId());
						}
					}
				}
				//Here we consider the data objects read through messages
				Collection<SequenceFlow> incomingSequenceFlows = activity.getIncoming();
				for(SequenceFlow sequenceFlow: incomingSequenceFlows) {
					if(sequenceFlow.getSource().getClass().equals(IntermediateCatchEventImpl.class)) {
						IntermediateCatchEvent catchEvent = (IntermediateCatchEvent) sequenceFlow.getSource();
						Collection<DataOutputAssociation> dataOutputAssociations = catchEvent.getDataOutputAssociations();
						for(DataOutputAssociation dataOutputAssociation: dataOutputAssociations) {
							if(dataOutputAssociation.getTarget().getId().equals(dataObjectID)) {
								output.add(activity.getId());
							}
						}
					}
				}
			}
			return output;
		}
		
		/**
		 * The method getExitingDataObject returns all the data objects that
		 * exit the participant pool through messages
		 * @param modelInstance
		 * @param participantName
		 * @return A list of data objects
		 */
		public static List<String> getExitingDataObject(BpmnModelInstance modelInstance, String participantName) {
			List<String> output = new ArrayList<String>();
			Collection<ModelElementInstance> throwEventSet = getElementsByType(modelInstance, IntermediateThrowEvent.class);
			Collection<ModelElementInstance> participantSet = getElementsByType(modelInstance, Participant.class);
			//Calculating the process associated with the input participant
			String process = null;
			for (ModelElementInstance p : participantSet) {
				if (p.getAttributeValue("name").equals(participantName)) {
					process = p.getAttributeValue("processRef");
				}
			}
			//For every message throw event
			for (ModelElementInstance throwEvent : throwEventSet) {
				//If the throw event is inside our process
				if (throwEvent.getParentElement().getAttributeValue("id").equals(process)) {
					//Calculating the data object entering the throw event
					IntermediateThrowEvent event = (IntermediateThrowEvent) throwEvent;
					Collection<DataInputAssociation> out = event.getDataInputAssociations();
					for(DataInputAssociation di: out) {
						for(ItemAwareElement iae :di.getSources()) {							
							output.add(iae.getId());
						}
					}
				}
			}
			return output;
		}
		
		/**
		 * The method getEnteringDataObject returns all the data objects that
		 * enter the participant pool through messages
		 * @param modelInstance
		 * @param participantName
		 * @return
		 */
		public static List<String> getEnteringDataObject(BpmnModelInstance modelInstance, String participantName) {
			List<String> output = new ArrayList<String>();
			Collection<ModelElementInstance> catchEventSet = getElementsByType(modelInstance, IntermediateCatchEvent.class);
			Collection<ModelElementInstance> participantSet = getElementsByType(modelInstance, Participant.class);
			//Calculating the process associated with the input participant
			String process = null;
			for (ModelElementInstance p : participantSet) {
				if (p.getAttributeValue("name").equals(participantName)) {
					process = p.getAttributeValue("processRef");
				}
			}
			//For every message catch event
			for (ModelElementInstance catchEvent : catchEventSet) {
				//If the catch event is inside our process
				if (catchEvent.getParentElement().getAttributeValue("id").equals(process)) {
					//Calculating the data object exiting the catch event
					IntermediateCatchEvent event = (IntermediateCatchEvent) catchEvent;
					Collection<DataOutputAssociation> out = event.getDataOutputAssociations();
					for(DataOutputAssociation di: out) {						
						output.add(di.getTarget().getId());
					}
				}
			}
			return output;
		}
		
		
		/**
		 * The method getPreviousActivities returns all the previous activities
		 * (according to the order of execution) of the input flow node
		 * @param node
		 * @return A list of the previous activities
		 */
		public static List<String> getPreviousActivities(FlowNode node){
			//Creating the output list that will contain all the previous activities
			List<String> output = new ArrayList<String>();
			
			Queue<FlowNode> queue = new LinkedList<FlowNode>();
			
			queue.add(node);
			while(!queue.isEmpty()) {
				FlowNode currentNode = queue.remove();
				Collection<SequenceFlow> sequenceFlowCollection = currentNode.getIncoming();
				for(SequenceFlow sequenceFlow: sequenceFlowCollection) {
					FlowNode sourceNode = sequenceFlow.getSource();
					//If the node has not yet been visited
					if(!queue.contains(sourceNode)) {
						if(sourceNode.getClass().equals(TaskImpl.class)) {
							output.add(sourceNode.getId());
						}
						queue.add(sourceNode);
					}
				}
			}
			
			return output;
		}
		
		/**
		 * The method getPreviousCatchEvents returns all the previous catch events
		 * (according to the order of execution) of the input flow node
		 * @param node
		 * @return A list of the previous catch events
		 */
		public static List<String> getPreviousCatchEvents(FlowNode node){
			//Creating the output list that will contain all the previous catch events
			List<String> output = new ArrayList<String>();
			
			Queue<FlowNode> queue = new LinkedList<FlowNode>();
			
			queue.add(node);
			while(!queue.isEmpty()) {
				FlowNode currentNode = queue.remove();
				Collection<SequenceFlow> sequenceFlowCollection = currentNode.getIncoming();
				for(SequenceFlow sequenceFlow: sequenceFlowCollection) {
					FlowNode sourceNode = sequenceFlow.getSource();
					//If the node has not yet been visited
					if(!queue.contains(sourceNode)) {
						if(sourceNode.getClass().equals(IntermediateCatchEventImpl.class)) {
							output.add(sourceNode.getId());
						}
						queue.add(sourceNode);
					}
				}
			}
			
			return output;
		}
		
		/**
		 * The method getDataObjectEnteringMessageFlow returns all the data objects
		 * entering a message flow
		 * @param modelInstance
		 * @param MessageFlowID
		 * @return A list of data objects
		 */
		public static HashSet<String> getDataObjectEnteringMessageFlow(BpmnModelInstance modelInstance, String MessageFlowID){
		
			HashSet<String> output = new HashSet<String>();
			
			//Retrieving the message flow element
			MessageFlow messageFlow = modelInstance.getModelElementById(MessageFlowID);
			
			//Calculating the input data objects
			InteractionNode sourceThrowEvent = messageFlow.getSource();
			Collection<DataInputAssociation> dataInputAssociationList = ((IntermediateThrowEvent) sourceThrowEvent).getDataInputAssociations();
			for(DataInputAssociation dataInputAssociation: dataInputAssociationList) {
				for(ItemAwareElement itemAwareElement :dataInputAssociation.getSources()) {
					output.add(itemAwareElement.getId());
				}
			}
			return output;
		}
		
		/**
		 * The method getDataObjectExitingMessageFlow returns all the data objects
		 * exiting a message flow
		 * @param modelInstance
		 * @param MessageFlowID
		 * @return A list of data objects
		 */
		public static HashSet<String> getDataObjectExitingMessageFlow(BpmnModelInstance modelInstance, String MessageFlowID){
		
			HashSet<String> output = new HashSet<String>();
			
			//Retrieving the message flow element
			MessageFlow messageFlow = modelInstance.getModelElementById(MessageFlowID);
			
			//Calculating the exiting data objects
			InteractionNode targetThrowEvent = messageFlow.getTarget();
			Collection<DataOutputAssociation> dataOutputAssociationList = ((IntermediateCatchEvent) targetThrowEvent).getDataOutputAssociations();			
			for(DataOutputAssociation dataOutputAssociation: dataOutputAssociationList) {
				output.add(dataOutputAssociation.getTarget().getId());
			}
			return output;
		}

		
}
