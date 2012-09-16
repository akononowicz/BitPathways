/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * Reads template from the server, answers questions about attrib group and attrib names,
 * returns attrib types and creates AttribGroupSets for given node types 
 * 
 * @author Andrzej Kononowicz
 *
 */

public class Template {
	
	private String templateAddress;
	
	private final String TEMPLATE_DIR="template";
	
	/** labels in different languages */
	private Hashtable<String, String> label=new Hashtable<String, String>();
	/** attrib groups from template */
	private Hashtable<String, TemplateAG> ag=new Hashtable<String, TemplateAG>();
	/** attrib bindings from template */
	private Hashtable<String, TemplateAG[]> ab=new Hashtable<String, TemplateAG[]>();
	
	/** attrib bindings from template */
	private String lang;
	
	
	public Template(String _templateAddress){

		templateAddress=_templateAddress;
		
		try{		
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        URL url=new URL(templateAddress);
	        
	        Document doc = db.parse(url.openStream());
	       
	        // --- load path metadata 
	        
	        Element metadataElement=(Element)doc.getElementsByTagName("metadata").item(0);
	        
	        Element lE=(Element)metadataElement.getElementsByTagName("labels").item(0);
        	NodeList lL=lE.getElementsByTagName("label"); 
        	
        	for (int j = 0; j < lL.getLength(); j++) {
        		Element lElement=(Element) lL.item(j);
        		label.put(lElement.getAttribute("lang"), lElement.getTextContent());
        	}
	        
	        // --- load all attribute types into a temporary hashtable
	        
	        Hashtable<String,TemplateA> temporaryAttributes = new Hashtable<String,TemplateA>();
	        
	        NodeList aList=doc.getElementsByTagName("attribute");
	        
	        for (int i = 0; i < aList.getLength(); i++) {
	        	
	        	Element aElement=(Element)aList.item(i);
	        	
	        	String aid=aElement.getAttribute("id");
	        	String atype=aElement.getAttribute("type");
	        	String aitemtype=aElement.getAttribute("item_type");
	        	String adict=aElement.getAttribute("dict_type");
	        		        	
	        	// attrib labels
	        	Hashtable<String, String> labels=new Hashtable<String, String>();
	        	
	        	Element llElement=(Element)aElement.getElementsByTagName("labels").item(0);
	        	NodeList lList=llElement.getElementsByTagName("label"); 
	        	
	        	for (int j = 0; j < lList.getLength(); j++) {
	        		Element lElement=(Element) lList.item(j);
	        		labels.put(lElement.getAttribute("lang"), lElement.getTextContent());
	        	}
	        	
	        	// attrib item labels
	        	Hashtable<String, String> ilabels=new Hashtable<String, String>();
	        	
	        	if(aitemtype.trim().length()>0){
	        		Element lliElement=(Element)aElement.getElementsByTagName("item_labels").item(0);
		        	NodeList liList=lliElement.getElementsByTagName("label"); 
		        	
		        	for (int j = 0; j < lList.getLength(); j++) {
		        		Element lElement=(Element) liList.item(j);
		        		ilabels.put(lElement.getAttribute("lang"), lElement.getTextContent());
		        	}
	        	}
	        		
	        	// create attribute object
	        	TemplateA a=new TemplateA(aid, labels, ilabels, atype, aitemtype, adict);
	        	temporaryAttributes.put(aid, a);
			}
	        
	        // --- load all attribute groups, attach proper attributes 
	        
	        NodeList agList=doc.getElementsByTagName("attribute_group");
	        
	        for (int i = 0; i < agList.getLength(); i++) {
	        	Element agElement=(Element)agList.item(i);
	        	String agid=agElement.getAttribute("id");
	        	
	        	// attrib group labels
	        	Hashtable<String, String> labels=new Hashtable<String, String>();
	        	
	        	Element llElement=(Element)agElement.getElementsByTagName("labels").item(0);
	        	NodeList lList=llElement.getElementsByTagName("label"); 
	        	
	        	for (int j = 0; j < lList.getLength(); j++) {
	        		Element lElement=(Element) lList.item(j);
	        		labels.put(lElement.getAttribute("lang"), lElement.getTextContent());
	        	}
	        	
	        	// attrib references
	        	NodeList arList=agElement.getElementsByTagName("attribute_ref");
	        	TemplateA[] irTab=new TemplateA[arList.getLength()];
	        	Hashtable<String, TemplateA> aHash=new Hashtable<String, TemplateA>(); 
	        	
	        	for (int j = 0; j < arList.getLength(); j++) {
	        		Element arElement=(Element) arList.item(j);
	        		String ref=arElement.getAttribute("ref");
		        	int order=Integer.parseInt(arElement.getAttribute("order"));
		        	
		        	TemplateA a=temporaryAttributes.get(ref);
		        	
		        	irTab[order-1]=a;
		        	aHash.put(ref,a);
	        	}
	        	
	        	TemplateAG lag=new TemplateAG(agid, labels, aHash, irTab);
	        	ag.put(agid, lag);
	        }
	        
	        
	        // --- load all attribute binding
	        
	        Hashtable<String,String[]> temporaryAB=new Hashtable<String,String[]>();
	        
	        NodeList abList=doc.getElementsByTagName("attribute_binding");
	        
	        // read all ABs into a temporary table
	        for (int i = 0; i < abList.getLength(); i++) {

	        	Element abElement=(Element)abList.item(i);
	        	String type=abElement.getAttribute("type");
	        	if (type.trim().length()==0) type="PATH";
	        	
	        	String ref=abElement.getAttribute("ag_ref");
	        	int order=Integer.parseInt(abElement.getAttribute("order"));
	        	
	        	String[] abtab;
	        	
	        	if (!temporaryAB.containsKey(type)){
	        		abtab=new String[100];
	        	}else{
	        		abtab=temporaryAB.get(type);
	        	}
	        	
	        	abtab[order-1]=ref;
	        	temporaryAB.put(type, abtab);
	        }
	        
	        // convert temporary AB into final AB
	        
	        Enumeration<String> nodeTypes=temporaryAB.keys();
	        
	        while (nodeTypes.hasMoreElements()) {
				
	        	String nodeType = (String) nodeTypes.nextElement();
				String[] agtab=temporaryAB.get(nodeType);
				
				int n=0;for (n = 0; n < agtab.length; n++) {if (agtab[n]==null) break;}
								
				TemplateAG[] agtab2=new TemplateAG[n];
				for (int i = 0; i < agtab2.length; i++) {
					agtab2[i]=ag.get(agtab[i]);
				}
				
				ab.put(nodeType, agtab2);
			
			}
	        	
	        
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//http://puls.cm-uj.krakow.pl/bpath/templates/t_simple_v001.xml
	
	public String getRepository(){
		int index=templateAddress.lastIndexOf("/");
		return templateAddress.substring(7,index);
	}
	
	public String getId(){
		int startIndex=templateAddress.lastIndexOf("/")+1;
		int endIndex=templateAddress.lastIndexOf(".");
		return templateAddress.substring(startIndex,endIndex);
	}
	
	public String getTemplateURL(){
		return templateAddress;
	}
	
	
	public String getLabel(String group_id, String lang){
		if (ag.get(group_id)==null) return "[unknown]";
		else return ag.get(group_id).getLabel(lang);
	}
	
	public String getLabel(String group_id, String attribute_id, String lang){
		if (ag.get(group_id)==null) return "[unknown]";
		return ag.get(group_id).getLabel(attribute_id,lang);
	}
	
	public String getItemLabel(String group_id, String attribute_id, String lang){
		if (ag.get(group_id)==null) return "[unknown]";
		return ag.get(group_id).getItemLabel(attribute_id,lang);
	}
	
	public String getType(String group_id, String attribute_id){
		if (ag.get(group_id)==null) return "string";
		return ag.get(group_id).getAttrib(attribute_id).getType();
	}
	
	public String getItemType(String group_id, String attribute_id){
		if (ag.get(group_id)==null) return "string";
		return ag.get(group_id).getAttrib(attribute_id).getItemType();
	}
	
	public String getDictType(String group_id, String attribute_id){
		if (ag.get(group_id)==null) return "[unknown]";
		return ag.get(group_id).getAttrib(attribute_id).getDictType();
	}
	
	public String getLanguage(){
		return lang;
	}
	
	public AttribGroupSet constructAttribGroupSet(PathModel model,String nodeName, String nodeId){
		
		
		Hashtable<String, AttribGroup> agHash=new Hashtable<String, AttribGroup>(); 

		// For each ag for this node type
		
		TemplateAG[] tAGs=ab.get(nodeName);
		if (tAGs!=null){
			String[] agOrder=new String[tAGs.length];
	
			for (int i = 0; i < tAGs.length; i++) {
	
				TemplateAG tAG=tAGs[i];
				agOrder[i]=tAGs[i].getId();
				
				// Construct a new AttribGroup
				Hashtable<String, Object[]> aHash = new Hashtable<String, Object[]>();
				
				// For each a in this ag
				TemplateA[] tAs=tAG.getAllAttribs();
				String[] aOrder=new String[tAs.length];
		
				for (int j = 0; j < tAs.length; j++) {
					TemplateA tA=tAs[j];
					
					aOrder[j]=tAs[j].getId();
	
					Object[] emptyNodeTab=new Object[1];
					emptyNodeTab[0]="";
					
					// add Attrib to aHash
					aHash.put(tA.getId(),emptyNodeTab);
				}
				
				// add AttribGroup to agHash
				agHash.put(tAG.getId(), new AttribGroup(nodeId, tAG.getId(),model,aHash,aOrder));	
			}
	
			AttribGroupSet ags=new AttribGroupSet(nodeId,getId(),agHash,agOrder);
			return ags;
		}else{
			AttribGroupSet ags=new AttribGroupSet(nodeId,getId(),agHash,new String[0]);
			return ags;
		}
	}
	
	public String toString(){
		
		String retStr="";
		retStr+="TEMPLATE: === "+getId()+" === "+label.get("pl")+"\n";
		
		Enumeration<String> abs=ab.keys();
		
		while (abs.hasMoreElements()) {
			String abname = (String) abs.nextElement();
			retStr+="<"+abname+">\n";
			
			TemplateAG[] ags=ab.get(abname);
			
			for (int i = 0; i < ags.length; i++) {
				retStr+=(i+1)+"."+ags[i].toString()+"\n";
			}
		}
		return retStr;
	}
}
