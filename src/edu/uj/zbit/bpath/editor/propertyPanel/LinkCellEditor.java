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


public class LinkCellEditor extends AbstractListCellEditor implements TableCellEditor{

	JTableX tableX;
	protected String dscr="";
	protected String href="";
	
	JTextField textField;
	Object value;
	
    private static final Logger log = Logger.getLogger(LinkCellEditor.class);
	
	
	public LinkCellEditor(AttribGroup _attribGroup) {
		super(_attribGroup);
		attribGroup=_attribGroup;
	}

	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		value=_value;
	
		if (value instanceof Element){	
			Element linkElement=(Element)value;
			Element hrefElement=(Element)linkElement.getElementsByTagName("href").item(0);
			Element dscrElement=(Element)linkElement.getElementsByTagName("dscr").item(0);

			dscr=dscrElement.getTextContent();
			href=hrefElement.getTextContent();
		}
		
		textField=new JTextField();
		textField.setEditable(false);		
		textField.setText(href);
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
		 LinkEditor linkEdit=new LinkEditor(tableX.getParentFrame(),dscr,href);
		 linkEdit.setVisible(true);
		 
		 if (linkEdit.isCompleted()){
			 href=linkEdit.getHref();
			 dscr=linkEdit.getDscr();
			 
			 textField.setText(href);
			 
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
			Element linkElement=doc.createElement("link");
			Element hrefElement=doc.createElement("href");
			hrefElement.appendChild(doc.createTextNode(href));
			Element dscrElement=doc.createElement("dscr");
			dscrElement.appendChild(doc.createTextNode(dscr));
		
			linkElement.appendChild(hrefElement);
			linkElement.appendChild(dscrElement);
			
			return linkElement;
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return "";
	}
	

}
