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
public class LinkEditor extends JDialog {
	
	protected String dscr;
	protected String href;
	protected boolean completed;
	
	public LinkEditor(Frame owner, String _dscr, String _href) {
		super(owner,true);
		
		dscr=_dscr;
		href=_href;
		
		Rectangle pd=owner.getBounds();
		
		int w=500;
		int h=400;
		

		initComponents();
		
		setBounds(pd.x+(int)(0.5*pd.width)-w/2,
				  pd.y+(int)(0.5*pd.height)-h/2,
				  w,h
		);
		
		hrefField.setText(href);
		dscrArea.setText(dscr);
		this.setResizable(false);
	}

	public boolean isCompleted() {
		return completed;
	}
	
	
	private void initComponents() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		hrefField = new JTextField();
		label2 = new JLabel();
		scrollPane1 = new JScrollPane();
		dscrArea = new JTextArea();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.LINK.TITLE"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);

			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(19), FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					}));

				//---- label1 ----
				label1.setText(BpathMainFrame.messages.getString("DIALOG.LINK.LINK")+":");
				contentPanel.add(label1, cc.xy(1, 1));
				contentPanel.add(hrefField, cc.xywh(3, 1, 3, 1));

				//---- label2 ---- 
				label2.setText(BpathMainFrame.messages.getString("DIALOG.LINK.DSCR")+":");
				contentPanel.add(label2, cc.xy(1, 3));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(dscrArea);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 3, 3, 3));
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
				cancelButton.setText(BpathMainFrame.messages.getString("COMMON.CANCEL"));
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
	private JTextField hrefField;
	private JLabel label2;
	private JScrollPane scrollPane1;
	private JTextArea dscrArea;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;

	public String getDscr() {
		dscr=dscrArea.getText();
		return dscr;
	}

	public void setDscr(String dscr) {
		this.dscr = dscr;
	}

	public String getHref() {
		href=hrefField.getText();
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
