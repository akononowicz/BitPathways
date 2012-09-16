/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter.glif;

import java.util.Hashtable;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.Metadata;

public class GLIF_Factory {

	public static final String KB_NS_PREFIX="http://protege.stanford.edu/kb#";
	public static final String A_NS_PREFIX="http://protege.stanford.edu/system#";
	
	public Hashtable<String, Resource> stepSet = new Hashtable<String, Resource>();
	
	private Model rdfModel=null;
	int classIndex=0;

	//----------------------------------------------------------------------------------	
	
	public GLIF_Factory(){
		rdfModel= ModelFactory.createDefaultModel();				
		rdfModel.setNsPrefix("kb", KB_NS_PREFIX);
		rdfModel.setNsPrefix("a", A_NS_PREFIX);
	}
	
	//----------------------------------------------------------------------------------
	
	public Resource createGLIFinstance(String type, String label){
		Resource r=rdfModel.createResource(KB_NS_PREFIX+"GLIF3_5_BitPathways_Class_"+classIndex);
		classIndex++;
		
		Resource r_type=rdfModel.createResource(GLIF_Factory.KB_NS_PREFIX+type);
		r.addProperty(RDF.type, r_type);
	
		r.addProperty(RDFS.label, label);
		r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"name"), label);
		
	
		return r;	
	}

	//----------------------------------------------------------------------------------	
	
	public Resource createGLIFinstanceMaintenance(Metadata m){
		
		Resource r=rdfModel.createResource(KB_NS_PREFIX+"GLIF3_5_BitPathways_Class_"+classIndex);
		classIndex++;
		
		Resource r_type=rdfModel.createResource(GLIF_Factory.KB_NS_PREFIX+"Maintenance_Info");
		r.addProperty(RDF.type, r_type);

		r.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"title"), m.getTitleMetadata().getName());
//		r.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"authoring_date"), m.getLastModifiedMetadata().getDate());		
		r.addProperty(rdfModel.createProperty(GLIF_Factory.KB_NS_PREFIX+"author"), m.getAuthorsMetadata()[0].getName());
		
		return r;
	}
	
	//----------------------------------------------------------------------------------	
	
	public Resource createGLIFinstance(String bp_type, String id, AttribGroupSet ags){
		
		Resource r=rdfModel.createResource(KB_NS_PREFIX+"GLIF3_5_BitPathways_Class_"+classIndex);
		classIndex++;
		
		String type=getGLIFtype(bp_type);
		
		Resource r_type=rdfModel.createResource(GLIF_Factory.KB_NS_PREFIX+type);
		r.addProperty(RDF.type, r_type);
		
		String label=ags.getSpecialValue("label")[0].toString();
		if (label==null) label="";
		r.addProperty(RDFS.label, label);
		r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"name"), label);
	
		
		if (type.equalsIgnoreCase("Patient_State")){
			r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"display_name"), label);
		}
			
		if (bp_type.equalsIgnoreCase("TASK")) {
			addTaskProperties(r, ags);
		}else if (bp_type.equalsIgnoreCase("DECISION")){
			addDecisionProperties(r, ags);
		}else if (bp_type.equalsIgnoreCase("START")){
			addStartProperties(r, ags);
		}else if (bp_type.equalsIgnoreCase("END")){
			addEndProperties(r, ags);
		}else if (bp_type.equalsIgnoreCase("SUBPATH")){
			addSubpathProperties(r, ags);
		}
		
		stepSet.put(id,r);
		
		return r;
	}
	
	//----------------------------------------------------------------------------------
	
	public static String getGLIFtype(String bp_type){
		
		if (bp_type.equalsIgnoreCase("TASK")) return "Action_Step";
		if (bp_type.equalsIgnoreCase("DECISION")) return "Decision_Step";
		if (bp_type.equalsIgnoreCase("START")) return "Patient_State_Step";
		if (bp_type.equalsIgnoreCase("END")) return "Patient_State_Step";
		if (bp_type.equalsIgnoreCase("SUBPATH")) return "Action_Step";
		return "Unknown"+bp_type;
	}
	
	//----------------------------------------------------------------------------------
	
	private void addTaskProperties(Resource r, AttribGroupSet ags){

		// task_general/med_description
		
		String text=ags.getSingleValueAsString("task_general", "med_description");
		if ((text!=null)&&(text.trim().length()>0))addDidacticsTextMaterial(r,"Comment","TEXT_HTML",text);

		// TODO the rest of attributes - outside of scope of the first version of the export function
		
	}
	
	
	//----------------------------------------------------------------------------------

	private void addDecisionProperties(Resource r, AttribGroupSet ags){
		
		// decision_general/dec_description
		
		String text=ags.getSingleValueAsString("decision_general", "dec_description");
		if ((text!=null)&&(text.trim().length()>0))addDidacticsTextMaterial(r,"Comment","TEXT_HTML",text);
		
		// TODO the rest of attributes - outside of scope of the first version of the export function
		
		
	}

	//----------------------------------------------------------------------------------

	private void addStartProperties(Resource r, AttribGroupSet ags){
		
		// start_general/inclusion_criteria
		
		String text=ags.getSingleValueAsString("start_general", "inclusion_criteria");
		if ((text!=null)&&(text.trim().length()>0))addCriterionText(r,"Inclusion criteria:"+text);
		
		// start_general/exclusion_criteria
		
		text=ags.getSingleValueAsString("start_general", "exclusion_criteria");
		if ((text!=null)&&(text.trim().length()>0))addCriterionText(r,"Exclusion criteria:"+text);
		
		// TODO the rest of attributes - outside of scope of the first version of the export function
	}
	
	//----------------------------------------------------------------------------------

	private void addEndProperties(Resource r, AttribGroupSet ags){
	
		// end_general/end_conditions
		
		String text=ags.getSingleValueAsString("end_general", "end_conditions");
		if ((text!=null)&&(text.trim().length()>0))addCriterionText(r,"End conditions:"+text);
		
		// TODO the rest of attributes - outside of scope of the first version of the export function		
	}
	
	//----------------------------------------------------------------------------------

	private void addSubpathProperties(Resource r, AttribGroupSet ags){
		
		// TODO the rest of attributes - outside of scope of the first version of the export function		
	}

	
	//----------------------------------------------------------------------------------	
	
	private void addCriterionText(Resource r, String text){
		Resource r2=createGLIFinstance("Criterion", "info_"+(classIndex+1));
		addDidacticsTextMaterial(r2,"Comment","TEXT_HTML",text);
		
		r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"patient_state_description"), r2);
		
	}
	
	private void addDidacticsTextMaterial(Resource step, String purpose, String mime, String text){
		addDidacticsTextMaterial(step, purpose, mime, text, 0);
	}
	
	private void addDidacticsTextMaterial(Resource step, String purpose, String mime, String text, int index){
		
		Resource r=createGLIFinstance("Supplemental_Material_List", "info_"+(classIndex+1));
		r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"purpose"), purpose);
		
		Resource r2=createGLIFinstance("Text_Material", "info_text_"+(classIndex+1)+"_"+index);
		r2.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"MIME_type_format"), mime);
		r2.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"material"), revivePolishCharacters(text));
		
		r.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"items"), r2);
		step.addProperty(rdfModel.createProperty(KB_NS_PREFIX+"didactics"), r);
		
		
	}
	
	//----------------------------------------------------------------------------------

	private void addDidacticsURLMaterial(Resource step, String purpose, String mime, Object[] values){
		//TODO
	}
	
	//----------------------------------------------------------------------------------
	
	private String revivePolishCharacters(String xmlString){
		
		String[] polish={"π","Ê","Í","≥","Ò","Û","ú","ü","ø","•","∆"," ","£","—","”","å","è","Ø","ñ","ñ"};
		String[] safe={"&#261;","&#263;","&#281;","&#322;","&#324;","&#243;","&#347;","&#378;","&#380;","&#260;","&#262;","&#280;","&#321;","&#323;","&#211;","&#346;","&#377;","&#379;"};
		
		for (int i = 0; i < safe.length; i++) {
			xmlString=xmlString.replaceAll(safe[i],polish[i]);	
		}

		return xmlString;
	}
	
	
	public Resource getStepResources(String id){
		return stepSet.get(id);
	}
	
	
	public Model getModel(){
		return rdfModel;
	}
	
}
