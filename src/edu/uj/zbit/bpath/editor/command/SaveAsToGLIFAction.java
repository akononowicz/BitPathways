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
import edu.uj.zbit.bpath.editor.model.converter.glif.GLIF_Factory;
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
import java.io.PrintStream;
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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.exist.xquery.functions.util.FileRead;
import org.w3c.dom.Document;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Convertes file into GLIF format
 * 
 * @author Andrzej Kononowicz, 2011
 */

public class SaveAsToGLIFAction extends AbstractAction  {
	
	private PathModel model;
	private JFrame parent;
	private DataFlowEnd newEndElement;
	private JFileChooser chooser;
	private DataFlowCanvas canvas;
	
	public SaveAsToGLIFAction(PathModel _model,JFrame _parent,DataFlowCanvas _canvas){
		super(BpathMainFrame.messages.getString("ACTION.GLIFSAVE"));
		model=_model;
		parent=_parent;
		canvas=_canvas;
		chooser=new JFileChooser(model.getLastSavedFile());
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public SaveAsToGLIFAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){
		if (enabled){
			
			chooser.showSaveDialog(parent);
			File dir=chooser.getSelectedFile();
			if (dir==null) return; 
			
			try {
				
				//PrintStream out=System.out;
				//BufferedWriter out=new BufferedWriter(new FileWriter(dir.getAbsolutePath()+"/"+model.getPathId()+".rdf"));
				BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir.getAbsolutePath()+"/"+model.getPathId()+".rdf"),"UTF8"));
								
				// create an empty model
				GLIF_Factory glif=new GLIF_Factory();
				
				Model rdfModel=glif.getModel();		

				Resource guidelineCollection=glif.createGLIFinstance("Guideline_Collection",model.getMetadata().getTitleMetadata().getName());				
								
				Resource guideline=glif.createGLIFinstance("Guideline",model.getMetadata().getTitleMetadata().getName());
				Resource algorithm=glif.createGLIFinstance("Algorithm",model.getMetadata().getTitleMetadata().getName());
				Resource maintenance=glif.createGLIFinstanceMaintenance(model.getMetadata());
				
				guideline.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"algorithm"), algorithm);					
				guideline.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"maintenance_info"), maintenance);
				
				guidelineCollection.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"guidelines"), guideline);
				
				// for each vertex create an algorithm step
				
				FlowChartNode[] vertices=model.getAllVertices();
				
				for (int i = 0; i < vertices.length; i++) {

					FlowChartNode v=vertices[i];
					
					String vtype=v.getType();
					
					Resource step=null; 
					
					if (vtype.equalsIgnoreCase("COMMENT")){
						
					}else{
						step=glif.createGLIFinstance(vtype, v.getId(),v.getAttributes());
						if (vtype.equalsIgnoreCase("START")){
							algorithm.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"first_step"), step);
						}
					}
					
					algorithm.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"steps"), step);
				}
				
				
				// add all edges 
				
				DataFlowEdge[] edges=model.getAllEdges();
				
				for (int i = 0; i < edges.length; i++) {
					
					DataFlowEdge edge=edges[i];
					
					Resource connector=null;
					Resource sStep=glif.getStepResources(edge.getScrVertex().getId());					
					Resource tStep=glif.getStepResources(edge.getDestVertex().getId());
					
					if (edge.getScrVertex().getType().equalsIgnoreCase("DECISION")){
						
						String label=edge.getAttributes().getSpecialValue("label")[0].toString();
						connector=glif.createGLIFinstance("Decision_Option",label);
						connector.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"destination"), tStep);
						
						connector.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"display_name"), label);
						sStep.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"options"), connector);
										
					}else{
						connector=glif.createGLIFinstance("Next_Step","edge_"+edge.getId());						
						sStep.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"next_step"), tStep);	
					}
					
					algorithm.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"binary_relationships"), connector);
				}
				
				//Write model to file
				RDFWriter w=rdfModel.getWriter("RDF/XML-ABBREV");
				w.setProperty("showDoctypeDeclaration", true);
				w.write(rdfModel, out, null);
				
				System.out.println("GLIF export completed");
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			
		}
	}
	
}
