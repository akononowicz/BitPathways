/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.PathwayFormatException;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.MetadataEntity;

public class LoadPathDialog extends JDialog {
	
	JTable table;
	boolean selected=false;
	String selectedPid="";
	String selectedName="";
	JDialog dialog=this;
	LoadPathModel pathListModel;
	
	public LoadPathDialog(Frame owner,Hashtable<Metadata,String> metadata) throws PathwayFormatException{
		
		super(owner,BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.TITLE"),true);
		
		JLabel infoTextLabel=new JLabel(BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.SELECTDSCR"));
		JButton selectButton=new JButton(BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.SELECT"));
		JButton cancelButton=new JButton(BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.CANCEL"));
		table=new JTable();
		JPanel southPanel=new JPanel();
		Container mainPanel=getContentPane();
		
		mainPanel.setLayout(new BorderLayout());
		
		mainPanel.add(infoTextLabel,BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(table),BorderLayout.CENTER);
		mainPanel.add(southPanel,BorderLayout.SOUTH); 
		
		JPanel buttonPanel=new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.add(buttonPanel,BorderLayout.EAST);
		
		FlowLayout flowLayout=new FlowLayout();
		buttonPanel.setLayout(flowLayout);
		
		buttonPanel.add(selectButton);
		buttonPanel.add(cancelButton);
		
		pathListModel=new LoadPathModel(metadata);
		table.setModel(pathListModel);
		
		selectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int s_row=table.getSelectedRow();
				if(s_row==-1) {
					JOptionPane.showMessageDialog(
							dialog, 
							BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.SELECTDSCR"), 
							BpathMainFrame.messages.getString("ERROR"), 
							JOptionPane.ERROR_MESSAGE); 
					return;
				}
				
				selected=true;
				
				selectedPid=pathListModel.getPIDforRow(s_row); 
				selectedName=pathListModel.getValueAt(s_row, 1).toString();
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selected=false;
				setVisible(false);
			}
		});
		
		Rectangle p=owner.getBounds();
		int w=500,h=250;
		
		setBounds(p.x+p.width/2-w/2, p.y+p.height/2-h/2, w, h);
		table.getColumnModel().getColumn(0).setPreferredWidth(125);
		table.getColumnModel().getColumn(1).setPreferredWidth(275);
	}
	
	
	
	public boolean isSelected(){
		return selected;
	}
	
	public String getPathId(){
		return selectedPid;
	}
	
	public String getPathName(){
		return selectedName;
	}
	
	
	
	private class LoadPathModel extends AbstractTableModel{

		  Metadata[] metadata_data;
		  Hashtable<Metadata,String> metadata_ids;
		
		/**
		 * @param xml XML with a list of paths. 
		 * Obtained by the getPathList WebService
		 * 
		 * @author Andrzej Kononowicz
		 */

		  public LoadPathModel(Hashtable<Metadata,String> data){
			  	  
			  metadata_ids=data;
			  metadata_data=new Metadata[data.size()];
			  
			  Enumeration<Metadata> ms=data.keys();
			  
			  int i=0;
			  
			  while (ms.hasMoreElements()) {
				metadata_data[i]=ms.nextElement();
				i++;
			  }
			  Arrays.sort(metadata_data);
		  }
		
		  public int getRowCount(){
			  return metadata_data.length;
		  }
		  
		  public int getColumnCount(){
			  return 2;
		  }
		  
		  public String getColumnName(int column){
			  if (column==0) return BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.COLAUTHORS");
			  else return BpathMainFrame.messages.getString("DIALOG.LOADPATHWAY.COLNAME");
		  }
		  
		  public Object getValueAt(int row, int column){
			  
			  Metadata m=metadata_data[row];
			  
			  if (column==0){				  
				  MetadataEntity[] entities=m.get("authors");
				  
				  String authors="";
				  
				  for (int i = 0; i < entities.length; i++) {
					authors+=entities[i].getName();
					if (i!=(entities.length-1)) authors+=" | ";
				  }
				  
				  return authors;  
			  }
			  return m.get("title")[0].getName(); 
		  }
		  
		  public String getPIDforRow(int row){
			  return metadata_ids.get(metadata_data[row]);
		  }
	}
	
}
