/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.events.PathModelEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelListener;

/** Contains tables with staff categories */

public class ComponentListPanel extends JPanel implements PathModelListener{

	PathModel model;
	ComponentListTable[] lists;
	
	public ComponentListPanel(PathModel _model){
		model=_model;
		initComponents();
	}

	public void initComponents(){
		setLayout(new GridLayout(1,4));
		lists=new ComponentListTable[]{
				new ComponentListTable(this,model,BpathMainFrame.messages.getString("MAIN.PANELS.COMPONENTLIST.PHYSICIANS")),
				new ComponentListTable(this,model,BpathMainFrame.messages.getString("MAIN.PANELS.COMPONENTLIST.NURSES")),
				new ComponentListTable(this,model,BpathMainFrame.messages.getString("MAIN.PANELS.COMPONENTLIST.TECHNICANS")),
				new ComponentListTable(this,model,BpathMainFrame.messages.getString("MAIN.PANELS.COMPONENTLIST.OTHERS"))	
		};
		for (int i = 0; i < lists.length; i++) {
			add(new JScrollPane(lists[i]));
		}
	}
	/* TODO:To be corrected -> Language specific names hard-coded*/
	private int getCatIndex(String name){
		if (name.equalsIgnoreCase("Arztdienst")) return 0;
		if (name.equalsIgnoreCase("Pflegedienst")) return 1;
		if (name.equalsIgnoreCase("Behandlungsdienste")) return 2;
		return 3;
	}
	
	public void elementToBeAdded(PathModelEvent e){
	
	}
	
	public void elementAdded(PathModelEvent e){
		if (e.getSource() instanceof DataFlowComponent){
			DataFlowComponent comp=(DataFlowComponent) e.getSource();
			int catIndex=getCatIndex(comp.getCategory());
			lists[catIndex].updateData();
		}
	}
	
	public void elementSelected(PathModelEvent e){
		if (e.getSource() instanceof DataFlowComponent){
			
			DataFlowComponent comp=(DataFlowComponent) e.getSource();
			int catIndex=getCatIndex(comp.getCategory());
			lists[catIndex].addSelection(comp);

			for (int i = 0; i < lists.length; i++) {
				if (i!=catIndex){
					lists[i].deselectList();
				}
			}	
		}
	}
	
	
	public void elementRemoved(PathModelEvent e){
		if (e.getSource() instanceof DataFlowComponent){
			DataFlowComponent comp=(DataFlowComponent) e.getSource();
			int catIndex=getCatIndex(comp.getCategory());
			lists[catIndex].updateData();
		}
	}
	
	public void elementChanged(PathModelEvent e){
		for (int i = 0; i < lists.length; i++) {
			lists[i].updateData();
		}
	}
	
	public void modelLoaded(PathModelEvent e){
		
		for (int i = 0; i < lists.length; i++) {
			lists[i].updateData();
		}
	}
	
	public void deselectOthers(String catName){
		int index=getCatIndex(catName);
		for (int i = 0; i < lists.length; i++) {
			if (i!=index){
				lists[i].deselectList();
			}
		}
	}

	
}
