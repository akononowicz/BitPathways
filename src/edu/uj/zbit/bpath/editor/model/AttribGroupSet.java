/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/
package edu.uj.zbit.bpath.editor.model;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * Contains all groups of attributes of one template. 
 * Current version support just one template per pathways but can be easy extended.
 * AttribGroupSets are constructed by templates. A node in the model has 
 * (currently one, later more) attribute group sets attached.  
 * 
 * @author Andrzej A. Kononowicz
 *
 */

public class AttribGroupSet {

	private static final Logger log = Logger.getLogger(PathModel.class);
	
	private String elementId; // id of the vertex or edge to which this ag is attached
	private Hashtable<String, AttribGroup> agHash;
	private String templateId;
	private String[] agOrder;
	
	AttribGroupSet (String _elementId, String _templateId,Hashtable<String, AttribGroup> _ag, String[] _agOrder){
		agHash=_ag;
		templateId=_templateId;
		agOrder=_agOrder;
	}
	
	/* returns the first occurrence of this attrib type (e.g. label or color)
	 * returns null if no occurrence of this attribute present
	 */
	
	public Object[] getSpecialValue(String a){
		for (int i = 0; i < agOrder.length; i++) {
			Object[] o=agHash.get(agOrder[i]).getAttrib(a);
			if (o!=null) return o;
		}	
		return null;
	}
	
	/**
	 *	 pastes the same value (in v) to all attrib_id attributes in all groups
	 */
	
	public void setSpecialValue(String attrib_id,Object[] v){
		
		for (int i = 0; i < agOrder.length; i++) {
			AttribGroup ag=agHash.get(agOrder[i]);			
			if (ag.existAttrib(attrib_id)) ag.setAttrib(attrib_id,v);
		}
		
	}
	
	public int getSize(){
		return agHash.size();
	}

	public String getSingleValueAsString(String ag, String a){
		Object[] value=null;
		
		try{value=getValue(ag,a);}catch(Exception e){return "";};
		
		if ((value==null)||(value.length==0)||(value[0]==null)) return "";
		else return value[0].toString();
	}
	
	public Object[] getValue(String ag, String a){
		return agHash.get(ag).getAttrib(a);
	}

	public void setValue(String ag, String a, Object[] value){	
		if (agHash.containsKey(ag)) agHash.get(ag).setAttrib(a,value);
		else System.out.println("Attribute group '"+ag+"' not found in this template.");
	}
	
	public Element toDOM(Document doc){
		Element ags=doc.createElement("attribute_groups");

		for (int i = 0; i < agOrder.length; i++) {
			Element ag=doc.createElement("attribute_group");
			
			ag.setAttribute("template", templateId);
			ag.setAttribute("type_id", agOrder[i]);			                              
			
			Element[] attribs=agHash.get(agOrder[i]).toDOM(doc);
			
			for (Element attrib : attribs) {
				ag.appendChild(attrib);
			}
			
			ags.appendChild(ag);
		}
		
		
		return ags;
	}
	
	public AttribGroup get(int i){
		return agHash.get(agOrder[i]);
	}
	
}
