/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.ServerConfig;
import edu.uj.zbit.bpath.editor.model.User;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;


/**
 * Asks the user to enter his/hers username and password.
 * Checks the password on the server
 * If everything is fine returns uid of the user. Otherwise returns -1;
 * 
 * @author Andrzej Kononowicz
 */

public class UserLoginDialog extends JDialog {
	
	Frame parent;
	boolean completed=false;
	
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel label1c1;
	private JLabel label1c2;
	private JLabel label2;
	private JTextField usernameField;
	private JLabel label3;
	private JPasswordField passField;
	private JLabel label4;
	private JComboBox serverBox;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private JLabel iconLabel; 
	
	private ServerConfig[] serverConfig;
	
	private User user=null;
	private String errorMessage="";
	

	public UserLoginDialog(Frame parent) {
		super(parent,true);
		this.parent=parent;
		initComponents();
		
		int w=300,h=320;
		this.setBounds(0+(1024-w)/2,0+(768-h)/2,w,h);
		setForeground(Color.white);
			
		this.setResizable(false);
	}

	public UserLoginDialog(Dialog owner) {
		super(owner);
		setBackground(Color.white);
		initComponents();
	}

	private void initComponents() {

		// load Icon
		
		Image image = null;
	    try {
	        image = ImageIO.read(getClass().getResource("/ico/logo.png"));    
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
	    iconLabel = new JLabel(new ImageIcon(image));
		
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		label1c1 = new JLabel();
		label1c2 = new JLabel();
		label2 = new JLabel();
		usernameField = new JTextField("");
		label3 = new JLabel();
		passField = new JPasswordField("");
		
		label4 = new JLabel();
		
		serverConfig = parseServerConf(); 
		
		String[] serverNames=new String[serverConfig.length];
		
		for (int i = 0; i < serverNames.length; i++) {
			serverNames[i]=serverConfig[i].getLabel();
		}
		
		serverBox = new JComboBox(serverNames);
		
		serverBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		
		label4 = new JLabel();
		
		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.LOGIN.TITLE"));
		setBackground(Color.white);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
			
		//======== dialogPane ========
		{
			
			LineBorder border=new LineBorder(Color.white,12);

			dialogPane.setBorder(border);
			dialogPane.setBackground(Color.white);
			
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{	contentPanel.setBackground(Color.white);
				

				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.4, 0.0, 0.0, 1.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
				
				
				//---- icon label ----
				
				contentPanel.add(iconLabel, new GridBagConstraints(0, 0, 6, 1
						, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- label1 ----
				
				String version="";
				try{
		    		version=BpathMainFrame.version;
		    	}catch(Exception e){
		    		e.printStackTrace();
		    	}
				
				label1.setText(
						BpathMainFrame.messages.getString("DIALOG.LOGIN.CREDIT")
						+"  (v."+version+")"
				);			
				label1.setHorizontalAlignment(SwingConstants.CENTER);			
				
				contentPanel.add(label1, new GridBagConstraints(0, 1, 6, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));

				//---- label1c1 ----
				label1c1.setText(BpathMainFrame.messages.getString("DIALOG.LOGIN.CREDIT2"));
				contentPanel.add(label1c1, new GridBagConstraints(0, 2, 6, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));				
				label1c1.setHorizontalAlignment(SwingConstants.CENTER);

				//---- label1c2 ----
				label1c2.setText(" ");
				contentPanel.add(label1c2, new GridBagConstraints(0, 3, 6, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));		
				
				
				//---- label2 ----
				label2.setText(BpathMainFrame.messages.getString("DIALOG.LOGIN.USERNAME"));
				contentPanel.add(label2, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(usernameField, new GridBagConstraints(1, 4, 5, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- label3 ----
				label3.setText(BpathMainFrame.messages.getString("DIALOG.LOGIN.PASS"));
				contentPanel.add(label3, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				contentPanel.add(passField, new GridBagConstraints(1, 5, 5, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				
				//---- label4 ----
				label4.setText(BpathMainFrame.messages.getString("DIALOG.LOGIN.SERVER"));
				contentPanel.add(label4, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
				contentPanel.add(serverBox, new GridBagConstraints(1, 6, 5, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
			}
			dialogPane.add(contentPanel, BorderLayout.NORTH);
			
			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setBackground(Color.white);
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80, 80,40};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0,0.0};
				
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
				
				buttonBar.add(new JLabel(" "), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
				
	}

	private void okButtonActionPerformed(ActionEvent e) {
		try{
			BpathMainFrame.server=serverConfig[serverBox.getSelectedIndex()];
			ExistConnection.init();
			user=ExistConnection.authorizeUser(usernameField.getText().trim(), new String(passField.getPassword()));
			
		}catch(Exception err){
			errorMessage=err.getMessage();
		}
		completed=true;
		this.setVisible(false);			
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		completed=false;
		this.setVisible(false);	
	}




	public boolean isCompleted() {
		return completed;
	}
	
	
	private ServerConfig[] parseServerConf(){
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("conf/conf.xml"));
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("server");
			ServerConfig[] servTab=new ServerConfig[nList.getLength()];
			
			for (int i = 0; i < servTab.length; i++) {
				Element servElement=(Element) nList.item(i);
				String label=servElement.getElementsByTagName("label").item(0).getTextContent();
				String address=servElement.getElementsByTagName("address").item(0).getTextContent();
				String port=servElement.getElementsByTagName("port").item(0).getTextContent();
				
				servTab[i]=new ServerConfig(label, address, port);
				
				
				try {
					String db_name=servElement.getElementsByTagName("db_name").item(0).getTextContent();
					String db_type=servElement.getElementsByTagName("db_type").item(0).getTextContent();
					String db_user=servElement.getElementsByTagName("db_user").item(0).getTextContent();
					String db_pass=servElement.getElementsByTagName("db_pass").item(0).getTextContent();					
				} catch (Exception e) {}
			}
			
			return servTab;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	public String getUsername(){
		return usernameField.getText();
	}

	public User getUser() {
		return user;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
