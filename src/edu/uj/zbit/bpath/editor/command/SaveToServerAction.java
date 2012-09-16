/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;

import javax.swing.*;
import java.awt.event.*;

public class SaveToServerAction extends AbstractAction  {
	
	private PathModel model;
	private BpathMainFrame parent;
	private DataFlowCanvas canvas;

	public SaveToServerAction(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.SERVERSAVE"));
		model=_model;
		parent=(BpathMainFrame)_parent;
		canvas=_canvas;
	}

	public SaveToServerAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		
		model.deactivateSelection();
		
		if (enabled){
			if (model.getPathId().startsWith("tmp0.")){
				String newid=ExistConnection.createNewResource();
				model.setPathId(newid);
				
			}
			
			int error=ExistConnection.save(model.getPathId(), model, canvas, ""+parent.getUser().getUid());
			
			if (error==1){
				JOptionPane.showMessageDialog(
						parent, 
						BpathMainFrame.messages.getString("ERROR.CONNECTIONLOST"),
						BpathMainFrame.messages.getString("ERROR.CONNECTIONLOSTTITLE"),
						JOptionPane.ERROR_MESSAGE
				);
				parent.emergencySave();
				
			}else if (error==2){
			
				JOptionPane.showMessageDialog(
						parent, 
						BpathMainFrame.messages.getString("ERROR.OWNERERROR"),
						BpathMainFrame.messages.getString("ERROR.OWNERERRORTITLE"),
						JOptionPane.ERROR_MESSAGE
				);
				parent.emergencySave();
			
				
			}else{ model.setState(PathModel.FLAG_EDITABLE);
				JOptionPane.showMessageDialog(
						parent, 
						BpathMainFrame.messages.getString("ACTION.SAVESUCCESS"),
						BpathMainFrame.messages.getString("ACTION.SAVECONFIRMED"),
						JOptionPane.PLAIN_MESSAGE
				);
			}
		}
	}
	
	
}
