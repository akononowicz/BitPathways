/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.converter.mvp.MVP_AM_Factory;
import edu.uj.zbit.bpath.editor.model.converter.mvp.MVP_DAM_Factory;
import edu.uj.zbit.bpath.editor.model.converter.mvp.MVP_LOM_Factory;
import edu.uj.zbit.bpath.editor.model.converter.mvp.MVP_VPD_Factory;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.helper.xslt.FileManager;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exist.xquery.functions.util.FileRead;
import org.w3c.dom.Document;

/**
 * Saves selected nodes into a linear virtual patient in MVP (MedBiquitous Virtual Patient) format. 
 * 
 * @author Andrzej Kononowicz, 2009,2011
 */

public class SaveAsToMVPAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;
	private DataFlowCanvas canvas;
	
	public SaveAsToMVPAction(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.MVPSAVE"));
		model=_model;
		parent=_parent;
		canvas=_canvas;
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public SaveAsToMVPAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){
			
			
			String template=model.getDefaultTemplate().getId();
			
			if(!template.equalsIgnoreCase("t_vp_v001")){
				
				JOptionPane.showMessageDialog(parent,BpathMainFrame.messages.getString("ERROR.WRONGTEMPLATE.VIRUALPATIENT"));
				
				return;
			}
						
			chooser.showSaveDialog(parent);
			File dir=chooser.getSelectedFile();
			if (dir==null) return; 
			
			FlowChartNode[] nodes;
			DataFlowEdge[] edges;
			
			if (model.getSelectedVerticesCount()<1){
				nodes=model.getAllVertices();
				edges=model.getAllEdges();
			}else{
				// if selected part just of the pathway take only those edges that link selected nodes 
				nodes=model.getSelectedVertices();
				
				ArrayList<FlowChartNode> nodeList=new ArrayList<FlowChartNode>();
				nodeList.addAll(Arrays.asList(nodes));
											
				DataFlowEdge[] allEdges=model.getAllEdges();
				
				int includedEdgesCount=allEdges.length;
				
				for (int i = 0; i < allEdges.length; i++) {
					DataFlowEdge edge=allEdges[i];
					if (!(nodeList.contains(edge.getScrVertex())&&
							nodeList.contains(edge.getDestVertex()))
						){
						allEdges[i]=null;
						includedEdgesCount--;
					}
				}
				
				edges=new DataFlowEdge[includedEdgesCount];
				int j=0;
				
				for (int i = 0; i < allEdges.length; i++) {
					if (allEdges[i]!=null){
						
						edges[j]=allEdges[i];
						j++;
					}
				}
				
			}
			
			// (reverse order of nodes)
			/*DataFlowNode[] revNodes=new DataFlowNode[nodes.length];			
			for (int i = 0; i < revNodes.length; i++) revNodes[i]=nodes[revNodes.length-1-i];
			nodes=revNodes;*/
			
			// --------- SAVE to MVP-format -------------
			
			Document vpdDoc=MVP_VPD_Factory.createVirtualPatientData(nodes);
			Document damDoc=MVP_DAM_Factory.createDataAvaiablityModel(nodes);			
			Document amDoc=MVP_AM_Factory.createActivityModel(nodes,edges);	
			Document lomDoc=MVP_LOM_Factory.createMetadata(model.getPathId(),model.getMetadata(),model.getPathAttributes());	
						
			// Copy generic MVP file into target folder
			
			unzipToTargetDirectory("/resources/mvp_generic.zip",dir.getAbsolutePath());
	        
			// write MVP files into target directory
			
			String dirStr=dir.getAbsolutePath()+"/tmp/";
			
			writeXMLfile(vpdDoc,dirStr+"virtualpatientdata.xml");
			writeXMLfile(damDoc,dirStr+"dataavailabilitymodel.xml");
			writeXMLfile(amDoc,dirStr+"activitymodel.xml");			
			writeXMLfile(lomDoc,dirStr+"metadata.xml");			
			 
			
			
			// Zip target folder
			
			try {
				ZipOutputStream zipout = new ZipOutputStream(
						new BufferedOutputStream(new FileOutputStream(dir.getAbsolutePath()+"/"+model.getPathId()+".zip")));
				File tmpFolder=new File(dir.getAbsolutePath()+"/tmp/");

				zipFolder(zipout,tmpFolder,"");
			    zipout.flush();
			    zipout.close(); 
			    
			     // Delete temporary folder
			    deleteDir(tmpFolder);
			} catch (IOException ie) {ie.printStackTrace();}
			
			System.out.println("MVP export completed");
			
		}
	}

	private void writeXMLfile(Document doc,String filename){
		
		try {
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			//initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			String xmlString = result.getWriter().toString();
			
			BufferedWriter bout=new BufferedWriter(new FileWriter(filename));
			bout.write(killPolishCharacters(xmlString));
			bout.close();
			 			 
		 } catch (Exception e) {
			 System.out.println("Error");
			 e.printStackTrace();
		 } 
	}
	
	private String killPolishCharacters(String xmlString){
		
		String[] polish={"¹","æ","ê","³","ñ","ó","œ","Ÿ","¿","¥","Æ","Ê","£","Ñ","Ó","Œ","","¯","–","–"};
		String[] safe={"&amp;#261;","&amp;#263;","&amp;#281;","&amp;#322;","&amp;#324;","&amp;#243;","&amp;#347;","&amp;#378;","&amp;#380;","&amp;#260;","&amp;#262;","&amp;#280;","&amp;#321;","&amp;#323;","&amp;#211;","&amp;#346;","&amp;#377;","&amp;#379;","-","-"};
		
		for (int i = 0; i < safe.length; i++) {
			xmlString=xmlString.replaceAll(polish[i], safe[i]);	
		}

		return xmlString;
	}
	
	private void zipFolder(ZipOutputStream zipout, File folder,String path)throws IOException{
	    
	     BufferedInputStream in = null;
	     byte[] data    = new byte[1000];
	     File[] files = folder.listFiles();
	     
	     for (int i=0; i<files.length; i++){
	    	 File newfile=files[i]; 
	    	 
	    	 if (newfile.isFile()){
			      in = new BufferedInputStream(new FileInputStream(files[i]), 1000);                  
			      zipout.putNextEntry(new ZipEntry(path+files[i].getName())); 
			      int count;
			      while((count = in.read(data,0,1000)) != -1){
		           zipout.write(data, 0, count);
		          }
			      zipout.closeEntry();
	    	 }else{
	    		zipFolder(zipout, newfile,path+newfile.getName()+"/");
	    	 }
	      }
	}
	
	//-----------------------------------------------------------------------------------------------
	
	private void unzipToTargetDirectory(String zipFile,String targetDir){
	    try {
	        // Open the ZIP file
	        ZipInputStream in = new ZipInputStream(SaveAsToMVPAction.class.getResourceAsStream(zipFile));
	    
	        ZipEntry entry=null;
	        
	        // Create directory structure
	        while( (entry = in.getNextEntry())!=null){
		        // Open the output file
	        	String outFilename=entry.getName();
		        
		        if (entry.isDirectory()){
		        	try{
		        		new File(targetDir+"/tmp/"+outFilename).mkdirs();
		        	}catch(Exception e){
		        		e.printStackTrace();
		        	}
		        }else{
		        	OutputStream out = new FileOutputStream(targetDir+"/tmp/"+outFilename);
				    
			        // Transfer bytes from the ZIP file to the output file
			        byte[] buf = new byte[1024];
			        int len;
			        while ((len = in.read(buf)) > 0) {
			            out.write(buf, 0, len);
			        }
			        out.close();    	
		        }
		    }
	    
	        // Close the streams
	
	        in.close();
	    } catch (IOException e) {e.printStackTrace(); }
		
	}

	
	//-----------------------------------------------------------------------------------------------
	 // Deletes all files and subdirectories under dir.
    // Returns true if all deletions were successful.
    // If a deletion fails, the method stops attempting to delete and returns false.
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    } 
	
}
