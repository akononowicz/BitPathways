/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.MetadataEntity;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;
import edu.uj.zbit.bpath.editor.*;
import edu.uj.zbit.bpath.editor.dialogs.NewTaskWizard;
import edu.uj.zbit.bpath.editor.dialogs.MetadataWizard;


import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class DuplicatePathCommand extends AbstractAction {
	
	public static String NAME=BpathMainFrame.messages.getString("ACTION.DUPLICATE");
	
	private PathModel model;
	private BpathMainFrame parent;
	private DataFlowCanvas canvas;
	
	public DuplicatePathCommand(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.DUPLICATE"));
		model=_model;
		parent=(BpathMainFrame)_parent;
		canvas=_canvas;
	}
	
	public void actionPerformed(ActionEvent e){
		
		ExistConnection.save(model.getPathId(), model, canvas, ""+parent.getUser().getUid());
		
		String id=ExistConnection.createNewResource();
		
		if (!BpathMainFrame.noserver){
			
			if (id==null){
				JOptionPane.showMessageDialog(parent, BpathMainFrame.messages.getString("ERROR.XMLSERVER"), BpathMainFrame.messages.getString("ERROR"), JOptionPane.ERROR_MESSAGE);	
				return;
			}
		}else{
			id="tmp"+Math.random();
		}
		
		model.setPathId(id);
		
		JOptionPane.showMessageDialog(
				parent, 
				BpathMainFrame.messages.getString("ACTION.DUPLICATIONSUCCESS"),
				BpathMainFrame.messages.getString("ACTION.SAVECONFIRMED"),
				JOptionPane.PLAIN_MESSAGE
		);
			
	}
}
