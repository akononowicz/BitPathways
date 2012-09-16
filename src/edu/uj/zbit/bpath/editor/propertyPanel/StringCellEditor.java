/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.w3c.dom.Document;


public class StringCellEditor extends DefaultCellEditor {

	static JTextField text=new JTextField();
	Document doc;
	
	public StringCellEditor(Document _doc) {
		super(text);	
		doc=_doc;
	}
	
	public Object getCellEditorValue(){
		
		return text.getText().
		replaceAll("<", "&lt;").
		replaceAll(">", "&gt;");
	}

}
