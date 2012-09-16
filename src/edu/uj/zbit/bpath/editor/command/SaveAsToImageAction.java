/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;

import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;

public class SaveAsToImageAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;
	private DataFlowCanvas canvas;

	
	public SaveAsToImageAction(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.IMAGESAVE"));
		model=_model;
		parent=_parent;
		canvas=_canvas;
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File file){
				if (!file.isDirectory()) return file.getName().endsWith(".png");
				else return true;
			}
			public String getDescription(){
				return "*.png";
			}
			
		});
	}
	

	public SaveAsToImageAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){
			
			
			chooser.showSaveDialog(parent);
			File file=chooser.getSelectedFile();
			if (file==null) return; 
			
			if(!file.getName().endsWith(".png"))
				file=new File(file.getAbsolutePath()+".png");
			
			model.deactivateSelection();
			
			RenderedImage rendImage = canvas.saveImage();
			try{
				ImageIO.write(rendImage, "png", file);
			}catch(Exception err){
				err.printStackTrace();
			}
					
		}
	}
}
