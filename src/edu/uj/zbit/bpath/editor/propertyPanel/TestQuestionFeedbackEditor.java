/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

public class TestQuestionFeedbackEditor extends JDialog {

	private JButton okButton=new JButton(BpathMainFrame.messages.getString("COMMON.OK"));
	public JTextArea textArea=new JTextArea();
	private JPanel centerPanel=new JPanel();
	private JPanel buttonPanel=new JPanel();
	
	public TestQuestionFeedbackEditor(Frame parent, String feedback) {
		super(parent, BpathMainFrame.messages.getString("DIALOG.MCQ.FEEDBACK"));
		
		
		textArea.setText(feedback);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(okButton,BorderLayout.SOUTH);
		getContentPane().add(textArea,BorderLayout.CENTER);
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
			}
		});
		
		int w=300;int h=300;
		Rectangle p=parent.getBounds();
		
		setBounds(p.x+p.width/2-w/2, p.y+p.height/2-h/2, w, h);
		
	}


}
