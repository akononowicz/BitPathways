/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.util.Hashtable;


/**
 * 
 * Represent and attribute from a template. Is part of one or many attribute groups. 
 * Has a type e.g. html, string can link to a dictionary (type=dict, dict_type=?) or comprise of
 * many simple attributes forming an attribute list (type=list, item_type=?)  
 * 
 * @author Andrzej Kononowicz
 *
 */

public class TemplateA {

	private String id;
	
	/** labels in different languages */
	private Hashtable<String, String> label=new Hashtable<String, String>();
	
	/** item labels in different languages */
	private Hashtable<String, String> ilabel=new Hashtable<String, String>();
	
	private String type;
	private String itemType;
	private String dictType;
	
	public TemplateA(String _id, Hashtable<String, String> _label, Hashtable<String, String> _ilabel, String _type, String _itemType, String _dictType){
		id=_id;
		label=_label;
		ilabel=_ilabel;
		type=_type;
		itemType=_itemType;
		dictType=_dictType;
	}
	
	public String getLabel(String lang){
		return label.get(lang);
	}

	public String getItemLabel(String lang){
		return ilabel.get(lang);
	}
	
	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getItemType() {
		return itemType;
	}

	public String getDictType() {
		return dictType;
	}

	public String toString(){
		String retStr="";
		
		retStr+="["+id+"] "+label.get("pl");
		
		if (itemType.trim().length()>0) retStr+=" item type="+itemType;
		if (dictType.trim().length()>0) retStr+=" dict type="+dictType;
		
		return retStr;
	}
	
	
}
