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
public class DurationEditor extends JDialog {
	
	private boolean completed=false;
	private String durationValue="";
	
	public DurationEditor(Frame owner,String durationStr) {
		super(owner);
		initComponents();

	
		int year=readDurationParam(durationStr, 'P','Y');
		int month=readDurationParam(durationStr, 'P','M');
		int day=readDurationParam(durationStr, 'P','D');
		
		int hour=readDurationParam(durationStr, 'T','H');
		int minute=readDurationParam(durationStr, 'T','M');
		int second=readDurationParam(durationStr, 'T','S');
		
		if (year>-1) yearField.setText(""+year);
		if (month>-1) monthField.setText(""+month);
		if (day>-1) dayField.setText(""+day);
		if (hour>-1) hourField.setText(""+hour);
		if (minute>-1) minuteField.setText(""+minute);
		if (second>-1) secondField.setText(""+second);
		
		Rectangle pd=owner.getBounds();
		
		setBounds(pd.x+(int)(0.5*pd.width)-200,
				  pd.y+(int)(0.5*pd.height)-75,
				  240,240
		);
	
	}

	private int readDurationParam(String word, char group, char param){
		
		word=word.toUpperCase();
		
		if (group=='T') {
			if (word.indexOf('T')>0){
				word=word.substring((word.indexOf('T')));	
			}else{
				word="";
			}
		}else{
			if (word.indexOf('T')>0){
				word=word.substring(0,(word.indexOf('T')));	
			}
		}
		
		int i=word.indexOf(""+param);
		
		if (i<1) return -1;
		
		char[] paramTab=word.toCharArray();
		
		String value1=""+paramTab[i-1];
		String value0="";
		
		try{
			value0=""+Integer.parseInt(""+paramTab[i-2]);
		}catch(Exception e){}
		
		String value=value0+value1;
		
		return Integer.parseInt(value);
		
	}
	
	public DurationEditor(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		
		yearLabel=new JLabel();
		monthLabel=new JLabel();
		dayLabel=new JLabel();
		hourLabel=new JLabel();
		minuteLabel=new JLabel();
		secondLabel=new JLabel();
		
		yearField=new JTextField();
		monthField=new JTextField();
		dayField=new JTextField();
		hourField=new JTextField();
		minuteField=new JTextField();
		secondField=new JTextField();
		
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle(BpathMainFrame.messages.getString("DIALOG.DURATION.TITLE"));
		setResizable(false);
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));			
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {40, 60};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
				
				//---- label1 (Year) ----
				yearLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.YEAR"));
				contentPanel.add(yearLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(yearField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label2 (Month) ----
				monthLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.MONTH"));
				contentPanel.add(monthLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(monthField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label3 (Day) ----
				dayLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.DAY"));
				contentPanel.add(dayLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(dayField, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label4 (Hour) ----
				hourLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.HOUR"));
				contentPanel.add(hourLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(hourField, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label5 (Minute) ----
				minuteLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.MINUTE"));
				contentPanel.add(minuteLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(minuteField, new GridBagConstraints(1, 4,2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label6 (Second) ----
				secondLabel.setText(BpathMainFrame.messages.getString("DIALOG.DURATION.SECOND"));
				contentPanel.add(secondLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(secondField, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
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
		

	}

	private void okButtonActionPerformed(ActionEvent e) {
		
		completed=false;
		String localvalue="P";

		//--- parse Year --- 
		String text=yearField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>100)) throw new Exception();
				localvalue+=v+"Y";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.YEAR"));
				return;
			}
		}
		
		//--- parse Month --- 
		text=monthField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>11)) throw new Exception();
				localvalue+=v+"M";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.MONTH"));
				return;
			}
		}

		//--- parse Day --- 
		text=dayField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>31)) throw new Exception();
				localvalue+=v+"D";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.DAY"));
				return;
			}
		}
		
		if ((hourField.getText().trim().length()>0)||
			(minuteField.getText().trim().length()>0)||
			(secondField.getText().trim().length()>0)
				){
			localvalue+="T";
		}
				
		//--- parse Hour --- 
		text=hourField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>23)) throw new Exception();
				localvalue+=v+"H";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.HOUR"));
				return;
			}
		}
		
		//--- parse Minute --- 
		text=minuteField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>59)) throw new Exception();
				localvalue+=v+"M";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.MINUTE"));
				return;
			}
		}
		
		//--- parse Second --- 
		text=secondField.getText().trim();		
		if(text.length()>0){			
			try{
				int v=Integer.parseInt(text);
				if ((v<0)||(v>59)) throw new Exception();
				localvalue+=v+"S";
			}catch(Exception err){
				JOptionPane.showMessageDialog(getOwner(), BpathMainFrame.messages.getString("DIALOG.DURATION.ERROR.SECOND"));
				return;
			}
		}

		
		durationValue=localvalue;
		completed=true;
		setVisible(false);

	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		completed=false;
		setVisible(false);
	}

	
	private JPanel dialogPane;
	private JPanel contentPanel;
	
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;

	
	private JLabel yearLabel,monthLabel,dayLabel,hourLabel,minuteLabel,secondLabel;
	private JTextField yearField,monthField,dayField,hourField,minuteField,secondField;	
	

	
	public boolean isCompleted() {
		return completed;
	}
	
	public String getDuration(){
		return durationValue;
	}
}
