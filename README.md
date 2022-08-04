# Thesis
This is the repository of Federico Migliosi's master's thesis

### 📚Introduction
---
If business processes do not handle information securely the consequence can be dire. There is currently no modelling language or appropriate methods to effectively analyze possible security issues of information used in processes. 
To solve the aforementioned problem we have identified some goals to be accomplished:

1)Create a modeling language to describe information and its use within processes;

2)Define algorithms to analyze security properties in business processes;

3)Implement the algorithms inside a software and test them with use cases.

### 👨‍💻 Installation
---
In order to execute the code some libraries are required. They enables the parsing of a BPMN process saved in a .bpmn file.
I our project we use the Camunda workflow engine with the Eclipse IDE.

For a correct project set up you need to follow [this](https://docs.camunda.org/get-started/java-process-app/project-setup/) guide. There is shown how to generate an Eclipse Maven project and how to import the necessary dependencies.

### 🧐 Usage
---
In this project there are three packages, respectively:
* InformationStructure<br/>
&emsp;This package contains methods and classes to explore the information structure and to relate information with data objects
* InformationAnalyses<br/>
&emsp;This package contains methods that allow us to understand how and by whom the information is read or written within the process and the path it follows.
* SecurityAnalyses<br/>
&emsp;This package contains methods to test the security requirements of the process.

Now we present some examples of execution using the process saved in `hospital.bpmn` with its respective information `hospital.xml`.
```java
import it.polimi.deib.federicomigliosi.InformationStructure.*;
import it.polimi.deib.federicomigliosi.InformationAnalyses.*;
import it.polimi.deib.federicomigliosi.SecurityAnalyses.*;

public class TestArea {
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
        SecurityAnalysis.attackHarmDetectionValidationInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "patient_SSN")
        
        //Check the integrity satisfiability of "patient_SSN" information
        SecurityAnalysis.integrityValidationInformation("Examples/hospital.xml", "Examples/hospital.bpmn", "patient_SSN")
    }
}
```
