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

public class SaveAsToFileAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;
	
	public SaveAsToFileAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.FILESAVEAS"));
		model=_model;
		parent=_parent;
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File file){
				if (!file.isDirectory()) return file.getName().endsWith(".path");
				else return true;
			}
			public String getDescription(){
				return "*.path";
			}
			
		});
	}
	

	public SaveAsToFileAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){
			chooser.showSaveDialog(parent);
			File file=chooser.getSelectedFile();
			if (file==null) return; 
			
			if(!file.getName().endsWith(".path"))
				file=new File(file.getAbsolutePath()+".path");
			
			// Save model to file
			model.saveModelToFile(file);
			
			model.setLastSavedFile(file);
			
			
		}
	}
}
