/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

/**
 * @author Andrzej Kononowicz
 */
public class CiteEditor extends JDialog {
	
	protected String abstr;
	protected String cite;
	protected boolean completed;
	
	public CiteEditor(Frame owner, String _abstr, String _cite) {
		super(owner,true);
		
		abstr=_abstr;
		cite=_cite;
		
		Rectangle pd=owner.getBounds();
		
		int w=500;
		int h=200;
		

		initComponents();
		
		setBounds(pd.x+(int)(0.5*pd.width)-w/2,
				  pd.y+(int)(0.5*pd.height)-h/2,
				  w,h
		);
		
		citeField.setText(cite);
		abstrArea.setText(abstr);
		this.setResizable(false);
	}

	public boolean isCompleted() {
		return completed;
	}
	
	
	private void initComponents() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		citeField = new JTextArea();
		label2 = new JLabel();
		scrollPane1 = new JScrollPane();
		abstrArea = new JTextArea();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.CITE.TITLE"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);

			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new BorderLayout());


				//---- label2 ---- 
				label2.setText(BpathMainFrame.messages.getString("DIALOG.CITE.CITE")+":");
				contentPanel.add(label2, BorderLayout.NORTH);

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(citeField);
				}
				contentPanel.add(scrollPane1, BorderLayout.CENTER);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						completed=true;
						setVisible(false);
					}
				});
				
				//---- cancelButton ----
				cancelButton.setText(BpathMainFrame.messages.getString("COMMON.CANCEL")+":");
				buttonBar.add(cancelButton, cc.xy(4, 1));
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						completed=false;
						setVisible(false);
					}
				});
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		

		
		pack();
		setLocationRelativeTo(getOwner());
		
	}


	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextArea citeField;
	private JLabel label2;
	private JScrollPane scrollPane1;
	private JTextArea abstrArea;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;

	public String getAbstract() {
		abstr=abstrArea.getText();
		return abstr;
	}

	public void setAbstract(String dscr) {
		this.abstr = dscr;
	}

	public String getCite() {
		cite=citeField.getText();
		return cite;
	}

	public void setCite(String href) {
		this.cite = href;
	}

}
