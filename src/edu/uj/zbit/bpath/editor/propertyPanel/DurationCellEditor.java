/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;


public class DurationCellEditor extends AbstractCellEditor implements TableCellEditor{
	
	Object value="";
	JTableX tableX;
	String editorCaption;
	JTextField textField;
	
	
	public DurationCellEditor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		value=_value;
		
		textField=new JTextField();
		textField.setEditable(false);		
		textField.setText(value.toString());
		
		AttribGroup attribGroup=(AttribGroup)table.getModel();
		editorCaption=attribGroup.getValueAt(row,0).toString();
		
		textField.addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e){
				 if (e.getModifiers()==MouseEvent.BUTTON1_MASK){
					 leftClick();
				 }
			 }
		});
		
		return textField;
	}
	
	protected void leftClick(){
		 DurationEditor durationDialog=new DurationEditor(tableX.getParentFrame(),value.toString());
		 durationDialog.setVisible(true);
				 
		 if (durationDialog.isCompleted()){
			 String duration=durationDialog.getDuration();
			 textField.setText(duration);
			 value=duration;
			 fireEditingStopped();
		 }
	}
	
	
	public Object getCellEditorValue(){
		
		return value;
	}
	

}
