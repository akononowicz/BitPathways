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

public class CiteCellRenderer extends DefaultTableCellRenderer{

	public CiteCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		
		if (value instanceof Element){
			Element activityElement=(Element)value;
			Element nameElement=(Element)activityElement.getElementsByTagName("cite").item(0);
			String name=nameElement.getChildNodes().item(0).getNodeValue();
			super.setValue(name);		
		}else{
			super.setValue("");
		}
		
	}

}
