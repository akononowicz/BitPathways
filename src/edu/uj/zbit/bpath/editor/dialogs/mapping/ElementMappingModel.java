/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs.mapping;
import javax.swing.table.AbstractTableModel;


/**
 * 
 * Data model for the table displayed in the TemplateMappingDialog window.
 * 
 * @author Andrzej Kononowicz
 */

public class ElementMappingModel extends AbstractTableModel {
	
	private ElementMapping map; 
	
	private String srcTempName; 
	private String destTempName;
	
	public ElementMappingModel(String _srcTempName, String _destTempName){
		srcTempName=_srcTempName;
		destTempName=_destTempName;
	}
	
	public void setElementMapping(ElementMapping _map){
		map=_map;
		
		fireTableDataChanged();
		
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		if (map==null) return 0;
		else return map.destTab.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (map==null) return null;
		
		if(col==0){
			return map.destTab[row];
		}else return map.srcTab[row];
	}
	
	@Override
	public String getColumnName(int column){
		if (column==0) return "Szablon docelowy: Wirtualny pacjent"; 
		else return "Szablon Ÿród³owy: Edukacyjna œcie¿ka kliniczna";
	}

	 public void setValueAt(Object value, int row, int col) {
		 
		 if(col==0) return;
		 else{
			 map.srcTab[row]=value.toString();
			 fireTableCellUpdated(row, col);
		 }
	 }
	 
	  public boolean isCellEditable(int row, int col) {
          if (col==0) return false; else return true;
          
      }
	
}
