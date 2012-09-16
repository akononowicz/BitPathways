/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.security.action.GetLongAction;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.AttribGroup;

/**
 * Used to represent the entire path (which is equivalent to the 
 * situation in which no element is selected)
 * 
 * @author Andrzej Kononowicz
 *
 */

public class DataFlowPath implements FlowChartElement {
	
	protected String id;
	protected String bithpathwayid=null;
	private AttribGroupSet attributes;
	
	public String getType(){return "PATH";}
	
	public DataFlowPath(String id){
		 this.id=id;
	}	
	
	public String getTypeName(){
		  return BpathMainFrame.messages.getString("ELEMENT.PATH");
	}

	public String getId(){
		return id;
	}

	public void setId(String _id){
		id=_id;
	}
	
	public Element getElementAsDOM(Document doc,int[] order){
		 return getElementAsDOM(doc);
	}
	
	public Element getElementAsDOM(Document doc){
	
		Element pathNode=doc.createElement("header");
		pathNode.appendChild(attributes.toDOM(doc));
		 
		return pathNode;
	}
	
	   public Element getElementAsDOM(){
		   try{
			   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			   Document doc = factory.newDocumentBuilder().newDocument();
			   return getElementAsDOM(doc);
		   }catch(Exception e){
			   e.printStackTrace();
		   }
		   return (Element)null;
	   }
	
	
	  public AttribGroupSet getAttributes(){
		  return attributes;
	  }
	  
	  public void setAttributes(AttribGroupSet attributes){
		  this.attributes=attributes;
	  }


	public String getBithpathwayId() {
		return bithpathwayid;
	}


	public void setBithpathwayId(String mippid) {
		this.bithpathwayid = mippid;
	}

}
