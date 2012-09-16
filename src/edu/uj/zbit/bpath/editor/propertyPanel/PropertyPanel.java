/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.*;
import edu.uj.zbit.bpath.editor.model.*;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowPath;
import edu.uj.zbit.bpath.editor.model.events.PathModelEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelListener;

import java.awt.*;

public class PropertyPanel extends JPanel {

	protected PathModel model;
	protected String[] groupNames;
	
	protected PropertyPanelHeader classHeader; 
	protected PropertyPanelHeader[] propertyHeaders;
	protected int activeGroupIndex=0;
	protected PropertyPanel thisPanel=this;
	protected JTableX table=new JTableX();
	protected AttribGroupSet tableModels;
	
	public PropertyPanel(PathModel _model){
		model=_model;
		model.addPathModelListener(new PathModelListener(){
			
			public void elementToBeAdded(PathModelEvent e){
			  	repaint();
			}
			
			  
			public void elementAdded(PathModelEvent e){
				  	repaint();
			}
			

			public void elementSelected(PathModelEvent e){
				  JTabbedPane tabPane=(JTabbedPane)getParent();
				  if (tabPane.getSelectedComponent()==thisPanel){ 
					  FlowChartElement dfe=(FlowChartElement) e.getSource();
					  reloadPanel(dfe);				  
				  }
			}
			  
			public void elementRemoved(PathModelEvent e){
				  	repaint();
			}
			
			public void elementChanged(PathModelEvent e){
			  	repaint();
			}
			
			public void modelLoaded(PathModelEvent e){
				elementSelected(e);
			}
		 });
		
		setMinimumSize(new Dimension(10,10));
	}

	
	public void reloadPanel(FlowChartElement dfe){
		  removeAll();
		  setLayout(new BorderLayout());
		
		  if(dfe instanceof DataFlowEdge){
			  tableModels=model.getEdgeAttributes(dfe.getId());  
		  }else if (dfe instanceof DataFlowPath){
			  tableModels=model.getPathAttributes();
		  } else {
			  tableModels=model.getNodeAttributes(dfe.getId());
		  }
		  
		  groupNames=new String[tableModels.getSize()];
		  propertyHeaders=new PropertyPanelHeader[tableModels.getSize()];
		  
		  classHeader=new PropertyPanelHeader(thisPanel,dfe.getTypeName()+":");
		  classHeader.setActive(true);
		  classHeader.setHBgColor(classHeader.CLASS_HEADER);
		
		  activeGroupIndex=0;
		  
		  for (int i = 0; i < tableModels.getSize(); i++) {  
			groupNames[i]=tableModels.get(i).getName();
			propertyHeaders[i]=new PropertyPanelHeader(thisPanel,tableModels.get(i).getName());
		  }
		  propertyHeaders[activeGroupIndex].setActive(true);
		  
		  // north panel
		  JPanel northPanel=new JPanel();

		  northPanel.setLayout(new GridLayout(2,1));
		  northPanel.add(classHeader);
		  northPanel.add(propertyHeaders[activeGroupIndex]);
		  
		  add(northPanel,BorderLayout.NORTH);
		  
		  // centre panel
		  table.setModel(tableModels.get(0));
		  JScrollPane scroll=new JScrollPane(table);
		  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		  
		  
		  scroll.getViewport().setBackground(Color.WHITE);
		  
		  add(scroll,BorderLayout.CENTER);

		  // south panel
		  JPanel southPanel=new JPanel();
		  southPanel.setLayout(new GridLayout(tableModels.getSize()-1,1));
		  
		  for(int i=0;i<tableModels.getSize();i++){
			  if (i!=activeGroupIndex) southPanel.add(propertyHeaders[i]);
		  }
		  
		  add(southPanel,BorderLayout.SOUTH);
		 
		  repaint();
		  revalidate();
	}
	
	public void fireSelected(String name){

		int j=1;
		for(int i=0; i<propertyHeaders.length;i++){
			if (groupNames[i].equalsIgnoreCase(name)){
				activeGroupIndex=i;
				table.setModel(tableModels.get(activeGroupIndex));	
				propertyHeaders[0].setName(tableModels.get(activeGroupIndex).getName());
			}else{
				propertyHeaders[j].setName(tableModels.get(i).getName());
				j++;
			}
		}

		repaint();
		revalidate();
	}
	
}
