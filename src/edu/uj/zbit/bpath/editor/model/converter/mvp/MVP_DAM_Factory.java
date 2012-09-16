/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter.mvp;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.editor.propertyPanel.TestQuestion;

public class MVP_DAM_Factory {
	
	public final static String NAMESPACE="http://ns.medbiq.org/dataavailabilitymodel/v1/";
	
//-----------------------------------------------------------------------------------------------
	
	public static Document createDataAvaiablityModel(FlowChartNode[] nodes){
		
		Document document=null;
		
		// create empty document	
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 document = documentBuilder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root=document.createElementNS(NAMESPACE,"DataAvailabilityModel");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:qti", "http://www.imsglobal.org/xsd/imsqti_v2p1");
		root.setAttribute("xsi:schemaLocation", "http://ns.medbiq.org/dataavailabilitymodel/v1/ dataavailabilitymodel.xsd");
		document.appendChild(root);
		
		String label,id;
		
		for (int i=0;i<nodes.length;i++) {
			
			FlowChartNode node=nodes[i];
			
			if (MVP_VPD_Factory.isClassAccepted(node.getClass())){
				id=node.getId();
				label=node.getAttributes().getValue("card", "label")[0].toString();
				if (label.trim().length()<1) label="[No Name]";
				Object[] mcqs=node.getAttributes().getSpecialValue("mcq_questions");
				String expert=node.getAttributes().getSingleValueAsString("card", "author_comment");
				createCardDAMNode(root,id,label,mcqs,expert);
				if (mcqs.length>0) createAnswerCommentDAMNode(root,id,label,mcqs);
			}
		}
		
		return document;
	}
	
//-----------------------------------------------------------------------------------------------	
	
	private static void createCardDAMNode(Element root,String id,String label,Object[] mcqs, String expert){
		
		Element damElement=root.getOwnerDocument().createElement("DAMNode");
		damElement.setAttribute("id", "CARD_"+id);
		root.appendChild(damElement);
		
		Element labelElement=root.getOwnerDocument().createElement("DAMNodeLabel");
		labelElement.setTextContent(label);
		damElement.appendChild(labelElement);

		// create question INFO reference
		
		Element itemElement=root.getOwnerDocument().createElement("DAMNodeItem");
		itemElement.setAttribute("display", "immediately");
		damElement.appendChild(itemElement);
		
		Element itemPathElement=root.getOwnerDocument().createElement("ItemPath");
		itemPathElement.setTextContent("/VirtualPatientData/VPDText[@id='INFO_"+id+"']");
		itemElement.appendChild(itemPathElement);
		
		Element itemOrderElement=root.getOwnerDocument().createElement("ItemOrder");
		itemOrderElement.setTextContent("0");
		itemElement.appendChild(itemOrderElement);
		
		// add questions 
		
		for (int i = 0; i < mcqs.length; i++) {
			if (mcqs[i] instanceof Element){
				
				// create question QEST reference					
				Element di2Element=root.getOwnerDocument().createElement("DAMNodeItem");
				di2Element.setAttribute("display", "immediately");

				Element ip2Element=root.getOwnerDocument().createElement("ItemPath");
				ip2Element.setTextContent("/VirtualPatientData/VPDText[@id='QEST_"+id+"_"+i+"']");
				di2Element.appendChild(ip2Element);
				
				Element io2Element=root.getOwnerDocument().createElement("ItemOrder");
				io2Element.setTextContent("1");
				di2Element.appendChild(io2Element);
				
				damElement.appendChild(di2Element);
				
				// create question QTI+ANSWERCOMMENT reference
				Element di3Element=root.getOwnerDocument().createElement("DAMNodeItem");
				di3Element.setAttribute("display", "immediately");

				Element ip3Element=root.getOwnerDocument().createElement("ItemPath");
				ip3Element.setTextContent("/VirtualPatientData/XtensibleInfo/qti:assessmentItem[@identifier='QTI_"+id+"_"+i+"']");
				di3Element.appendChild(ip3Element);
				
				Element ic3Element=root.getOwnerDocument().createElement("ItemComment");
				ic3Element.setTextContent("/DataAvailabilityModel/DAMNode[@id='ANSWERCOMMENT_"+id+"_"+i+"']");
				di3Element.appendChild(ic3Element);
				
				Element io3Element=root.getOwnerDocument().createElement("ItemOrder");
				io3Element.setTextContent("2");
				di3Element.appendChild(io3Element);
				
				damElement.appendChild(di3Element);
					
			}
		}	
		
		// add Expert comment
		
		
		if((expert!=null)&&(expert.length()>0)){
			Element itemEXPElement=root.getOwnerDocument().createElement("DAMNodeItem");
			itemEXPElement.setAttribute("display", "immediately");
			damElement.appendChild(itemEXPElement);
			
			Element itemPathEXPElement=root.getOwnerDocument().createElement("ItemPath");
			itemPathEXPElement.setTextContent("/VirtualPatientData/VPDText[@id='EXPT_"+id+"']");
			itemEXPElement.appendChild(itemPathEXPElement);
			
			Element itemOrderEXPElement=root.getOwnerDocument().createElement("ItemOrder");
			if (mcqs[0] instanceof Element) itemOrderEXPElement.setTextContent("3"); 
			else itemOrderEXPElement.setTextContent("1");
				
			itemEXPElement.appendChild(itemOrderEXPElement);
		}
		
	}

	private static void createAnswerCommentDAMNode(Element root,String id,String label,Object[] mcqs){
		
		for (int i = 0; i < mcqs.length; i++) {
			if (mcqs[i] instanceof Element){
		
				Element damElement=root.getOwnerDocument().createElement("DAMNode");
				damElement.setAttribute("id", "ANSWERCOMMENT_"+id+"_"+i);
				root.appendChild(damElement);
				
				Element labelElement=root.getOwnerDocument().createElement("DAMNodeLabel");
				labelElement.setTextContent("ANSWERCOMMENT");
				damElement.appendChild(labelElement);
		
				// create question ANSWERCOMMENT reference
				
				Element itemElement=root.getOwnerDocument().createElement("DAMNodeItem");
				itemElement.setAttribute("display", "ontrigger");
				damElement.appendChild(itemElement);
				
				Element itemPathElement=root.getOwnerDocument().createElement("ItemPath");
				itemPathElement.setTextContent("/VirtualPatientData/VPDText[@id='ANSWERCOMMENT_"+id+"_"+i+"']");
				itemElement.appendChild(itemPathElement);
				
				Element itemOrderElement=root.getOwnerDocument().createElement("ItemOrder");
				itemOrderElement.setTextContent("0");
				itemElement.appendChild(itemOrderElement);
			}
		}
	}
	
}