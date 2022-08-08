package it.polimi.deib.federicomigliosi.TestExamples;

import it.polimi.deib.federicomigliosi.InformationStructure.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import it.polimi.deib.federicomigliosi.InformationAnalyses.*;
import it.polimi.deib.federicomigliosi.SecurityAnalyses.*;

public class TestExamples {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		//Returns the information contained in the data object "Specialistic report" (the method takes as input the ID)
        XMLprocessing.fromDataObjectToInformationSingle("Examples/hospital.xml", "DataObjectReference_18m1ns9");
        
        //Returns the data objects where the input information is stored
        XMLprocessing.fromInformationToDataObjectSingle("Examples/hospital.xml", "patient_SSN");
        
        //Returns the activities reading the input information
        InformationAnalysis.getActivityThatReadInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "patient_SSN", true);
        
        //Returns the participants writing the input information
        InformationAnalysis.getParticipantThatWriteInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "pathology_exam_outcome", true);
    
        //Check the attack harm detection satisfiability of "patient_SSN" information
        SecurityAnalysis.attackHarmDetectionValidationInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "patient_SSN");
        
        //Check the integrity satisfiability of "patient_SSN" information
        SecurityAnalysis.integrityValidationInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "patient_SSN");

	}

}

