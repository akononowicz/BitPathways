/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;


public class ReadOnlyCellEditor extends DefaultCellEditor {

	static JTextField text=new JTextField();
	
	public ReadOnlyCellEditor() {
		super(text);
		
		text.setEditable(false);
		// TODO Auto-generated constructor stub
	}

}
