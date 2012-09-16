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
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class HTMLcodeCellEditor extends AbstractCellEditor implements TableCellEditor{

	String html="HTML";
	JTableX tableX;
	String editorCaption;
	JTextField textField;
	
	
	public HTMLcodeCellEditor() {
		super();
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		html=value.toString();
		
		textField=new JTextField();
		textField.setEditable(false);		
		textField.setText(XMLProcessor.html2textlabel(html));
		
		AttribGroup attribGroup=(AttribGroup)table.getModel();
		editorCaption=attribGroup.getValueAt(row,0).toString();
		
		
		textField.addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e){
				 HTMLcodeEditor htmledit=new HTMLcodeEditor(tableX.getParentFrame(),editorCaption,html);
				 htmledit.show(); 
				 html=htmledit.getContent();
				 textField.setText(XMLProcessor.html2textlabel(html));
				 fireEditingStopped();
			 }
		});
		return textField;
	}
	
	public Object getCellEditorValue(){
		
		return html;
	}
	

}
