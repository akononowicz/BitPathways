/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class RemoveNodeCommmand extends BpathAction {
	
	public static String NAME=messages.getString("ACTION.DELETE");
	
	private FlowChartNode removedNode;
	
	public RemoveNodeCommmand(FlowChartNode _node,PathModel _model){
		super(NAME,null,_model,null);
		removedNode=_node;
	}
	
	public void doIt(){
		super.doIt();
		model.removeVertex(removedNode.getId());
		
	}
	
	public void undoIt(){
		//TODO
	}
	
}
