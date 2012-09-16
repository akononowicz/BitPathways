/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs;

/**
 * Dialog box for selecting new subpathway (either as existing or new pathway)
 * 
 * @author Andrzej Kononowicz, 2011
 * 
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;

public class SubpathWizard extends AddElementWizard {
	
	private BpathMainFrame parent;

	private String pathid;
	private String pathtitle;

	private JButton newPathButton,existingPathButton;
	private JPanel newPathPanel,existingPathPanel;
	private JLabel caption;
	
	public SubpathWizard(JFrame _parent) {
		
		super(_parent);
		parent=(BpathMainFrame)_parent;
		accepted=false;
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.setTitle(BpathMainFrame.messages.getString("ELEMENT.SUBPATH"));
		
		// add components
		
		caption = new JLabel(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.INFO"));
		newPathButton=new JButton(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.ASNEWPATHWAY"));
		existingPathButton=new JButton(BpathMainFrame.messages.getString("DIALOG.NEWSUBPATHWAY.ASEXISTINGPATH"));

		
		newPathPanel=new JPanel(new FlowLayout());
		newPathPanel.add(newPathButton);
		
		existingPathPanel=new JPanel(new FlowLayout());
		existingPathPanel.add(existingPathButton);
		
		this.add(caption,BorderLayout.NORTH);
		this.add(newPathPanel,BorderLayout.WEST);
		this.add(existingPathPanel,BorderLayout.EAST);		

		
		// set behavior
		
		existingPathButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					Hashtable<Metadata,String> pathway_info=ExistConnection.getPathList(((BpathMainFrame)parent).getUser());
					LoadPathDialog dialog=new LoadPathDialog(parent,pathway_info);
					dialog.setVisible(true);
					
					if (dialog.isSelected()){
						pathid=dialog.getPathId();
						pathtitle=dialog.getPathName();
						
						attribs.addAttribute("label", new Object[]{pathtitle});	 
						attribs.addAttribute("subpath", new Object[]{pathid});
						
						accepted=true;
						
					}
					
					setVisible(false);
					
				}catch(Exception eee){
					eee.printStackTrace();
				}	
			}
		});
		
		
		// set window size
		
		int box_width=250;
		int box_height=80;
		
		int box_x=parent.getX()+(parent.getWidth()/2)-(box_width/2);
		int box_y=parent.getY()+(parent.getHeight()/2)-(box_height/2);
		
		this.setBounds(box_x, box_y, box_width, box_height);
		
		this.setResizable(false);
	}

	
	public String getPathtitle() {
		return pathtitle;
	}

	public String getPathid() {
		return pathid;
	}
	
}
