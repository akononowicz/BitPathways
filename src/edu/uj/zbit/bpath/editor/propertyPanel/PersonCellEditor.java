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


public class PersonCellEditor extends AbstractListCellEditor implements TableCellEditor{

	JTableX tableX;
	String firstname="",famname="";	
	JTextField textField;
	Object value;
	
	
	public PersonCellEditor(AttribGroup _attribGroup) {
		super(_attribGroup);
		attribGroup=_attribGroup;
	}

	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		value=_value;
	
		if (value instanceof Element){	
			Element personElement=(Element)value;
			Element firstnameElement=(Element)personElement.getElementsByTagName("firstname").item(0);
			Element famnameElement=(Element)personElement.getElementsByTagName("famname").item(0);
			
			firstname=firstnameElement.getChildNodes().item(0).getNodeValue();
			famname=famnameElement.getChildNodes().item(0).getNodeValue();
		}
		
		textField=new JTextField();
		textField.setEditable(false);		
			
		if (famname.length()>0) textField.setText(firstname+" "+famname);
	
		textField.addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e){
				 if (e.getModifiers()==MouseEvent.BUTTON1_MASK){
					 leftClick();
				 }else if (e.getModifiers()==MouseEvent.BUTTON3_MASK){
					 rightClick(e);
				 }
			 }
		});
		return textField;
	}
	
	protected void leftClick(){
		 PersonEditor personEdit=new PersonEditor(tableX.getParentFrame(),firstname, famname);
		 personEdit.show(); 
		 
		 if (personEdit.isCompleted()){
			 firstname=personEdit.getFirstnameField();
			 famname=personEdit.getFamnameField();
			 
			 textField.setText(firstname+" "+famname);
			 
			 fireEditingStopped();
		 }
	}
	
	public Object getCellEditorValue(){
		
		Document doc;
		
		try{
			if (value instanceof Element){
				doc=((Element)value).getOwnerDocument();
			}else{
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				doc=factory.newDocumentBuilder().newDocument();
			}
			Element personElement=doc.createElement("person");
			Element nameElement=doc.createElement("firstname");
			nameElement.appendChild(doc.createTextNode(firstname));
			Element depElement=doc.createElement("famname");
			depElement.appendChild(doc.createTextNode(famname));
			
			personElement.appendChild(nameElement);
			personElement.appendChild(depElement);
			
			return personElement;
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return "";
	}
	

}
