/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;

import java.awt.event.*;
import java.io.File;

public class SaveToFileAction extends AbstractAction  {
	
	private PathModel model;
	private BpathMainFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;

	
	public SaveToFileAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.FILESAVE"));
		
		model=_model;
		parent=(BpathMainFrame)_parent;
		/*
		
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File file){
				if (!file.isDirectory()) return file.getName().endsWith(".xml");
				else return true;
			}
			public String getDescription(){
				return "*.xml";
			}
			
		});*/
	}
	

	public SaveToFileAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){

			File file =model.getLastSavedFile();
			
			if (file==null){
				SaveAsToFileAction sva=new SaveAsToFileAction(model,parent);
				sva.actionPerformed(e);
				
			}else{
				// Save model to file

				System.out.println("Saved");
				model.saveModelToFile(file);

			}
			
			if (parent.noserver) model.setState(PathModel.FLAG_EDITABLE);
				
		}
	}
}
