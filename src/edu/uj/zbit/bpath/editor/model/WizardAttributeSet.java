/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.util.Enumeration;
import java.util.Hashtable;



/**
 * Contains a set of attribute identifier and attribute value pairs
 * Is used by AddElementWizards to collect the initial parameters
 * 
 * @author Andrzej Kononowicz
 */

public class WizardAttributeSet {
	
	Hashtable<String, Object[]> hash=new Hashtable<String, Object[]> ();

	public void addAttribute(String attrib_id,Object[] attrib_value){
		hash.put(attrib_id, attrib_value);
	}
	
	public Object[][] getAttributes(){
		Object[][] ret=new Object[hash.size()][2];
		
		Enumeration<String> keys=hash.keys();
		
		int i=0;
		
		while(keys.hasMoreElements()){
			ret[i][0]=keys.nextElement();
			ret[i][1]=hash.get(ret[i][0]);
			i++;
		}
		
		return ret;
	}
}
