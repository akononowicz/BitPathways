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
public class TestQuestionEditor extends JDialog {
	
	private TestQuestion question;
	private Frame owner;
	private String commentStr1="",commentStr2="",commentStr3="",commentStr4="";

	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label3;
	private JTextField nameTextField;
	private JLabel label1;
	private JScrollPane scrollPane6;
	private JTextArea questionTextArea;
	private JLabel label2;
	private JCheckBox checkBox1;
	private JScrollPane scrollPane2;
	private JTextArea a1TextArea;
	private JButton commentButton1;
	private JCheckBox checkBox2;
	private JScrollPane scrollPane3;
	private JTextArea a2TextArea;
	private JButton commentButton2;
	private JCheckBox checkBox3;
	private JScrollPane scrollPane7;
	private JTextArea a3TextArea;
	private JButton commentButton3;
	private JCheckBox checkBox4;
	private JScrollPane scrollPane5;
	private JTextArea a4TextArea;
	private JButton commentButton4;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private boolean completed=false;

	
	
	public TestQuestionEditor(Frame owner,TestQuestion _question) {
		super(owner);
		this.owner=owner;
		initComponents();
		question=_question;
		
		if (question!=null){
			nameTextField.setText(question.getName());
			questionTextArea.setText(question.getQuestionText());
		
			//1
			a1TextArea.setText(question.getAnswerText(0));
			checkBox1.setSelected(question.isAnswerCorrect(0));
			commentStr1=question.getAnswerComment(0);
			
			//2
			a2TextArea.setText(question.getAnswerText(1));
			checkBox2.setSelected(question.isAnswerCorrect(1));
			commentStr2=question.getAnswerComment(1);
			
			//3
			a3TextArea.setText(question.getAnswerText(2));
			checkBox3.setSelected(question.isAnswerCorrect(2));
			commentStr3=question.getAnswerComment(2);
			
			//4
			a4TextArea.setText(question.getAnswerText(3));
			checkBox4.setSelected(question.isAnswerCorrect(3));
			commentStr4=question.getAnswerComment(3);
			
		}else{
			question=new TestQuestion();
		}
		
		
		Rectangle pd=owner.getBounds();
		
		setBounds(pd.x+(int)(0.5*pd.width)-265,
				  pd.y+(int)(0.5*pd.height)-215,
				  530,430
		);
	}

	private void initComponents() {
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label3 = new JLabel();
		nameTextField = new JTextField();
		label1 = new JLabel();
		scrollPane6 = new JScrollPane();
		questionTextArea = new JTextArea();
		label2 = new JLabel();
		checkBox1 = new JCheckBox();
		scrollPane2 = new JScrollPane();
		a1TextArea = new JTextArea();
		commentButton1 = new JButton();
		checkBox2 = new JCheckBox();
		scrollPane3 = new JScrollPane();
		a2TextArea = new JTextArea();
		commentButton2 = new JButton();
		checkBox3 = new JCheckBox();
		scrollPane7 = new JScrollPane();
		a3TextArea = new JTextArea();
		commentButton3 = new JButton();
		checkBox4 = new JCheckBox();
		scrollPane5 = new JScrollPane();
		a4TextArea = new JTextArea();
		commentButton4 = new JButton();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setModal(true);
		setTitle(BpathMainFrame.messages.getString("DIALOG.MCQ.TITLE"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setLayout(new BorderLayout());
			
			//======== contentPanel ========
			{
				contentPanel.setLayout(new GridBagLayout());
				((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
				((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0E-4};
				((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
				
				//---- label3 ----
				label3.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.ID"));
				contentPanel.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				contentPanel.add(nameTextField, new GridBagConstraints(1, 0, 13, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label1 ----
				label1.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.QUESTION"));
				contentPanel.add(label1, new GridBagConstraints(0, 1, 14, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//======== scrollPane6 ========
				{
					scrollPane6.setViewportView(questionTextArea);
				}
				contentPanel.add(scrollPane6, new GridBagConstraints(0, 2, 14, 3, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- label2 ----
				label2.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.QUESTION"));
				contentPanel.add(label2, new GridBagConstraints(0, 5, 14, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- checkBox1 ----
				checkBox1.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.CORRECT"));
				contentPanel.add(checkBox1, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//======== scrollPane2 ========
				{
					scrollPane2.setViewportView(a1TextArea);
				}
				contentPanel.add(scrollPane2, new GridBagConstraints(1, 6, 12, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- commentButton1 ----
				commentButton1.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.FEEDBACK"));
				commentButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commentButton1ActionPerformed(e);
					}
				});
				contentPanel.add(commentButton1, new GridBagConstraints(13, 6, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
				
				//---- checkBox2 ----
				checkBox2.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.CORRECT"));
				contentPanel.add(checkBox2, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//======== scrollPane3 ========
				{
					scrollPane3.setViewportView(a2TextArea);
				}
				contentPanel.add(scrollPane3, new GridBagConstraints(1, 8, 12, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- commentButton2 ----
				/*commentButton2.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.FEEDBACK"));
				commentButton2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commentButton2ActionPerformed(e);
					}
				});
				contentPanel.add(commentButton2, new GridBagConstraints(13, 8, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));*/
				
				//---- checkBox3 ----
				checkBox3.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.CORRECT"));
				contentPanel.add(checkBox3, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//======== scrollPane7 ========
				{
					scrollPane7.setViewportView(a3TextArea);
				}
				contentPanel.add(scrollPane7, new GridBagConstraints(1, 10, 12, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- commentButton3 ----
				/*commentButton3.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.FEEDBACK"));
				commentButton3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commentButton3ActionPerformed(e);
					}
				});
				contentPanel.add(commentButton3, new GridBagConstraints(13, 10, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));*/
				
				//---- checkBox4 ----
				checkBox4.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.CORRECT"));
				contentPanel.add(checkBox4, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//======== scrollPane5 ========
				{
					scrollPane5.setViewportView(a4TextArea);
				}
				contentPanel.add(scrollPane5, new GridBagConstraints(1, 12, 12, 2, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
				
				//---- commentButton4 ----
			/*	commentButton4.setText(BpathMainFrame.messages.getString("DIALOG.MCQ.FEEDBACK"));
				commentButton4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						commentButton4ActionPerformed(e);
					}
				});
				contentPanel.add(commentButton4, new GridBagConstraints(13, 12, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));*/
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
			
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
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 5), 0, 0));
				
				//---- cancelButton ----
				cancelButton.setText(BpathMainFrame.messages.getString("COMMON.CANCEL"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
					new Insets(0, 0, 0, 0), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		
	}

	private void okButtonActionPerformed(ActionEvent e) {
		
		// check if enough data
		if (nameTextField.getText().trim().length()==0){
			JOptionPane.showMessageDialog(owner,BpathMainFrame.messages.getString("DIALOG.MCQ.IDNOTEMPTY"),BpathMainFrame.messages.getString("DIALOG.MCQ.MISSINGDATA"),JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (questionTextArea.getText().trim().length()==0){
			JOptionPane.showMessageDialog(owner,BpathMainFrame.messages.getString("DIALOG.MCQ.NOEMPTYQUESTION"),BpathMainFrame.messages.getString("DIALOG.MCQ.MISSINGDATA"),JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JCheckBox[] boxes={checkBox1,checkBox2,checkBox3,checkBox4};
		
		boolean checked=false;
		
		for (int i = 0; i < boxes.length; i++) {
			JCheckBox box = boxes[i];
			if (box.isSelected()){
				checked=true;
				break;
			}
		}
		
		if(!checked){
			JOptionPane.showMessageDialog(owner,BpathMainFrame.messages.getString("DIALOG.MCQ.CORRECTMIN1"),BpathMainFrame.messages.getString("DIALOG.MCQ.MISSINGDATA"),JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// create TestQuestion object
		
		if (question==null){
			question=new TestQuestion();
		}
		
		question.setName(nameTextField.getText());
		question.setQuestionText(questionTextArea.getText());
		question.setAnswer(0, checkBox1.isSelected(), a1TextArea.getText(), commentStr1);
		question.setAnswer(1, checkBox2.isSelected(), a2TextArea.getText(), commentStr2);
		question.setAnswer(2, checkBox3.isSelected(), a3TextArea.getText(), commentStr3);
		question.setAnswer(3, checkBox4.isSelected(), a4TextArea.getText(), commentStr4);
		
		completed=true;
		this.dispose();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		completed=false;
		this.dispose();
	}

	private void commentButton1ActionPerformed(ActionEvent e) {
				
		TestQuestionFeedbackEditor f=new TestQuestionFeedbackEditor(owner, commentStr1);
		f.setModal(true);
		f.setVisible(true);
		commentStr1=f.textArea.getText();
		commentStr2=commentStr1;
		commentStr3=commentStr1;
		commentStr4=commentStr1;
	}

	private void commentButton2ActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void commentButton3ActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void commentButton4ActionPerformed(ActionEvent e) {
		// TODO add your code here
	}


	
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public TestQuestion getQuestion() {
		return question;
	}
	
}
