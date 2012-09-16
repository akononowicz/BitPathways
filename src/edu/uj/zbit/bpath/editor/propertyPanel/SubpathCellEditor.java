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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.dialogs.SubpathWizard;
import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.PathModel;

public class SubpathCellEditor extends AbstractListCellEditor implements TableCellEditor{

	JTableX tableX;
	String pathid="",pathtitle="";
	JTextField textField=new JTextField();
	Object value;
	
	
	public SubpathCellEditor(AttribGroup _attribGroup) {
		super(_attribGroup);
		attribGroup=_attribGroup;
		
		textField.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				 if (e.getModifiers()==MouseEvent.BUTTON1_MASK){
					 leftClick();
				 }else if (e.getModifiers()==MouseEvent.BUTTON3_MASK){
					 rightClick(e);
				 }
			 }
		});
		
	}


	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		value=_value;
		textField.setEditable(false);
		
		if (value.toString().trim().length()>0){
			textField.setText(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.CONNECTEDCHANGE"));
		}else{
			textField.setText(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.DISCONNECTEDCHANGE"));
		}
		
		return textField;
	}
	
	protected void leftClick(){
		
		BpathMainFrame parent=(BpathMainFrame)tableX.getParentFrame();
		
		SubpathWizard wizard=new SubpathWizard(parent);
		
		wizard.setVisible(true);
		
		if (wizard.isAccepted()){
			value=wizard.getPathid();
			parent.model.getSelectedVertices()[0].getAttributes().setSpecialValue("label", new Object[]{wizard.getPathtitle()});
			
		}
			
		fireEditingStopped();
	}
	
	public Object getCellEditorValue(){	
		return value;
	}
	
}
