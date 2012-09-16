/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;

/** 
 * Table containing list of all components from a given staff category
 * 
 * @author Andrzej A. Kononowicz
 */

public class ComponentListTable extends JTable implements ListSelectionListener{

	String cat;
	PathModel model;
	ArrayList values;
	ComponentListPanel myparent;
	JPopupMenu pop=new JPopupMenu();
	
	public ComponentListTable(ComponentListPanel _myparent,PathModel _model,String _cat){
		cat=_cat;
		myparent=_myparent;
		values=new ArrayList();
		model=_model;
		
		// add 
		
		JMenuItem deleteMenu=new JMenuItem(BpathMainFrame.messages.getString("MAIN.PANELS.COMPONENTLIST.DELETESELECTION"));
		deleteMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int[] selectedRows=getSelectedRows();
				ArrayList todelete=new ArrayList();
				
				for (int i = 0; i < selectedRows.length; i++) {
					todelete.add(values.get(selectedRows[i]));
				}
				
				for (int i = 0; i < todelete.size(); i++) {
					DataFlowComponent comp=(DataFlowComponent)todelete.get(i);
					values.remove(comp);
					model.removeVertex(comp.getId());	
				}
			}
		});
		
		pop.add(deleteMenu);
		
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// add context menu
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if (e.getButton()==MouseEvent.BUTTON1){
					if (myparent!=null) myparent.deselectOthers(cat);
				}
			}
			public void mouseReleased(MouseEvent evt){
				if (evt.isPopupTrigger()){
					pop.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
			
		});
		
	}
	
	public void updateData(){
		values.clear();
		FlowChartNode[] vertices=model.getAllVertices();
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] instanceof DataFlowComponent) {
				DataFlowComponent element = (DataFlowComponent) vertices[i];
				if (!cat.equalsIgnoreCase("Übrige")){
					if (element.getCategory().equalsIgnoreCase(cat)){
						values.add(element);
					}
				}else{
					if ((!element.getCategory().equalsIgnoreCase("Arztdienst"))&&
						(!element.getCategory().equalsIgnoreCase("Behandlungsdienste"))&&
						(!element.getCategory().equalsIgnoreCase("Pflegedienst"))){
						values.add(element);
					}
				}
			}
		}
		ComponentListModel listModel=new ComponentListModel();
		setModel(listModel);
		
		this.repaint();
		this.revalidate();
	}
	
	public void addSelection(DataFlowComponent component){
		ListSelectionModel sm=this.getSelectionModel();
		for (int i = 0; i < values.size(); i++) {
			if (component==values.get(i)){
				sm.addSelectionInterval(i, i);
				break;
			}
		}		
	}

		
	public void valueChanged(ListSelectionEvent e){
		if (e.getValueIsAdjusting()) return;
		ListSelectionModel selModel=(ListSelectionModel)e.getSource();
		if (!selModel.isSelectionEmpty()){
			int selIndex=selModel.getMinSelectionIndex();
			DataFlowComponent component=(DataFlowComponent)values.get(selIndex);
			model.setSelectedVertex(component);
			
			repaint();

		}
	}
	
	public void deselectList(){
		getSelectionModel().clearSelection();
		this.repaint();
	}
	
	private class ComponentListModel extends AbstractTableModel{
		public int getRowCount(){
			return values.size();
		}
		public int getColumnCount(){
			return 1;
		}
		
		public Object getValueAt(int i,int j){
			return ((DataFlowComponent)values.get(i)).getLabel();
		}
		
		public String getColumnName(int columnName){
			return cat;
		}
	}
	

}
