/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.Template;
import edu.uj.zbit.bpath.editor.model.WizardAttributeSet;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;


public class ChangeTemplateAction extends AbstractAction {
	
	private PathModel model;
	private JFrame parent;
	private JFileChooser chooser;
	
	// element type, String[dest_attrib][src_attrib]
	private Hashtable<String, String[][]> elementMapping=null;;
	
	
	public ChangeTemplateAction(PathModel _model,JFrame _parent){
		super(BpathMainFrame.messages.getString("ACTION.CHANGETEMPLATE"));
		model=_model;
		parent=_parent;
		chooser=new JFileChooser(model.getLastSavedFile());
		
		chooser.addChoosableFileFilter(new FileFilter(){
			public boolean accept(File file){
				if (!file.isDirectory()) 
					return (file.getName().endsWith(".xml"));
				else 
					return true;
			}
			public String getDescription(){
				return "*.path or *.xml";
			}
			
		});
	}

	public ChangeTemplateAction(String name, Icon icon){
		super(name,icon);
	}
	
	public void actionPerformed(ActionEvent e){

		if (enabled){
			
			chooser.showOpenDialog(parent);
			File mappingfile=chooser.getSelectedFile();
			if (mappingfile==null) return; 
			
			Document mapping_doc=null;

			// read mapping file
			try{		
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				mapping_doc = db.parse(mappingfile);				
			}catch(Exception me){
				JOptionPane optionPane = new JOptionPane(me.getMessage(),JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
				optionPane.createDialog(parent,null).setVisible(true);
			}
			
			if (mapping_doc==null) return;
			
			NodeList list=mapping_doc.getElementsByTagName("mapping");

			if (list.getLength()!=1){
				JOptionPane optionPane = new JOptionPane(BpathMainFrame.messages.getString("ERROR.FILEERROR"),JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION);
				optionPane.createDialog(parent,null).setVisible(true);
				return;
			}
			
			// TODO : check whether the source template is the same as the one which is opened
			
			
			// change default template to the destination template	

			Element mappingE=(Element)list.item(0);
			
			String mappingTemplateAddress="http://"+
				mappingE.getAttribute("dest_repository")+"/"+
				mappingE.getAttribute("dest_template")+".xml";
			
			Template newTemplate = new Template(mappingTemplateAddress);
			
			model.setDefaultTemplate(newTemplate);
			
			// load all mapping pairs to a hashtable indexed by element types
			
			elementMapping = new Hashtable<String, String[][]>();
			
			list = mappingE.getElementsByTagName("element_map");
			
			for (int i = 0; i < list.getLength(); i++) {
				Element elemetMapE=(Element)list.item(i);
				String elementType=elemetMapE.getAttribute("id");
				
				NodeList list2=elemetMapE.getElementsByTagName("map");
				
				String[][] mappingTab = new String[list2.getLength()][2];
				
				for (int j = 0; j < mappingTab.length; j++) {
					 Element mapE=(Element)list2.item(j);
					 mappingTab[j][0]=mapE.getAttribute("dest");
					 mappingTab[j][1]=mapE.getAttribute("src");
				}
				
				elementMapping.put(elementType, mappingTab);
			}
			
			// for all vertices, edges and the path element 
			
			FlowChartElement path=(FlowChartElement)model.getPathElement();
			copyAttributeGroups(path,elementMapping.get(path.getType()));
			
			FlowChartNode[] verticies = model.getAllVertices();
			for (int i = 0; i < verticies.length; i++) {
				copyAttributeGroups(verticies[i],elementMapping.get(verticies[i].getType()));				
			}

			DataFlowEdge[] edges = model.getAllEdges();
			for (int i = 0; i < edges.length; i++) {
				copyAttributeGroups(edges[i],elementMapping.get(edges[i].getType()));				
			}
			
			model.fireModelLoaded();
			
		}
	}
	
	private void copyAttributeGroups(FlowChartElement node,String[][] mappingTab){
		
		// copy into a WizardAttribSet all required attributes using the hashtable with the mapping
		
		WizardAttributeSet wizard_as=new WizardAttributeSet();
		
		for (int i = 0; i < mappingTab.length; i++) {
			
			String src_ag=mappingTab[i][1].split("\\.")[0];
			String src_a=mappingTab[i][1].split("\\.")[1];
			
			Object[] value=node.getAttributes().getValue(src_ag, src_a);
			wizard_as.addAttribute(mappingTab[i][0], value);
	
		}
		
		// replace attribute set with a new one initialized by the new template
		
		AttribGroupSet ags= model.getDefaultTemplate().constructAttribGroupSet(model, node.getType(), node.getId());
		node.setAttributes(ags);
				
		// add from the WizardAttributeSet all required attributes 		
		
		Object[][] obj=wizard_as.getAttributes();
		
		for (int i = 0; i < obj.length; i++) {
			
			String dest_ag=obj[i][0].toString().split("\\.")[0];
			String dest_a=obj[i][0].toString().split("\\.")[1];
			
			node.getAttributes().setValue(dest_ag, dest_a, (Object[])obj[i][1]);
		}
	}
	
}
