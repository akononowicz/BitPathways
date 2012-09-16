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


import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.model.AttribGroup;


public class CiteCellEditor extends AbstractListCellEditor implements TableCellEditor{

	JTableX tableX;
	protected String myabstract="";
	protected String cite="";
	
	JTextField textField;
	Object value;
	
    private static final Logger log = Logger.getLogger(CiteCellEditor.class);
	
	
	public CiteCellEditor(AttribGroup _attribGroup) {
		super(_attribGroup);
		attribGroup=_attribGroup;
	}

	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		value=_value;
	
		if (value instanceof Element){	
			Element citeElement=(Element)value;
			Element hrefElement=(Element)citeElement.getElementsByTagName("cite").item(0);
			Element dscrElement=(Element)citeElement.getElementsByTagName("abstract").item(0);

			if (hrefElement.getChildNodes().getLength()>0){
				cite=hrefElement.getChildNodes().item(0).getTextContent();
			}else{
				cite="";	
			}
			
			if (dscrElement.getChildNodes().getLength()>0){
				myabstract=dscrElement.getChildNodes().item(0).getTextContent();
			}else{
				myabstract="";	
			}			
			
		}
		
		textField=new JTextField();
		textField.setEditable(false);		
		textField.setText(cite);
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
		 CiteEditor linkEdit=new CiteEditor(tableX.getParentFrame(),myabstract,cite);
		 linkEdit.setVisible(true);
		 
		 if (linkEdit.isCompleted()){
			 cite=linkEdit.getCite();
			 myabstract=linkEdit.getAbstract();
			 
			 textField.setText(cite);
			 
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
			Element citationElement=doc.createElement("citation");
			Element citeElement=doc.createElement("cite");
			citeElement.appendChild(doc.createTextNode(cite));
			Element abstractElement=doc.createElement("abstract");
			abstractElement.appendChild(doc.createTextNode(myabstract));
		
			citationElement.appendChild(citeElement);
			citationElement.appendChild(abstractElement);
			
			return citationElement;
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return "";
	}
	

}
