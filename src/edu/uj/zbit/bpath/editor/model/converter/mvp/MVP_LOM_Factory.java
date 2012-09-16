/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter.mvp;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.Metadata;
import edu.uj.zbit.bpath.editor.model.MetadataEntity;

public class MVP_LOM_Factory {
	
	public final static String NAMESPACE="http://ltsc.ieee.org/xsd/LOM";
	
	public static Document createMetadata(String id, Metadata metadata, AttribGroupSet pathAttribs){

		Document document=null;
		
		// create empty document	
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 document = documentBuilder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root=document.createElementNS(NAMESPACE, "lom");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:hx", "http://ns.medbiq.org/lom/extend/v1/");		
		root.setAttribute("schemaLocation", "http://ltsc.ieee.org/xsd/LOM healthcarelom.xsd");
		document.appendChild(root);
		
		String title=metadata.getTitleMetadata().getName();
				
		String dscr=pathAttribs.getSingleValueAsString("vp_general", "teacher_dscr");
		Object[] icdTab=pathAttribs.getValue("vp_general", "diagnosis_codes");
		MetadataEntity[] authors=metadata.getAuthorsMetadata();
				
		addGeneralBranch(root,id,title,dscr,icdTab);
		addLifeCycleBranch(root, authors);
	
		return document;
	}
	
	
	private static void addGeneralBranch(Element root, String id,String title, String dscr, Object[] codes){
	
		Element generalElement=root.getOwnerDocument().createElement("general");
		root.appendChild(generalElement);
		
		// Metadata: Identifier 
		
		Element identifierElement=root.getOwnerDocument().createElement("identifier");
		generalElement.appendChild(identifierElement);
		
		Element catalogElement=root.getOwnerDocument().createElement("catalog");
		catalogElement.setTextContent("BIT Pathways");
		identifierElement.appendChild(catalogElement);
		
		Element entryElement=root.getOwnerDocument().createElement("entry");
		entryElement.setTextContent(id);
		identifierElement.appendChild(entryElement);
		
		// Metadata: Title
		
		Element titleElement=root.getOwnerDocument().createElement("title");
		generalElement.appendChild(titleElement);
		
		Element string1Element=root.getOwnerDocument().createElement("string");
		string1Element.setTextContent(title);
		string1Element.setAttribute("language", BpathMainFrame.language);
		titleElement.appendChild(string1Element);
		
		// Metadata: Description
		
		Element dscrElement=root.getOwnerDocument().createElement("description");
		generalElement.appendChild(dscrElement);
		
		Element string2Element=root.getOwnerDocument().createElement("string");
		string2Element.setAttribute("language", BpathMainFrame.language);
		dscrElement.appendChild(string2Element);
		string2Element.setTextContent(title);
		
		// Metadata: Keywords
		
		for (int i = 0; i < codes.length; i++) {
			Element keywordElement=root.getOwnerDocument().createElement("keyword");
			keywordElement.setAttribute("id", codes[i].toString());
			keywordElement.setAttribute("source", "ICD-10");
			Element string3Element=root.getOwnerDocument().createElement("string");
			string3Element.setAttribute("language", BpathMainFrame.language);
			keywordElement.appendChild(string3Element);
			keywordElement.setTextContent(codes[i].toString());
		}
		
	}
	
	private static void addLifeCycleBranch(Element root, MetadataEntity[] author){
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
		GregorianCalendar cal=new GregorianCalendar();
		String dateStr=sdf.format(cal.getTime());
		
		Element lcElement=root.getOwnerDocument().createElement("lifeCycle");
		root.appendChild(lcElement);
		
		for (int i = 0; i < author.length; i++) {
			Element contributeElement=root.getOwnerDocument().createElement("contribute");
			lcElement.appendChild(contributeElement);
			
			Element roleElement=root.getOwnerDocument().createElement("role");
			contributeElement.appendChild(roleElement);
			
			Element sourceElement=root.getOwnerDocument().createElement("source");
			roleElement.appendChild(sourceElement);
			
			Element valueElement=root.getOwnerDocument().createElement("value");
			roleElement.appendChild(valueElement);
			
			sourceElement.setTextContent("LOMv1.0");
			valueElement.setTextContent("author");
			
			Element entityElement=root.getOwnerDocument().createElement("entity");
			lcElement.appendChild(entityElement);
			
			entityElement.setTextContent("BEGIN:VCARD\\nFN:"+author[i].getName()+" END:VCARD");
			
			Element dateElement=root.getOwnerDocument().createElement("date");
			Element dateTimeElement=root.getOwnerDocument().createElement("dateTime");
			dateTimeElement.setTextContent(dateStr);
			
			lcElement.appendChild(dateElement);
			dateElement.appendChild(dateTimeElement);
		}
	}
	

	
	private static String htmlize(String str){
		String result=str;
			
		result=result.replaceAll("!spacja", "&nbsp;");
		result=result.replaceAll("&lt;", "<");
		result=result.replaceAll("&gt;", ">");		
		result=result.replaceAll("&amp;", "&");	
		
		return result;
	}


}
