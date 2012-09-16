/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFrame;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.dialogs.MetadataWizard;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.PathModel;


public class ChangeMetadataAction extends AbstractAction {
	
	BpathMainFrame parent;
	PathModel model;
	
	public ChangeMetadataAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.METADATA"));
		parent=(BpathMainFrame)_parent;
		model=_model;
	}

	public ChangeMetadataAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		MetadataWizard dialog=new MetadataWizard(parent,MetadataWizard.MODE_EDIT_PATH);
		dialog.setVisible(true);
		
		if (dialog.isCompleted()){
			
			NewPathCommmand.updateMetadata(dialog, model.getMetadata());
			parent.setTitle(BpathMainFrame.messages.getString("MAIN.TITLE")+" - "+model.getMetadata().getTitleMetadata().getName());
		}
		
	}
	
	
	
}
