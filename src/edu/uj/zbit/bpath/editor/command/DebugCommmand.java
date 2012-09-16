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


public class DebugCommmand extends BpathAction {
	
	public static String NAME="Debug";
	
	private PathModel model;
	private BpathMainFrame parent;
	private DataFlowBranch newBranch;
	
	public DebugCommmand(PathModel _model,JFrame _parent){
		super(NAME);
		model=_model;
		parent=(BpathMainFrame)_parent;
	}
	
	public void doIt(){
		System.out.println("=================== Debug =====================");
		
		if (model==null){
			System.out.println("Empty model");
		}else{
			System.out.println("STATE:\t"+model.getState());
			System.out.println("PATH ID:\t"+model.getPathId());
			System.out.println("No server:\t"+parent.noserver);
			System.out.println("Is modified:\t"+model.isModified());
			System.out.println("File:\t"+model.getLastSavedFile());			
			
			
			System.out.println("User id:\t"+parent.getUser().getUid());
			System.out.println("User role:\t"+parent.getUser().getRole());			
				
		}
		
		
		
	}
	
	public void undoIt(){
		//TODO
	}
	
}
