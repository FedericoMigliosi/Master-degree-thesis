package it.polimi.deib.federicomigliosi.SecurityAnalyses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.ItemAwareElement;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
//
/**
 * The class SecuritySupportMethods contains methods to
 * support the security analysis by parsing the BPMN diagram
 * and retrieving the security annotations.
 * @author Federico Migliosi
 *
 */
public class SecuritySupportMethods {
	
	/**
	 * The method hasSecurityAnnotationDataObject checks if the input
	 * data objects has a specific security annotation
	 * @param dataObjectID
	 * @param modelInstance
	 * @param securityAnnotation
	 * @return true/false
	 */
	public static Boolean hasSecurityAnnotationDataObject(String dataObjectID, BpmnModelInstance modelInstance, String securityAnnotation) { 

		ItemAwareElement BPMNelement = modelInstance.getModelElementById(dataObjectID);

		if(BPMNelement==null) {
			return false;
		}

		ExtensionElements extensionElements = BPMNelement.getExtensionElements();
		
		if(extensionElements==null) {
			return false;
		}

		Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
				.filterByType(CamundaProperties.class).singleResult().getCamundaProperties();
		for (CamundaProperty property : properties) {
			String name = property.getCamundaName();
			if (name.equals(securityAnnotation)) {
				return true;
			}
		}

		return false;
	}
	
	
	/**
	 * The method hasSecurityAnnotationMessageFlow checks if the input
	 * message flow has a specific security annotation
	 * @param messageFlowID
	 * @param modelInstance
	 * @param securityAnnotation
	 * @return true/false
	 */
	public static Boolean hasSecurityAnnotationMessageFlow(String messageFlowID, BpmnModelInstance modelInstance, String securityAnnotation) { 

			MessageFlow messageFlow = modelInstance.getModelElementById(messageFlowID);

			if(messageFlow==null) {
				return false;
			}

			ExtensionElements extensionElements = messageFlow.getExtensionElements();
			
			if(extensionElements==null) {
				return false;
			}

			Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
					.filterByType(CamundaProperties.class).singleResult().getCamundaProperties();
			for (CamundaProperty property : properties) {
				String name = property.getCamundaName();
				if (name.equals(securityAnnotation)) {
					return true;
				}
			}

			return false;
		}
	
	
	/**
	 * The method hasSecurityAnnotationSwimlane checks if the input
	 * swimlane (pool or lane) has a specific security annotation
	 * @param swimlaneID
	 * @param modelInstance
	 * @param securityAnnotation
	 * @return true/false
	 */
	public static Boolean hasSecurityAnnotationSwimlane(String swimlaneID, BpmnModelInstance modelInstance, String securityAnnotation) { 

		BaseElement BPMNelement = modelInstance.getModelElementById(swimlaneID);

		if(BPMNelement==null) {
			return false;
		}

		ExtensionElements extensionElements = BPMNelement.getExtensionElements();
		
		if(extensionElements==null) {
			return false;
		}

		Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
				.filterByType(CamundaProperties.class).singleResult().getCamundaProperties();
		for (CamundaProperty property : properties) {
			String name = property.getCamundaName();
			if (name.equals(securityAnnotation)) {
				return true;
			}
		}

		return false;
	}
	
	
		
	/**	
	 * The method getSecurityAnnotations returns all the security
	 * annotation of a BPMN element
	 * @param extensionElements
	 * @return The list of security annotations
	 */
	public static List<String> getSecurityAnnotations(ExtensionElements extensionElements){
			List<String> output = new ArrayList<String>();
			
			Collection<CamundaProperty> properties = extensionElements.getElementsQuery()
					.filterByType(CamundaProperties.class).singleResult().getCamundaProperties();
			for (CamundaProperty property : properties) {
				String name = property.getCamundaName();
				output.add(name);
			}
			
			return output;
		}

}
