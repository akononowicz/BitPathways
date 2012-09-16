/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Hashtable;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.MetadataEntity;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;
import edu.uj.zbit.bpath.editor.*;
import edu.uj.zbit.bpath.editor.dialogs.NewTaskWizard;
import edu.uj.zbit.bpath.editor.dialogs.MetadataWizard;


import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class NewPathCommmand extends AbstractAction {
	
	public static String NAME=BpathMainFrame.messages.getString("ACTION.NEWPATH");
	
	private PathModel model;
	private JFrame parent;
	
	public NewPathCommmand(PathModel _model,JFrame _parent){
		super(NAME);
		model=_model;
		parent=_parent;
	}
	
	public void actionPerformed(ActionEvent e){
		
		
		String id="";
		if (!BpathMainFrame.noserver){
			id=ExistConnection.createNewResource();
			if (id==null){
				JOptionPane.showMessageDialog(parent, BpathMainFrame.messages.getString("ERROR.XMLSERVER"), BpathMainFrame.messages.getString("ERROR"), JOptionPane.ERROR_MESSAGE);	
				return;
			}
		}else{
			id="tmp"+Math.random();
		}
		
		BpathMainFrame mainFrame=(BpathMainFrame)parent;
				
		MetadataWizard dialog=new MetadataWizard(mainFrame, MetadataWizard.MODE_NEW_PATH);
		dialog.setVisible(true);
				
		
		if (dialog.isCompleted()){
			
			Metadata metadata=new Metadata();
			
			String username=mainFrame.getUser().getFamname()+";"+mainFrame.getUser().getFirstname();
			
			Date date=new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateStr=sdf.format(date);
			
			// metadata 1. created
			
			MetadataEntity entity=new MetadataEntity();
			entity.setName(username);
			entity.setDate(dateStr);
			entity.setId(""+mainFrame.getUser().getUid());
			MetadataEntity[] entities=new MetadataEntity[1];
			entities[0]=entity;
			metadata.put("created", entities);
			
			// metadata 2. last_modified
			entity=new MetadataEntity();
			entity.setName(username);
			entity.setDate(dateStr);
			entities=new MetadataEntity[1];
			entities[0]=entity;			
			metadata.put("last_modified", entities);		

			// metadata 8. access rights 			
			entity=new MetadataEntity();
			entity.setId(""+mainFrame.getUser().getUid());
			entity.setName(username);
			entities=new MetadataEntity[1];
			entities[0]=entity;			
			metadata.put("access_rights", entities);
			
			updateMetadata(dialog,metadata);
			
			model.newModel(id,metadata,dialog.getTemplates());
		}		
	}
	
	
	public static void updateMetadata(MetadataWizard dialog,Metadata metadata){
			
		MetadataEntity entity;
		MetadataEntity[] entities;
		
			// metadata 3. teacher
			if(dialog.getTeacher()!=null){
				metadata.remove("teacher");
				entity=new MetadataEntity();
				entity.setName(dialog.getTeacher());
				entities=new MetadataEntity[1];
				entities[0]=entity;		
				metadata.put("teacher", entities);		
			}
			
			// metadata 4. group			
			if(dialog.getGroup()!=null){
				metadata.remove("group");
				entity=new MetadataEntity();
				entity.setName(dialog.getGroup());
				entities=new MetadataEntity[1];
				entities[0]=entity;				
				metadata.put("group", entities);		
			}
			
			// metadata 5. topic
			if(dialog.getTopic()!=null){
				metadata.remove("topic");
				entity=new MetadataEntity();
				entity.setName(dialog.getTopic());
				entities=new MetadataEntity[1];
				entities[0]=entity;				
				metadata.put("topic", entities);		
			}
			
			// metadata 6. authors
			
			metadata.remove("authors");
			String[] authors=dialog.getAuthors();
			
			entities=new MetadataEntity[authors.length];
			
			for (int i = 0; i < authors.length; i++) {
				entity=new MetadataEntity();
				entity.setName(authors[i]);
				entities[i]=entity;
			}
						
			metadata.put("authors", entities);
			
			// metadata 7. title		
			metadata.remove("title");
			entity=new MetadataEntity();
			entity.setName(dialog.getPathTitle());
			entities=new MetadataEntity[1];
			entities[0]=entity;			
			metadata.put("title", entities);
			
			
			
			// ---
	}
}
