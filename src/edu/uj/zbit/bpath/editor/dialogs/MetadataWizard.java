/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.MetadataEntity;
import edu.uj.zbit.bpath.editor.model.Template;
import edu.uj.zbit.bpath.editor.model.TemplateInfo;
import edu.uj.zbit.bpath.editor.model.User;
import edu.uj.zbit.bpath.editor.model.UserGroupInfo;
import edu.uj.zbit.bpath.editor.propertyPanel.PersonEditor;

/**
 * Metadata editors of pathways. Enables the change of selected group, pathways authors, title, etc 
 * 
 * @author Andrzej Kononowicz, 2009-2012
 */

public class MetadataWizard extends JDialog {
	
	/* Links */
	private BpathMainFrame parent;
	private JDialog thisDialog;

	/* Data */
	boolean completed=false;
	private User user;
	private UserGroupInfo info;
	
	/* Models */
	private DefaultComboBoxModel<String> templateModel=new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> topicModel=new DefaultComboBoxModel<String>();		
	private DefaultComboBoxModel<String> groupModel=new DefaultComboBoxModel<String>();	
	private DefaultComboBoxModel<String> teacherModel=new DefaultComboBoxModel<String>();
	
	private DefaultListModel<String> authorModel=new DefaultListModel<String>();
	
	/* Modes */
	
	public static final int MODE_NEW_PATH=1;
	public static final int MODE_EDIT_PATH=2;
	private int mode=0;
	
	/* GUI */
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label3,label4,label5,label6,label7,label8;
	
	private JTextField pathnameField;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	
	private JButton addAuthorButton;
	private JButton removeAuthorButton;
	
	private JComboBox <String> topicBox;
	private JComboBox <String> groupBox;	
	private JComboBox <String> teacherBox;
	private JComboBox <String> templateBox;
	
	private JList <String> authorsList;
	
	public MetadataWizard(BpathMainFrame parent,  int _mode) {

		super(parent,true);
		
		this.parent=parent;
		this.user=parent.getUser();
		this.info=user.getGroupInfo();
		
		this.mode=_mode;
		
		thisDialog=this;
		
		buildGUI();
		
		Rectangle p=parent.getBounds();
		int w=300,h=300;
		this.setBounds(p.x+(p.width-w)/2,p.y+(p.height-h)/2,w,h);
		this.setResizable(false);
		
	}

	private void initFields(){
		
		String selectedTemplateURL=null, selectedTeacher=null, selectedTitle=null, selectedGroup=null, selectedTopic=null;
		String[] selectedAuthors = null;
		
		if (mode==MODE_EDIT_PATH){
			Metadata metadata=this.parent.model.getMetadata();
			templateBox.setEnabled(false);
			selectedTemplateURL=parent.model.getDefaultTemplate().getTemplateURL();
			selectedTitle=metadata.getTitleMetadata().getName();
			
			try{
				selectedTeacher=metadata.getTeacherMetadata().getName();
				selectedGroup=metadata.getGroupMetadata().getName();
				selectedTopic=metadata.getTopicMetadata().getName();
			}catch(Exception e){}
			
			MetadataEntity[] aut=metadata.getAuthorsMetadata();
			
			selectedAuthors=new String[aut.length];
			for (int i = 0; i < aut.length; i++) {
				selectedAuthors[i]=aut[i].getName();
			}
		}
		
	    TemplateInfo[] templatesInfo=info.getTemplates();

		for (int i = 0; i < templatesInfo.length; i++) {
			templateModel.addElement(templatesInfo[i].getName());
			if (selectedTemplateURL!=null){
				if (selectedTemplateURL==templatesInfo[i].getUrl()){
					templateModel.setSelectedItem(templatesInfo[i].getName());
				}
			}
		}	

		String[] tab=info.getTeachers();
		if ((tab!=null)&&(tab.length>0))
			for (int i = 0; i < tab.length; i++) {
				String[] teacherName=tab[i].split(";");
				teacherModel.addElement(teacherName[1]+" "+teacherName[0]);
			}
		else teacherBox.setEnabled(false);
		
		if (selectedTeacher!=null){
			String[] teacherName=selectedTeacher.split(";");
			teacherBox.setSelectedItem(teacherName[1]+" "+teacherName[0]);
		}

		tab=info.getSubgroups();
		if ((tab!=null)&&(tab.length>0))
			for (int i = 0; i < tab.length; i++) {
				String name=tab[i];
				groupModel.addElement(name);
			}
		else groupBox.setEnabled(false);

		
		tab=info.getTopics();
		if ((tab!=null)&&(tab.length>0))
			for (int i = 0; i < tab.length; i++) {
				String name=tab[i];
				topicModel.addElement(name);
			}
		else topicBox.setEnabled(false);
		
		
		// revoke previous values
		
		if (selectedGroup!=null){groupBox.setSelectedItem(selectedGroup);}
		if (selectedTopic!=null){topicBox.setSelectedItem(selectedTopic);}
		if (selectedTitle!=null){pathnameField.setText(selectedTitle);}
		if (selectedAuthors!=null){
			authorModel.removeAllElements();
			for (int i = 0; i < selectedAuthors.length; i++) {
				authorModel.addElement(selectedAuthors[i]);
			}
		}
				
	}
	
	
	private void buildGUI() {

		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label3 = new JLabel();
		pathnameField = new JTextField(BpathMainFrame.messages.getString("DIALOG.NEWPATHWAY.DEFAULTNAME"));
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		
		addAuthorButton = new JButton();
		removeAuthorButton = new JButton();
		
		label4 = new JLabel();label5 = new JLabel(); label6 = new JLabel(); label7 = new JLabel(); label8 = new JLabel();

		templateBox = new JComboBox<String>(templateModel);
		topicBox = new JComboBox<String>(topicModel);
		groupBox = new JComboBox<String>(groupModel);		
		teacherBox = new JComboBox<String>(teacherModel);		

		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.METADATA.DIALOGTITLE"));
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{

				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.4, 0.0, 0.0, 1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
				
				
				//---- Template Row  ----
				label3.setText(BpathMainFrame.messages.getString("TEMPLATE"));
				contentPanel.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(templateBox, new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
					
				//---- Teacher Row ----
				
				label5.setText(BpathMainFrame.messages.getString("EDU.TEACHER"));
				contentPanel.add(label5, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					contentPanel.add(teacherBox, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));	
					
				//---- Group Field ----
				
				label6.setText(BpathMainFrame.messages.getString("EDU.CLASS"));
				contentPanel.add(label6, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					contentPanel.add(groupBox, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));						
				
				//---- Topic Row ----
				
				label4.setText(BpathMainFrame.messages.getString("EDU.TOPIC"));
					contentPanel.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					contentPanel.add(topicBox, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));					
					
				//---- Topic Row ----
					
				label7.setText(BpathMainFrame.messages.getString("EDU.TITLE"));
					contentPanel.add(label7, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));
					contentPanel.add(pathnameField, new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));							

				//------ Authors panel
				
				authorModel.addElement(user.getFamname()+";"+user.getFirstname());
				authorsList=new JList(authorModel);
				authorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
				// Label: Authors (0,5,1,1)
					
				label8.setText(BpathMainFrame.messages.getString("EDU.AUTHORS"));
				contentPanel.add(label8, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
					
				
				// Button: Add author (1,5,1,1)

				contentPanel.add(addAuthorButton, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));				
				
				addAuthorButton.setText(BpathMainFrame.messages.getString("COMMON.ADD"));
				
				addAuthorButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {

						PersonEditor editor = new PersonEditor(thisDialog);
						
						Rectangle p=parent.getBounds();
						int w=250,h=150;
						
						editor.setBounds(p.x+(p.width-w)/2,p.y+(p.height-h)/2,w,h);
						editor.setModal(true);
						editor.setVisible(true);
						if(
								(editor.isCompleted())&&
								((""+editor.getFamnameField()+editor.getFirstnameField()).length()>0)
						)
						addAuthor(editor.getFamnameField()+";"+editor.getFirstnameField());
					}
				});
				
				// Button: remove authors(2,5,1,1)					
				
				contentPanel.add(removeAuthorButton, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));				
				
				removeAuthorButton.setText(BpathMainFrame.messages.getString("COMMON.REMOVE"));
				removeAuthorButton.setEnabled(false);
				
				removeAuthorButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						authorModel.remove(authorsList.getSelectedIndex());
					}
				});
				
				// JList: authors list (0,6,4,2)					
												
				authorsList.setLayoutOrientation(JList.VERTICAL);
				JScrollPane listScroller = new JScrollPane(authorsList);
				listScroller.setPreferredSize(new Dimension(thisDialog.getWidth(),50));
				
				contentPanel.add(listScroller, new GridBagConstraints(0, 6, 4, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 5, 5), 0, 0));		
				
				authorsList.addListSelectionListener(new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						removeAuthorButton.setEnabled(authorsList.getSelectedIndex()>=0);
					}
				});

				
				// TODO: created, last modified fields
				
			}
			dialogPane.add(contentPanel, BorderLayout.NORTH);
			
			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};
				
				//---- okButton ----
				okButton.setText(BpathMainFrame.messages.getString("COMMON.OK"));
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
		
		initFields();
	}
	
	
	private void addAuthor(String name){
		authorModel.addElement(name);
	}
	
	private void okButtonActionPerformed(ActionEvent e) {
	
		if (pathnameField.getText().length()==0){
			JOptionPane.showMessageDialog(parent,BpathMainFrame.messages.getString("DIALOG.NEWPATHWAY.ERRORNAME"));
			return;
		}		

		if (authorModel.getSize()==0){
			JOptionPane.showMessageDialog(parent,BpathMainFrame.messages.getString("DIALOG.NEWPATHWAY.AUTHORSERROR"));
			return;
		}		

		completed=true;

		this.setVisible(false);			
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		this.dispose();
	}

	public boolean isCompleted() {
		return completed;
	}
	
	public String getPathTitle(){
		return pathnameField.getText();
	}
	
	public String getTeacher(){
		if (teacherBox.isEnabled()) {
			return info.getTeachers()[teacherBox.getSelectedIndex()];
		}
		else return null;
	}

	public String getTopic(){
		if (topicBox.isEnabled()) return topicBox.getSelectedItem().toString();
		else return null;
	}
	
	public String getGroup(){
		if (groupBox.isEnabled()) return groupBox.getSelectedItem().toString();
		else return null;
	}
	
	public Template[] getTemplates(){
		
		int templateIndex=templateBox.getSelectedIndex();
		Template[] templates=new Template[1];
		
		TemplateInfo[] templatesInfo=info.getTemplates();
		templates[0]=new Template(templatesInfo[templateIndex].getUrl());
		
		return templates;		
	}
	
	public String[] getAuthors(){
		
		String[] tab=new String[authorModel.size()];
		Enumeration en=authorModel.elements();
		int i=0;
		
		while (en.hasMoreElements()) {
			String s = (String) en.nextElement();
			tab[i++]=s;
		}		
		
		return tab; 
	}
}
