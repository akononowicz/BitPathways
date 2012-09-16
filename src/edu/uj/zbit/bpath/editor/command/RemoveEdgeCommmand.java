/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class RemoveEdgeCommmand extends BpathAction {
	
	public static String NAME=messages.getString("ACTION.DELETE");
	private DataFlowEdge removedEdge;
	
	public RemoveEdgeCommmand(DataFlowEdge _edge,PathModel model){
		super(NAME,null,model,null);
		removedEdge=_edge;
	}
	
	public void doIt(){
		super.doIt();
		model.removeEdge(removedEdge.getId());
		model.fireElementRemoved(removedEdge);
		
	}
	
	public void undoIt(){
		//TODO
	}
	
}
