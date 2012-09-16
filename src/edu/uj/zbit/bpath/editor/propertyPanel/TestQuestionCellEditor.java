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


public class TestQuestionCellEditor extends AbstractListCellEditor implements TableCellEditor{

	JTableX tableX;
	JTextField textField;
	Object value;
	TestQuestion question;


	public TestQuestionCellEditor(AttribGroup _attribGroup) {
		super(_attribGroup);
		attribGroup=_attribGroup;
	}

	public Component getTableCellEditorComponent(JTable table, Object _value, boolean isSelected, int row, int column){
		tableX=(JTableX)table;
		textField=new JTextField();
		value=_value;
	
		if (value instanceof Element){	
			Element quesitonElement=(Element)value;
			question=new TestQuestion(quesitonElement);
		}
			
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
		 TestQuestionEditor testQuestionEdit=new TestQuestionEditor(tableX.getParentFrame(),question);
		 testQuestionEdit.setVisible(true); 
		 
		 if (testQuestionEdit.isCompleted()){
			 question=testQuestionEdit.getQuestion();
			 
			 textField.setText(question.getName());
		 }
		 
		 fireEditingStopped();
	}
	
	public Object getCellEditorValue(){

		Element domQuestion=null;
		
		if (question!=null) return question.getAsDOM();
		return domQuestion;
	}

}
