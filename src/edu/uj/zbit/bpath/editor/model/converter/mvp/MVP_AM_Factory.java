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

import com.sun.org.apache.xml.internal.utils.NameSpace;

import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowSubpath;

public class MVP_AM_Factory {

	public final static String NAMESPACE="http://ns.medbiq.org/activitymodel/v1/";
	
	public static Document createActivityModel(FlowChartNode[] nodes,DataFlowEdge[] edges){

		Document document=null;
		
		// create empty document	
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 document = documentBuilder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root=document.createElementNS(NAMESPACE,"ActivityModel");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xsi:schemaLocation", "http://ns.medbiq.org/activitymodel/v1/ activitymodel.xsd");
		document.appendChild(root);
		
		Element ansElement=document.createElement("ActivityNodes");
		Element nsElement=document.createElement("NodeSection");
		
		nsElement.setAttribute("id", "NS_1");
		nsElement.setAttribute("label", "Virtual Patient");
		
		root.appendChild(ansElement);
		ansElement.appendChild(nsElement);
		
		Element linksElement=null;
		
		if (edges.length>0){
			linksElement=document.createElement("Links");
			root.appendChild(linksElement);
		}
		
		String label,id;
		
		for (FlowChartNode node : nodes) {
			
			if (MVP_VPD_Factory.isClassAccepted(node.getClass())){
				id=node.getId();
				label=node.getAttributes().getValue("card", "label")[0].toString();
				if (label.trim().length()<1) label="[No Name]";
				createAMNode(nsElement,id,label);
			}
		}
			
		// Edge Element
		for (DataFlowEdge edge: edges) {
			String text=edge.getAttributes().getValue("edge_general", "label")[0].toString();
			
			if (text.trim().length()==0) text="next";
			
			if (!(edge.getDestVertex() instanceof DataFlowSubpath))
			createLinkNode(linksElement,text,edge.getScrVertex().getId(),edge.getDestVertex().getId());
		}
		
		
		return document;
	}
	
	//-----------------------------------------------------------------------------------------------	
	
	private static void createAMNode(Element root,String id,String title){
		
		Element anElement=root.getOwnerDocument().createElement("ActivityNode");
		anElement.setAttribute("id", "AN_"+id);
		anElement.setAttribute("label", title);
		root.appendChild(anElement);
		
		Element contentElement=root.getOwnerDocument().createElement("Content");
		contentElement.setTextContent("/DataAvailabilityModel/DAMNode[@id = 'CARD_"+id+"']");
		anElement.appendChild(contentElement);
	}
	
	//-----------------------------------------------------------------------------------------------	
	
	private static void createLinkNode(Element root,String label,String idA, String idB){
		
		Element linkElement=root.getOwnerDocument().createElement("Link");
		linkElement.setAttribute("display", "true");
		linkElement.setAttribute("label", label);
		root.appendChild(linkElement);
		
		Element aElement=linkElement.getOwnerDocument().createElement("ActivityNodeA");
		aElement.setTextContent("/ActivityModel/ActivityNodes/NodeSection/ActivityNode[@id='AN_"+idA+"']");
		linkElement.appendChild(aElement);
		
		
		Element bElement=linkElement.getOwnerDocument().createElement("ActivityNodeB");
		bElement.setTextContent("/ActivityModel/ActivityNodes/NodeSection/ActivityNode[@id='AN_"+idB+"']");	
		linkElement.appendChild(bElement);
	}
	
}
