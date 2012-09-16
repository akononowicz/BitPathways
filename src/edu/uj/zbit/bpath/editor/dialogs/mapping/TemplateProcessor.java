/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/


package edu.uj.zbit.bpath.editor.dialogs.mapping;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * 
 * Helper class for reading all types and attributes in a given template
 * 
 * @author Maciej Wilk 
 *
 */

public class TemplateProcessor {
	
	private Document doc;
	
	public TemplateProcessor(String file){
		try {
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 org.w3c.dom.Document _doc = builder.parse(file);
			 this.doc = _doc;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String[] getElementNames(){
		
		NodeList nodeList = this.doc.getElementsByTagName("attribute_binding");
		ArrayList<String> h = new ArrayList<String>();
		
		String s;
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			Element element = (Element) node;
			s = element.getAttribute("type");
		
			if (!h.contains(s)){
				if(s.equals("")){
					h.add("PATH");
				}
				else{
					h.add(s);
				}	
			}
		}
		String[] str = new String[h.size()];
		h.toArray(str);
		return str;
		
	}
	private String[] getAG(String abType){
		
		NodeList nodeList = this.doc.getElementsByTagName("attribute_binding");
		ArrayList<String> h = new ArrayList<String>();
		String s;
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			Element element = (Element) node;
			s = element.getAttribute("type");
			if(s.equals(abType)){
				s = element.getAttribute("ag_ref");
				h.add(s);
			}
		}
		String[] agTab = new String[h.size()];
		h.toArray(agTab);
		return agTab;
		
	}
	private String[] getA(String agTab){
		
		NodeList nodeList = this.doc.getElementsByTagName("attribute_group");
		ArrayList<String> h = new ArrayList<String>();
		String s;
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			Element element = (Element) node;
			s = element.getAttribute("id");
			if(s.equals(agTab)){
				NodeList refList = element.getElementsByTagName("attribute_ref");
				String str;
				for(int j = 0; j < refList.getLength(); j++){
					Node ref = refList.item(j);
					Element elem = (Element) ref;
					str = elem.getAttribute("ref");
					h.add(str);
				}
			}
		}
		String[] aTab = new String[h.size()];
		h.toArray(aTab);
		return aTab;
		
	}
	private String getTypeDetails(String aTab){
		
		NodeList nodeList = this.doc.getElementsByTagName("attribute");
		HashSet<String> h = new HashSet<String>();
		String str = "";
		String s;
		
		for(int i = 0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			Element element = (Element) node;
			s = element.getAttribute("id");
			if(s.equals(aTab)){
				s = element.getAttribute("type");
				str = s;
				if(s.equals("dict")){
					s = element.getAttribute("dict_type");
					str += "."+s;
				}
				s = element.getAttribute("type");
				if(s.equals("list")){
					s = element.getAttribute("item_type");
					str += "."+s;
					if(s.equals("dict")){
						s = element.getAttribute("dict_type");
						str += "."+s;
					}
				}
			}
		}
		return str;
		
	}
	public String[] getAttributeList4Element(String abType){
		
		ArrayList<String> retArray = new ArrayList<String>();
		String[] agTab = getAG(abType);
		
		for(int i = 0; i < agTab.length; i++){
			String[] aTab = getA(agTab[i]);
			for(int j = 0; j < aTab.length; j++){
				String aDscr = getTypeDetails(aTab[j]);
				retArray.add(agTab[i]+"."+aTab[j]+":"+aDscr);
			}
		}
		String[] ret = new String[retArray.size()];
		retArray.toArray(ret);
		return ret;
		
	}

}