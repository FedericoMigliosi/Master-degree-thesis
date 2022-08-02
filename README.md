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
    This package
* InformationAnalyses
* SecurityAnalyses
```java
System.out.println("ciao");
```
