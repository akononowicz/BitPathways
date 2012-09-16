/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/


package edu.uj.zbit.bpath.editor.dialogs;

import javax.swing.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

import java.awt.*;
import java.awt.event.*;

public class NewTaskWizard extends AddElementWizard {
	
	JTextField nameField=new JTextField(BpathMainFrame.messages.getString("DIALOG.NEWCOMPONENT.DEFAULTNAME"));
	JLabel label1=new JLabel(BpathMainFrame.messages.getString("DIALOG.NEWCOMPONENT.ASK"));
	JButton jOk=new JButton(BpathMainFrame.messages.getString("COMMON.OK"));
	JPanel buttonPanel=new JPanel();
	JFrame parent;
	
	public NewTaskWizard(JFrame _parent){
		super(_parent);
		
		parent=_parent;
		BorderLayout borderLayout=new BorderLayout();
		getContentPane().setLayout(borderLayout);
		getContentPane().add(label1,BorderLayout.NORTH);
		getContentPane().add(nameField,BorderLayout.CENTER);
		

		jOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (nameField.getText().length()==0){
					JOptionPane.showMessageDialog(parent,BpathMainFrame.messages.getString("DIALOG.NEWCOMPONENT.DEFAULTNAME"));
				}else{
					accepted=true;
					Object[] value=new Object[1];
					value[0]=nameField.getText();
					attribs.addAttribute("label", value);
					//attribs.addAttribute(Dict.ELEMENT_CATEGORY, BpathMainFrame.messages.getString("ACTION.NEWELEMENT.OTHERCAT"));					
					setVisible(false);
				}
			}
		});
		
		FlowLayout flowLayout=new FlowLayout();
		flowLayout.setAlignment(FlowLayout.CENTER);
		
		buttonPanel.setLayout(flowLayout);
	
		buttonPanel.add(jOk);
		
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		
		Rectangle p=parent.getBounds();
		int w=300,h=100;
		this.setBounds(p.x+(p.width-w)/2,p.y+(p.height-h)/2,w,h);
		
		this.setResizable(false);
	}
	
}
