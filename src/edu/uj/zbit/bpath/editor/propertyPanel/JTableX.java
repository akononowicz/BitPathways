/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.*;

import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import org.apache.log4j.Logger;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.RenderEditorModel;


import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



 public class JTableX extends JTable {
	 private static final Logger log = Logger.getLogger(JTableX.class);
     protected RenderEditorModel rm;
     
     public JTableX(){
         super();
         rm = null;
        
     
         addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e) {

         		super.mouseClicked(e);
         		if (getSelectedColumn()==0){

         			Object renderer=rm.getRenderer(getSelectedRow(), 0);
         			if (renderer instanceof LabelListItemCellRenderer){

	             		LabelListItemCellRenderer dR=(LabelListItemCellRenderer)renderer;

	             		if (e.getPoint().x>=(-dR.getBounds().x-dR.getDelRectangle().width)){
	                 		AttribGroup attribGroupModel=(AttribGroup)getModel();
	                 		attribGroupModel.removeRowFromList(getSelectedRow());
	             		}		
         			}
         		}
         		
         	}
         });
            
     }

    
     public TableCellEditor getCellEditor(int row, int col){
    	 
    	 
         TableCellEditor tmpEditor = null;
         if (rm!=null)
             tmpEditor = rm.getEditor(row);
         if (tmpEditor!=null)
             return tmpEditor;
         return super.getCellEditor(row,col);
     }
     
     public TableCellRenderer getCellRenderer(int row, int col){
         TableCellRenderer tmpRenderer = null;
         
         if (rm!=null){
        	 tmpRenderer = rm.getRenderer(row,col);
         }
         if (tmpRenderer!=null){
        	 return tmpRenderer;
         }
         return super.getCellRenderer(row,col);
     }
     
     public void tableChanged(TableModelEvent e){
    	 
    	 Object model=getModel();
    	 
    	 if ((model!=null)&&
    		 (model instanceof AttribGroup)){
    		 AttribGroup attribGroupModel=(AttribGroup)model;
    		 attribGroupModel.pathwayModified();
    		 
    		 rm=attribGroupModel.getRenderEditorModel();
    	 }
    			 
    	 
    	 super.tableChanged(e);
     }
     
    protected void configureEnclosingScrollPane(){
    	Container p = getParent();
    	if (p instanceof JViewport){
    		Container gp=p.getParent(); 
    		if (gp instanceof JScrollPane){
    			JScrollPane scrollPane = (JScrollPane)gp;
    			JViewport viewport=scrollPane.getViewport();
    			if (viewport ==null || viewport.getView() != this){
    				return;
    			}
    			scrollPane.getViewport().setBackingStoreEnabled(true);
    			scrollPane.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
    		}
    	}
    }
    
    public JFrame getParentFrame(){
    	return _getParentFrame(this.getParent());
    }
    
    private JFrame _getParentFrame(Component c){
    	
    	if (c instanceof JFrame) return (JFrame)c;
    	else return _getParentFrame(c.getParent());
    }
    
    

 }