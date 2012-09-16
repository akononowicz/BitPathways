/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/


package edu.uj.zbit.bpath.editor.dialogs.mapping;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 
 * Dummy starter class - to be replaced by proper binding 
 * 
 * @author Andrzej Kononowicz
 */

public class Start extends JFrame {


	public Start(){
		
		
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		TemplateMappingDialog dialog=new TemplateMappingDialog();
		dialog.setModal(true);
		
		dialog.setBounds(100, 100, 800, 400);
		dialog.setVisible(true);
		dispose();
		System.exit(0);
		
	}
	

	public static void main(String[] args) {
		new Start();
	}

}
