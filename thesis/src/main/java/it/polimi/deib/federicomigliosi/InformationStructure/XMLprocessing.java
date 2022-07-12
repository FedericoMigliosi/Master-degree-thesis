package it.polimi.deib.federicomigliosi.InformationStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
//

/**
 * The XMLprocessing class is used to analyze the .xml file containing 
 * the information structure defined by the user.
 *  
 * @author Federico Migliosi
 *
 */
public class XMLprocessing {
	
	
	/**
	 * The getSubTree method calculates all the nodes associated to the subtree
	 * whose root is the input node
	 * @param nodeIterator 
	 * @return A list of nodes
	 */
	public static List<Node> getSubTree(NodeIterator nodeIterator) {
		List<Node> subTree = new ArrayList<Node>();
		for (Node node = nodeIterator.nextNode(); node != null;
		        node = nodeIterator.nextNode()) {
			subTree.add(node);
		}
		return subTree;	
	}
	
	/**
	 * The method getInformationSubTree returns all the information in a subtree
	 * @param nodeIterator
	 * @return A list of information
	 */
	public static HashSet<String> getInformationSubTree(NodeIterator nodeIterator){
		HashSet<String> informationList = new HashSet<String>();
		List<Node> subTree = getSubTree(nodeIterator);
		for(Node node: subTree) {
			if(node.getNodeName().equals("information")) {
				String informationName = node.getAttributes().item(0).getTextContent();
				informationList.add(informationName);				
			}
		}
		return informationList;
	}
	
	/**
	 * The fromInformationToDataObjectMultiple method calculates all the data objects that
	 * contains a specific information
	 * (and all of its parts represented by the subtree)
	 * @param fileName
	 * @param information
	 * @return A list of data objects
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> fromInformationToDataObjectMultiple(String fileName, String information) throws ParserConfigurationException, SAXException, IOException{
		//Creation of the output list that will contain all the data objects		
        HashSet<String> dataObjectList = new HashSet<String>();
        
        
    	//Here we obtain a parser that produces DOM object trees from XML documents
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(fileName);

        /*
         * DocumentTraversal contains methods that create iterators 
         * to traverse a node and its children.
         */
        DocumentTraversal trav = (DocumentTraversal) document;
        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);
        
        //Now we traverse the XML structure 
        for (Node node = it.nextNode(); node != null;
                node = it.nextNode()) {
            if(node.getNodeName()=="information") {
            	String informationName = node.getAttributes().item(0).getTextContent();
            	//If the information node corresponds to the one we are looking for 
            	if(informationName.equals(information)) {
                    NodeIterator subTreeIterator = trav.createNodeIterator(node,
            		        NodeFilter.SHOW_ELEMENT, null, true);
                    
                    List<Node> subTreeNodeList=getSubTree(subTreeIterator);                                       
                    
                    //For each information in the subtree we extract the name of the containing data object  
                    for(Node n: subTreeNodeList) {
                    	if(n.getNodeName()=="dataObject") {
                    		dataObjectList.add(n.getAttributes().item(0).getTextContent());
                    	}
                    }
                    break;    
            	}
            }
        }      
        return dataObjectList;
	}
	
	
	
	/** 
	 * The fromInformationToDataObjectSingle method calculates all the data objects that
	 * contains a specific information
	 * (considering only the single node and not the subtree)
	 * @param fileName 
	 * @param information
	 * @return A list of data objects
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> fromInformationToDataObjectSingle(String fileName, String information) throws ParserConfigurationException, SAXException, IOException{
		//Creation of the output list that will contain all the data objects
		HashSet<String> dataObjectList = new HashSet<String>();
        
    	//Here we obtain a parser that produces DOM object trees from XML documents
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(fileName);

        /*
         * DocumentTraversal contains methods that create iterators 
         * to traverse a node and its children.
         */
        DocumentTraversal trav = (DocumentTraversal) document;
        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);
        
        
      //Now we traverse the XML structure 
        for (Node node = it.nextNode(); node != null;
                node = it.nextNode()) {
            if(node.getNodeName()=="information") {
            	String informationName = node.getAttributes().item(0).getTextContent();
            	//If the information node corresponds to the one we are looking for 
            	if(informationName.equals(information)) {
            		
            		Node dataObjectSet=node.getChildNodes().item(1);
            		
                    NodeIterator subTreeIterator = trav.createNodeIterator(dataObjectSet,
            		        NodeFilter.SHOW_ELEMENT, null, true);
                    
                    List<Node> nodiDelSottoalbero=getSubTree(subTreeIterator);                                       

                  //For each information in the subtree we extract the name of the containing data object
                    for(Node n: nodiDelSottoalbero) {
                    	if(n.getNodeName()=="dataObject") {
                    		dataObjectList.add(n.getAttributes().item(0).getTextContent());
                    	}
                    }
            		
                    break;    
            	}
            }
        }
 
        return dataObjectList;
	}

	
	/**
	 * The method fromDataObjectToInformationSingle calculates all the information
	 * contained in a single data object
	 * @param fileName
	 * @param dataObjectID
	 * @return A list of information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static List<String> fromDataObjectToInformationSingle(String fileName, String dataObjectID) throws ParserConfigurationException, SAXException, IOException{
		//Creation of the output list that will contain all the information
        List<String> informationList = new ArrayList<String>();
        
    	//Here we obtain a parser that produces DOM object trees from XML documents
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(fileName);

        /*
         * DocumentTraversal contains methods that create iterators 
         * to traverse a node and its children.
         */
        DocumentTraversal trav = (DocumentTraversal) document;
        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);
        
        
		//Now we traverse all the nodes in a depth-first approach
		for (Node node = it.nextNode(); node != null;
		        node = it.nextNode()) {	
			if(node.getNodeName()=="dataObject") {
				String DOID = node.getAttributes().item(0).getTextContent();		 
					if(DOID.equals(dataObjectID)) {
						Node informationParent=node.getParentNode().getParentNode();
						String informationParentName=informationParent.getAttributes().item(0).getTextContent();
						informationList.add(informationParentName);
					}
				
			}
		} 
        return informationList;
	}
	
	/**
	 * The method fromDataObjectToMultipleInformation returns all the information
	 * contained in a data object, considering not only the information directly
	 * connected to the data object, but also the subtree.
	 * @param fileName
	 * @param dataObjectID
	 * @return A list of Information
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static HashSet<String> fromDataObjectToInformationMultiple(String fileName, String dataObjectID) throws ParserConfigurationException, SAXException, IOException{
		//Creation of the output list that will contain all the information
		HashSet<String> informationList = new HashSet<String>();
        
    	//Here we obtain a parser that produces DOM object trees from XML documents
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(fileName);

        /*
         * DocumentTraversal contains methods that create iterators 
         * to traverse a node and its children.
         */
        DocumentTraversal trav = (DocumentTraversal) document;
        NodeIterator it = trav.createNodeIterator(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);
        
        
		//Now we traverse all the nodes in a depth-first approach
		for (Node node = it.nextNode(); node != null;
		        node = it.nextNode()) {	
			if(node.getNodeName()=="dataObject") {
				String DOID = node.getAttributes().item(0).getTextContent();		 
					if(DOID.equals(dataObjectID)) {
						Node informationParent=node.getParentNode().getParentNode();
						String informationParentName=informationParent.getAttributes().item(0).getTextContent();
						informationList.add(informationParentName);
						//Adding also the information contained in the subtree
						NodeIterator subTreeIterator = trav.createNodeIterator(informationParent,
	            		        NodeFilter.SHOW_ELEMENT, null, true);
						informationList.addAll(getInformationSubTree(subTreeIterator));
					}
				
			}
		} 
		return informationList;
	}
	
}
