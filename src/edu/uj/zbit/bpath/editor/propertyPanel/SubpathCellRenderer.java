/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.table.DefaultTableCellRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class SubpathCellRenderer extends DefaultTableCellRenderer{

	public SubpathCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		if (value.toString().trim().length()>0){
			super.setValue(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.CONNECTEDCHANGE"));
		}else{
			super.setValue(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.DISCONNECTEDCHANGE"));
		}
	}

}
