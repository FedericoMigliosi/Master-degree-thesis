# Security analysis of information management in business processes
This is the repository of Federico Migliosi's master's thesis

## 📚Abstract
Business processes are collections of activities that are performed in a defined sequence to achieve business goals. Companies rely on business processes to optimize their internal workflow and increase their efficiency. Problems arise when business processes do not handle information securely, such as when an information is read or modified by unauthorised actors. Currently there is no modelling language or appropriate methods to effectively analyze possible security issues. 

This thesis faces this problem by proposing a systematic approach by modelling information structure and using it to verify security requirements.In particular, this thesis proposes an extension of Business Process Model and Notation (BPMN), the most popular modelling language for specifying business processes, with semantic partition trees, used to represent information. Each node in the data structure represents a single piece of information relevant for the organizational domain, the nodes are linked together by branches with associated semantics. 

Specific security analyses are proposed to support experts in identifying possible flaws inside business processes. These analyses are implemented by algorithms that exploit the newly designed modelling language together with BPMN security extensions, allowing to understand how the information is manipulated and the satisfiability of certain security requirements. Empirical tests were carried out to observe the relationships between the analyses execution time and specific business process features. 

## 👨‍💻 Getting started
### Prerequisites
This software was developed and tested in `macOS Montgomery` with the Intel processor. The used Java and Eclipse verions are the following:
* `Java version "17.0.2"`
* `Eclipse version 2022-03 (4.23.0)`

Later more up-to-date versions should not cause any problems. Both Java and the Eclispse IDE can be installed using the following links:
* [Java JDK](https://adoptopenjdk.net/)
* [Eclipse IDE](https://www.eclipse.org/downloads/)

### Installation
1. Run Eclipse IDE
2. Click on `File` > `Import`
3. Chose `Git` > `Projects from Git (with smart import)` and click on `Next`
4. Click on `Clone URI` and click on `Next`
5. In the URI textbox insert the repository link (The `Host`, `Protocol` and `Repository path` boxes should be filled automatically) and click on `Next`
6. Select the master branch  and click `Next`
7. Then chose where to save the new repository in the local computer
8. Now you should be presented with the option of choosing two folders: select only the one with the `Maven` tag on the left
9. Click `Finish`
10. Right click on the new project in the `Package Explorer` and select `Maven` > `Update Project`
11. Click `Ok`


## :computer: Project Structure
In this project there are four packages, respectively:
* *InformationStructure*<br/>
This package contains methods and classes to explore the information structure and to relate information with data objects
* *InformationAnalyses*<br/>
This package contains methods that allow to understand how and by whom the information is read or written within the process and the path it follows.
* *SecurityAnalyses*<br/>
This package contains methods to test process security requirements.
* *TestExamples*<br/>
This package contains examples to analyse a hospital business process.

## 🧐 Usage
The `Example` folder, as the name suggests, is where all the example processes with their information structure are saved.
Inside the `TestExamples` package there is a class that can be used to run some example analysis onto the hospital process; this class is also listed below
```java
package it.polimi.deib.federicomigliosi.TestExamples;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import it.polimi.deib.federicomigliosi.InformationStructure.*;
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
```
