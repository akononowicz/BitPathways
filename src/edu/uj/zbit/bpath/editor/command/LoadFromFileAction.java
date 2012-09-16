/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;


import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.PathModel;

import java.awt.event.*;
import java.io.File;

public class LoadFromFileAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private JFileChooser chooser;

	
	public LoadFromFileAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.FILELOAD"));
		model=_model;
		parent=_parent;
		chooser=new JFileChooser(model.getLastSavedFile());
		
		chooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File file){
				if (!file.isDirectory()) 
					return (file.getName().endsWith(".path"))||(file.getName().endsWith(".xml"));
				else 
					return true;
			}
			public String getDescription(){
				return "*.path or *.xml";
			}
			
		});
	}
		

	public LoadFromFileAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){

		if (enabled){

			// TODO: Ask to save

			if (model.isModified()){
				int answer = JOptionPane.showConfirmDialog(parent,BpathMainFrame.messages.getString("ACTION.FILELOAD.CONFIRM"));
				
				if(answer==JOptionPane.NO_OPTION){
					return;
				}
			}

			
			chooser.showOpenDialog(parent);
			File file=chooser.getSelectedFile();
			if (file==null) return; 
			
			try{
				model.loadModelFromFile(file);
			}catch(Exception me){
				JOptionPane optionPane = new JOptionPane(me.getMessage(),JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
				optionPane.createDialog(parent,null).setVisible(true);
			}
			
			model.setLastSavedFile(file);
			
		}
	}
}
