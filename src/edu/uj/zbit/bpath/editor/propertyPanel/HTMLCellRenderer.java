/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.table.DefaultTableCellRenderer;

import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class HTMLCellRenderer extends DefaultTableCellRenderer{

	public HTMLCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		String html=XMLProcessor.html2textlabel(value.toString());
		super.setValue(html);
	}

}
