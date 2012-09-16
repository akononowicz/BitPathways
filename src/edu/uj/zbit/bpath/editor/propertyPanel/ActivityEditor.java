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
import javax.swing.border.*;
import javax.swing.text.html.HTMLEditorKit;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.ControledVocabularyDict;


/**
 * @author Andrzej Kononowicz
 */
public class ActivityEditor extends JDialog {

	boolean completed=false;
	private ActivityEditor me;
	
	public ActivityEditor(Frame owner) {
		super(owner,true);
		me=this;
		initComponents();
		
		Rectangle pd=owner.getBounds();
		int h=300,w=500;
		setBounds(pd.x+(int)(0.5*pd.width)-w/2,
				  pd.y+(int)(0.5*pd.height)-h/2,
				  w,h
		);
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		shortnameLabel = new JLabel();
		label2 = new JLabel();
		catLabel = new JLabel();
		label3 = new JLabel();
		nameArea = new JEditorPane();
		nameArea.setEditable(false);
		nameArea.setBackground(new Color(212,208,200));
		label4 = new JLabel();
		pointsLabel = new JLabel();
		label5 = new JLabel();
		scrollPane1 = new JScrollPane();
		dscrPanel = new JEditorPane();

		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.ACTIVITYCATALOGUE"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0E-4};
				
				//---- label1 ----
				label1.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.ID"));
				contentPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(shortnameLabel, new GridBagConstraints(1, 0, 4, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- label2 ----
				label2.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.ACTIVITYCATALOGUE"));
				contentPanel.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(catLabel, new GridBagConstraints(1, 1, 4, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- label3 ----
				label3.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.NAME"));
				contentPanel.add(label3, new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(nameArea, new GridBagConstraints(1, 2, 7, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label4 ----
				label4.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.POINTS"));
				contentPanel.add(label4, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(pointsLabel, new GridBagConstraints(1, 4, 7, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				
				//---- label5 ----
				label5.setText(BpathMainFrame.messages.getString("DIALOG.ACTIVITY.DSCR"));
				contentPanel.add(label5, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//======== scrollPane1 ========
				{
					
					//---- dscrPanel ----
					dscrPanel.setEditable(true);
					
					scrollPane1.setViewportView(dscrPanel);
				}
				contentPanel.add(scrollPane1, new GridBagConstraints(1, 5, 7, 2, 0.0, 0.0,
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
				buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {

						completed=true;
						hide();
						
					}
				});
				
				//---- cancelButton ----
				cancelButton.setText(BpathMainFrame.messages.getString("COMMON.CANCEL"));
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						completed=false;
						hide();						
					}
				});
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
	}

	public String getShortname(){
		return shortnameLabel.getText();
	}
	
	public void setShortname(String shortname){
		shortnameLabel.setText(shortname);
	}	
	
	public String getActivityCatalogue(){
		return catLabel.getText();
	}	
	
	public void setActivityCatalogue(String actCat){
		catLabel.setText(actCat);
	}	
	
	public String getActivityName(){
		return nameArea.getText();
	}	
	
	public void setActivityName(String name){
		nameArea.setText(name);
	}
	
	public String getPoints(){
		return pointsLabel.getText();
	}
	
	public void setPoints(String points){
		pointsLabel.setText(points);
	}
	
	public void setDscr(String dscr){
		dscrPanel.setText(dscr);
	}
	
	public String getDscr(){
		return dscrPanel.getText();
	}
	
	public boolean isCompleted(){
		return completed;
	}
	
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel shortnameLabel;
	private JLabel label2;
	private JLabel catLabel;
	private JLabel label3;
	private JEditorPane nameArea;
	private JLabel label4;
	private JLabel pointsLabel;
	private JLabel label5;
	private JScrollPane scrollPane1;
	private JEditorPane dscrPanel;

	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;

}
