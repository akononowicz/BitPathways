/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import edu.uj.zbit.bpath.editor.model.AttribGroup;

public class ListControlCell extends JPanel  
	implements TableCellRenderer, TableCellEditor, Serializable{
	JButton addButton=new JButton("Add");
	AttribGroup attribGroup;
	int row;
	

	
	 public ListControlCell(AttribGroup _attribGroup, int _row){
		 attribGroup=_attribGroup;
		 row=_row;
		 
			setLayout(new GridLayout(1,1));
			
			addButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					attribGroup.addRowToList(row);
				}
			});

		
			add(addButton);

	 }
	
	 public void addCellEditorListener(CellEditorListener l){
		 
	 }

	 public void cancelCellEditing(){
		 
	 }

	 public Object getCellEditorValue(){
		 return null;
	 }

	 public boolean isCellEditable(EventObject anEvent){
		 return true;
	 }

	 public void removeCellEditorListener(CellEditorListener l){
		 
	 }

	 public boolean shouldSelectCell(EventObject anEvent){
		 return true;
	 }

	 public boolean stopCellEditing(){
		 return true;
	 }
	
	
	public Component getTableCellRendererComponent(JTable table, Object value,
                  boolean isSelected, boolean hasFocus, int row, int column) {
		
		return this;
	}
	
	 public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		 return this;
	 }

}
