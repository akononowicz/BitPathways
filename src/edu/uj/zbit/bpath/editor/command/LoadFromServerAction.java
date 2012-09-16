/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.PathwayFormatException;
import edu.uj.zbit.bpath.editor.dialogs.LoadPathDialog;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;

import java.awt.event.*;
import java.io.File;
import java.io.StringReader;
import java.util.Hashtable;

public class LoadFromServerAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;


	
	public LoadFromServerAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.SERVERLOAD"));
		model=_model;
		parent=_parent;
	}
		

	public LoadFromServerAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){

		if (model.isModified()){
			int answer = JOptionPane.showConfirmDialog(parent,BpathMainFrame.messages.getString("ACTION.FILELOAD.CONFIRM"));
			
			if(answer==JOptionPane.NO_OPTION){
				return;
			}
		}
		
		if (enabled){
			BpathMainFrame bpframe=(BpathMainFrame)parent;
			Hashtable<Metadata,String> pathListXML=ExistConnection.getPathList(bpframe.getUser());
			
			if (pathListXML==null){
				JOptionPane.showMessageDialog(
						parent, 
						BpathMainFrame.messages.getString("ERROR.CONNECTIONLOST2"),
						BpathMainFrame.messages.getString("ERROR.CONNECTIONLOSTTITLE"),
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}
			
			try{
				LoadPathDialog dialog=new LoadPathDialog(parent,pathListXML);
				dialog.setVisible(true);
				if (dialog.isSelected()){
					String id=dialog.getPathId();

					if (id==null){
						JOptionPane.showMessageDialog(parent, BpathMainFrame.messages.getString("ERROR.XMLSERVER"), BpathMainFrame.messages.getString("ERROR"), JOptionPane.ERROR_MESSAGE);	
						return;
					}
					
					Document doc=(Document)ExistConnection.getPath(id);
					NodeList nodelist=doc.getElementsByTagName("bitpathways");
					
					model.setModelAsDOM((Element)nodelist.item(0));

				}
			}catch(PathwayFormatException e1){
				e1.printStackTrace();
				JOptionPane.showMessageDialog(parent, BpathMainFrame.messages.getString("ERROR.PARSEERROR"), BpathMainFrame.messages.getString("ERROR"), JOptionPane.ERROR_MESSAGE);	
			}
		}
	}
}
