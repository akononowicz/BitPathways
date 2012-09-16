/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import javax.swing.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.PathModel;

import java.awt.Cursor;
import java.awt.event.*;
import java.util.ResourceBundle;

public abstract class BpathAction extends AbstractAction implements BpathCommand {
	
	protected static ResourceBundle messages=BpathMainFrame.messages;
	
	protected PathModel model;
	protected JFrame parent;
	
	public BpathAction(String name){
		super(name);
	}

	public BpathAction(String _name, Icon _icon,PathModel _model,JFrame _parent){
		super(_name,_icon);
		this.model=_model;
		this.parent=_parent;
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled) doIt();
	}
	
	public void doIt(){
		model.addStateFlag(PathModel.FLAG_MODIFIED);
		
	}
}
