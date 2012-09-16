/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/


package edu.uj.zbit.bpath.editor.dialogs.mapping;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;


/**
 * 
 * Editor for changing values of the source column.
 * For value modifications returns a combobox instance with all source attributes 
 * associated with the selected element type
 * and having the same type as the destination (target) attribute
 * 
 * @author Andrzej Kononowicz
 */

public class SrcElementEditor extends AbstractCellEditor implements
		TableCellEditor, ItemListener{

	JComboBox combo;
	ElementMapping map;
	String selected_value="";
	JTable table;
	
	public SrcElementEditor(ElementMapping _map){
		map=_map;
		combo=new JComboBox();
		combo.addItemListener(this);
	}
	
	@Override
	public Object getCellEditorValue() {
		return selected_value;
	}

	@Override
	public Component getTableCellEditorComponent(JTable _table, Object value,
			boolean isSelected, int row, int column) {
		
		table=_table;
		
		if(value!=null) selected_value=value.toString();
		else selected_value="";
		
		
		combo.removeAllItems();
		
		String[] attributes=map.getMatchingAttributes(table.getValueAt(row, 0).toString());

		for (int i = 0; i < attributes.length; i++) {
			combo.addItem(attributes[i]);	
		}
		combo.addItem("");
	
		combo.setSelectedItem(value);
		
		return combo;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getStateChange()==e.SELECTED){
			selected_value=combo.getSelectedItem().toString();
			((ElementMappingModel)(table.getModel())).fireTableDataChanged();
		}else{
			selected_value="";
			((ElementMappingModel)(table.getModel())).fireTableDataChanged();
		}
	}

}
