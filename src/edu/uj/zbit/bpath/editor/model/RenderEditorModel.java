/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.util.Hashtable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class RenderEditorModel {
    private Hashtable editors;
    private Hashtable renderers_values;
    private Hashtable renderers_labels;    
    private AttribGroup attribGroup;
    
    public RenderEditorModel(AttribGroup ag){
        editors = new Hashtable();
        renderers_values = new Hashtable();
        renderers_labels = new Hashtable();
        attribGroup=ag;
    }

   public AttribGroup getAttribGroup(){
	   return attribGroup;
   }
    
   public void addEditorForRow(int row, TableCellEditor e ){
       editors.put(new Integer(row), e);
   }
   public void removeEditorForRow(int row){
       editors.remove(new Integer(row));
  }
   public TableCellEditor getEditor(int row) {
       return (TableCellEditor)editors.get(new Integer(row));
   }
   
  public void addRendererForRow(int row, int col, TableCellRenderer e ){
	  if (col==1) renderers_values.put(new Integer(row), e);
	  else renderers_labels.put(new Integer(row), e);
  }
  public void removeRendererForRow(int row, int col){
	  
	  if (col==1) renderers_values.remove(new Integer(row));
	  else renderers_labels.remove(new Integer(row));
 }
  public TableCellRenderer getRenderer(int row,int col) {
	  
      if (col==1){
    	  return (TableCellRenderer)renderers_values.get(new Integer(row));
      }
      else{ 
    	  return (TableCellRenderer)renderers_labels.get(new Integer(row));
      }
  }
   
}