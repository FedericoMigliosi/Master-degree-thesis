# Security analysis of information management in business processes
This is the repository of Federico Migliosi's master's thesis

## 📚Introduction
If business processes do not handle information securely the consequence can be dire. There is currently no modelling language or appropriate methods to effectively analyze possible security issues of information used in processes. 
To solve the aforementioned problem we have identified some goals to be accomplished:

1)Create a modeling language to describe information and its use within processes;

2)Define algorithms to analyze security properties in business processes;

3)Implement the algorithms inside a software and test them with use cases.

## 👨‍💻 Getting started
### Prerequisites
The software was developed and tested in `macOS Montgomery` with the Intel processor. The used Java and Eclipse verions ar the following:
* `Java version "17.0.2"`
* `Eclipse version 2022-03 (4.23.0)`

Later more up-to-date versions shouldn't cause any problems. Both Java and the Eclispse IDE can be installed as follows:
* For Java install the JDK (I have used [this](https://adoptopenjdk.net/) link for the installation)
* For the Eclipse IDE I have use the [official dowloand page](https://www.eclipse.org/downloads/)

### Installation
1. Run Eclipse IDE
2. Click on `File` > `Import`
3. Chose `Git` > `Projects from Git` and click on `Next`
4. Click on `Clone URI` and click on `Next`
    1. In the URI textbox insert the cloning link of the repository
    2. The `Host`, `Protocol` and `Repository path` boxes should be filled automatically 
    3. In the `Authentication` section insert the personal GitHub username and password
    4. Click `Next`
5. Select the only branch present and click `Next`
6. In the current window chose where to save the new repository in the local computer
7. Now a new Tab called `Git repositories` shoud appear in the Eclipse dashboard
8. Select the newly downloaded project > right click and select `Show in` > `Project Explorer`
9. A new project should now appear in the project tab

## :computer: Project Structure
In this project there are three packages, respectively:
* *InformationStructure*<br/>
This package contains methods and classes to explore the information structure and to relate information with data objects
* *InformationAnalyses*<br/>
This package contains methods that allow us to understand how and by whom the information is read or written within the process and the path it follows.
* *SecurityAnalyses*<br/>
This package contains methods to test the security requirements of the process.


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
