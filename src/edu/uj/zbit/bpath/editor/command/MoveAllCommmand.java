/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.*;
import edu.uj.zbit.bpath.editor.dialogs.NewTaskWizard;


import javax.swing.*;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;


public abstract class MoveAllCommmand extends BpathAction {

	public final static int MOVE_RIGHT=1;
	public final static int MOVE_LEFT=2;	
	public final static int MOVE_UP=3;
	public final static int MOVE_DOWN=4;	
	
	public final static int MOVE_DISTANCE=20;
	

	protected DataFlowComponent newComponent;
	
	protected int dir;

	public MoveAllCommmand(String name){
		super(name);
		setEnabled(false);
	}

	public MoveAllCommmand(String name, Icon icon, PathModel model, JFrame frame){
		super(name,icon,model,frame);
	}

	protected void move(){
		FlowChartNode[] nodes=model.getAllVertices();
		
		for (FlowChartNode n : nodes) {
			
			switch (dir) {
			case MOVE_DOWN:
				if ((n.y+MOVE_DISTANCE)<DataFlowCanvas.MAX_HEIGHT) n.y+=MOVE_DISTANCE;
				break;
			case MOVE_UP:
				if ((n.y-MOVE_DISTANCE)>0) n.y-=MOVE_DISTANCE;
				break;
			case MOVE_RIGHT:
				if ((n.x+MOVE_DISTANCE)<DataFlowCanvas.MAX_WIDTH) n.x+=MOVE_DISTANCE;
				break;
			case MOVE_LEFT:
				if ((n.x-MOVE_DISTANCE)>0) n.x-=MOVE_DISTANCE;
				break;
			default:
				break;
			}
			
			parent.repaint();
		}
	}

	public void doIt(){
		move();
	}
	
	public void undoIt(){
		//TODO
	}
	
}
