/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class MoveAllLeftCommmand extends MoveAllCommmand {
	
	public static String NAME=messages.getString("ACTION.MOVE.LEFT");
		
	public MoveAllLeftCommmand(PathModel _model,JFrame _parent){
		super(NAME);model=_model;parent=_parent;
		
		dir=super.MOVE_LEFT;
	}
}
