/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.Color;

import javax.swing.table.DefaultTableCellRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class ReadOnlyCellRenderer extends DefaultTableCellRenderer{

	public ReadOnlyCellRenderer() {
		super();
		this.setBackground(new Color(230,230,230));
	}
	
	protected void setValue(Object value){
		super.setValue((value));		
	}

}
