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
import javax.swing.JToolBar;

import com.hexidec.ekit.EkitCore;

/**
 * 
 * HTML Editor based on Ekit 
 */

public class HTMLEditor extends JDialog{

	private final String TOOLBAR_MIPP = "CT|CP|PS|UN|RE|BL|IT|UD|SK|SU|SB|UL|OL|AL|AC|AR|AJ|UC|UM|FO";
	protected EkitCore ekitCore;

	
	
	public HTMLEditor(JFrame parentFrame,String caption,String content) {
		super(parentFrame,caption,true);
		initialize(parentFrame,content);		
		
		
	}

	public HTMLEditor(JDialog parent,String caption,String content) {
		super(parent,caption,true);
		initialize(parent,content);	
	}
	
	private void initialize(Component parent, String content){
		// initialize ekit core
		
		boolean base64 = false;
		URL urlCSS = (URL)null;
		boolean showToolBar = true;
		boolean showToolBarMulti = false;
		boolean showViewSource = false;
		String sLanguage = null;
		String sCountry = null;
		boolean editModeExclusive = true;
		boolean showMenuIcons = true;
		String toolbarSeq = TOOLBAR_MIPP;
	
		ekitCore = new EkitCore(content, urlCSS, showToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, false, showToolBarMulti, toolbarSeq);
		ekitCore.initializeSingleToolbar(toolbarSeq);


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
		this.getContentPane().add(ekitCore, BorderLayout.CENTER);
		this.getContentPane().add(ekitCore.getToolBar(showToolBar), BorderLayout.NORTH);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		Rectangle pd=parent.getBounds();
		
		setBounds(pd.x+(int)(0.5*pd.width)-350,
				  pd.y+(int)(0.25*pd.height),
				  700,(int)(0.5*pd.height)
		);
		

	}
	
	public String getContent(){
		return ekitCore.getDocumentBody();
	}
	

}
