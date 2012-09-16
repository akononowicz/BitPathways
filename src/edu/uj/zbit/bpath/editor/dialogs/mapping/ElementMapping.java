/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs.mapping;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * Stores data of the mapping for a given element class. Contains data in following form:
 * two String[]: 
 *
 * destTab - with all target attribute names for the given element type read from the template file
 * srcTab - names of those source attributes which are assigned to target attributes in a cell with the 
 * respective index (displayed in the same row in the GUI table)    
 *
 * two hashtables (one for src and one for dest) containing as values types of the attributes stored as keys   
 * 
 * @author Andrzej Kononowicz
 */

public class ElementMapping {

	private TemplateProcessor srcTemplate,destTemplate;
	
	private String elementName;

	/** all target attribute names for the given element type read from the template file*/
	public String[] srcTab;
	/** names of those source attributes which are assigned to target attributes 
	in a cell with the respective index (displayed in the same row in the GUI table)*/    
	public String[] destTab;

	//two hashtables (one for src and one for dest) containing as values types of the attributes stored as keys in the same table 
	private Hashtable<String,String> srcTypes= new Hashtable<String,String>();
	private Hashtable<String,String> destTypes= new Hashtable<String,String>();
		
	public ElementMapping(TemplateProcessor src, TemplateProcessor dest, String element){
		
		srcTemplate=src;
		destTemplate=dest;
		elementName=element;
		
		String[] attributeTabDest=destTemplate.getAttributeList4Element(elementName);
		destTab=new String[attributeTabDest.length];
		srcTab=new String[attributeTabDest.length];
		
		for (int i = 0; i < attributeTabDest.length; i++) {
			String[] fan=attributeTabDest[i].split(":");
			destTab[i]=fan[0];
			destTypes.put(fan[0], fan[1]);
		}
		
		String[] attributeTabSrc=srcTemplate.getAttributeList4Element(elementName);
		for (int i = 0; i < attributeTabSrc.length; i++) {
			String[] fan=attributeTabSrc[i].split(":");
			srcTypes.put(fan[0], fan[1]);
		}

	}

	public void setSrcValue4Dest(String dest_attib, String src_attrib){
		
		for (int i = 0; i < destTab.length; i++) {
			if (destTab[i].equalsIgnoreCase(dest_attib)){
				srcTab[i]=src_attrib;
				return;
			}
		}
		
	}
	
	public String getElementName() {
		return elementName;
	}
	
	/**
	 * returns a table with all the src attributes with the same type as the type of the dest attribute
	 * in the parameter  
	 * 
	 * @param attributeName dest attribute for which a list of suitable src attributes is sought
	 * @return
	 */
	public String[] getMatchingAttributes(String attributeName){
		
		String attributeType=destTypes.get(attributeName);
		
		ArrayList<String> matchingAttributesNames=new ArrayList<String>();
		
		Enumeration<String> srcKeys=srcTypes.keys();
		
		while (srcKeys.hasMoreElements()) {
			String key = (String) srcKeys.nextElement();
			if(srcTypes.get(key).equalsIgnoreCase(attributeType)){
				matchingAttributesNames.add(key);
			}
		}
		
		String[] ret=new String[matchingAttributesNames.size()];
		
		matchingAttributesNames.toArray(ret);
		
		return ret;
	}
	
}
