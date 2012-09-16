/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 * 
 * HTML code Editor 
 */

public class HTMLcodeEditor extends JDialog{
	
	JTextArea textArea = new JTextArea(5, 30);
	JScrollPane scrollPane = new JScrollPane(textArea);
	
	public HTMLcodeEditor(JFrame parentFrame,String caption,String content) {
		super(parentFrame,caption,true);
		initialize(parentFrame,content);		
		
		
	}

	private void initialize(Component parent, String content){

		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		textArea.setText(content);

		// initialize button Panel
		
		JPanel buttonPanel=new JPanel();
		FlowLayout fl=new FlowLayout(FlowLayout.CENTER);
		buttonPanel.setLayout(fl);
		JButton okButton=new JButton("OK");
		buttonPanel.add(okButton);
		
		okButton.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						hide();
					}
				}
		);
		
		// assemble all
		


		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		Rectangle pd=parent.getBounds();
		
		setBounds(pd.x+(int)(0.5*pd.width)-350,
				  pd.y+(int)(0.25*pd.height),
				  700,(int)(0.5*pd.height)
		);
		
	}
	
	public String getContent(){
		return textArea.getText();
	}
	

}
