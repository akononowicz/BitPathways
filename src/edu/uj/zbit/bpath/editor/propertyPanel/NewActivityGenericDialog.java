/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

/**
 * @author Andrzej Kononowicz
 */
public class NewActivityGenericDialog extends JDialog {
	
	private boolean completed;
	
	public NewActivityGenericDialog(Frame owner) {
		super(owner,true);
		initComponents();
		
		Rectangle pd=owner.getBounds();
		int h=150,w=350;
		setBounds(pd.x+(int)(0.5*pd.width)-w/2,
				  pd.y+(int)(0.5*pd.height)-h/2,
				  w,h);
		setResizable(false);
	}


	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		shortnameTextField = new JTextField();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle("Neue Leistung");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));			
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};
				
				//---- label1 ----
				label1.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.ACTIVITYSHORTNAME"));
				contentPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				contentPanel.add(shortnameTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
			
			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};
				
				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				
				//---- cancelButton ----
				cancelButton.setText(BpathMainFrame.messages.getString("COMMON.CANCEL"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
	}

	private void okButtonActionPerformed(ActionEvent e) {
		completed=true;
		hide();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		completed=false;
		hide();
	}

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField shortnameTextField;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;

	public boolean isCompleted() {
		return completed;
	}

	public String getShortname() {
		return shortnameTextField.getText();
	}

}
