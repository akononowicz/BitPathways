/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.events.PathModelEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelListener;

/**
 * Displays in the right sidebar an HTML view of the selected element.
 * 
 * @author Andrzej A. Kononowicz
 *
 */

public class HTMLViewPanel extends JPanel {

	private PathModel model;
	private JEditorPane viewer;
	
	public HTMLViewPanel(PathModel _model){
		model=_model;
		
		setLayout(new BorderLayout());
		viewer=new JEditorPane();
		viewer.setEditorKit(new HTMLEditorKit());
		viewer.setEditable(false);
		viewer.setEnabled(false);
		
		add(new JScrollPane(viewer),BorderLayout.CENTER);
		
		model.addPathModelListener( new PathModelListener(){
				public void elementAdded(PathModelEvent e){
				  	repaint();
			}
			  
			public void elementSelected(PathModelEvent e){
				FlowChartElement dfe=(FlowChartElement) e.getSource();  
				reloadPanel(dfe);
			}
			  
			public void elementToBeAdded(PathModelEvent e){
			  	repaint();
			}
			
			public void elementRemoved(PathModelEvent e){
				  	repaint();
			}
			
			public void elementChanged(PathModelEvent e){
			  	repaint();
			}
			
			public void modelLoaded(PathModelEvent e){
				viewer.setEnabled(true);
				FlowChartElement dfe=(FlowChartElement) e.getSource();  
				reloadPanel(dfe);
			}
		
		  }
		);
	}
	
	public synchronized void reloadPanel(FlowChartElement dfe){
		
		JTabbedPane tabPane=(JTabbedPane)getParent();
	    if (tabPane.getSelectedComponent()!=this){ 
	 		  return;
		}
		
		String html=XMLProcessor.transformElement(dfe);
		viewer.setText(html);
			
	}
	
	
}
