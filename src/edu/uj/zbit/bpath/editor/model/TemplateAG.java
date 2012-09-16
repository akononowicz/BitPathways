/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.util.Hashtable;

/**
 * Represents an attribute group in a template. Informs about properties of the group and forwards 
 * queries to attributes within this group 
 * 
 * @author Andrzej Kononowicz
 *
 */

public class TemplateAG {

	private String id;
	/** labels in different languages */
	private Hashtable<String, String> label=new Hashtable<String, String>();
	/** attribs from template assigned to this attrib group*/
	private Hashtable<String, TemplateA> a=new Hashtable<String, TemplateA>();
	/** shows in which order attributes should be displayed **/
	private TemplateA[] attrib_order; 
	
	public TemplateAG(String _id, Hashtable<String, String> _label, Hashtable<String, TemplateA> _a, TemplateA[] _attrib_order){
		id=_id;
		label=_label;
		a=_a;
		attrib_order=_attrib_order;
	}
	
	public String getId(){
		return id;
	}
	
	public String getLabel(String lang){
		return label.get(lang);
	}
	
	public String getLabel(String attribute_id, String lang){
		if (a.get(attribute_id)==null) return "[unknown]";
		return a.get(attribute_id).getLabel(lang);
	}
	
	public String getItemLabel(String attribute_id, String lang){
		if (a.get(attribute_id)==null) return "[unknown]";
		return a.get(attribute_id).getItemLabel(lang);
	}
	
	public TemplateA getAttrib(String attribute_id){
		if (a==null) return null;
		return a.get(attribute_id);
	}
	
	public  TemplateA[] getAllAttribs(){
		return attrib_order;
	}
	
	public String toString(){
		
		String retStr="";
		retStr+="[["+id+"]] "+label.get("pl")+"\n";
		
		for (int i = 0; i < attrib_order.length; i++) {
			retStr+="-> "+(i+1)+"."+attrib_order[i].toString()+"\n";
		}
		
		return retStr;
	}
	
	
}
