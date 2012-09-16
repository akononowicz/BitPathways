/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.table.DefaultTableCellRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class PersonCellRenderer extends DefaultTableCellRenderer{

	public PersonCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		
		if (value instanceof Element){
			Element personElement=(Element)value;
			Element firstnameElement=(Element)personElement.getElementsByTagName("firstname").item(0);
			Element famnameElement=(Element)personElement.getElementsByTagName("famname").item(0);
			String firstname=XMLProcessor.getText(firstnameElement.getChildNodes());
			String famname=XMLProcessor.getText(famnameElement.getChildNodes());
			
			super.setValue(firstname+" "+famname);		
		}else{
			super.setValue("");
		}
		
	}

}
