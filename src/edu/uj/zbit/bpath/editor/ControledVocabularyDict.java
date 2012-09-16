/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Gets controlled vocabularies from xml files 
 * 
 * @author Andrzej A. Kononowicz
 *
 */

public class ControledVocabularyDict {


	private static HashMap<String,HashMap> dicts_PL;
	private static HashMap<String,HashMap> dicts_DE;
	private static HashMap<String,HashMap> dicts_EN;		
	
	static{
		 DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		
		 	dicts_PL=new HashMap<String,HashMap>();
			dicts_DE=new HashMap<String,HashMap>();
			dicts_EN=new HashMap<String,HashMap>();
		 
			try{
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(ControledVocabularyDict.class.getClass().getResourceAsStream("/xml/dicts.xml"));
				NodeList dictsNodes=document.getElementsByTagName("dict");
				
				for(int i=0;i<dictsNodes.getLength();i++){
					addDict((Element)dictsNodes.item(i));
				}
			
			}catch(Exception e){
				e.printStackTrace();
			} 
	}
	
	private static void addDict(Element dictNode){
		String dictName=dictNode.getAttribute("name");
		
		HashMap dict_PL=new HashMap();
		HashMap dict_DE=new HashMap();
		HashMap dict_EN=new HashMap();
		
		
		NodeList entries=dictNode.getElementsByTagName("entry");
		
		for(int i=0;i<entries.getLength();i++){
			
			Element entryNode=(Element)entries.item(i);
			String key=entryNode.getAttribute("key");

			NodeList valueNodes=entryNode.getElementsByTagName("value");
			
			for(int j=0;j<valueNodes.getLength();j++){
				Element valueNode=(Element)valueNodes.item(j);
				String lang=valueNode.getAttribute("lang");
				String value=valueNode.getFirstChild().getTextContent();
		
				if (lang.equalsIgnoreCase("EN"))
					dict_EN.put(key, value);
				else if (lang.equalsIgnoreCase("PL"))
					dict_PL.put(key, value);
				else if (lang.equalsIgnoreCase("DE"))				
					dict_DE.put(key, value);		
			}
		}
		
		dicts_DE.put(dictName,dict_DE);
		dicts_EN.put(dictName,dict_EN);
		dicts_PL.put(dictName,dict_PL);		
	}
	
	public static String[] getDictItems(String dictName,String lang){
		
		HashMap dict=null;
			
		
		if (lang.equalsIgnoreCase("EN"))
			dict=(HashMap)dicts_EN.get(dictName);
		else if (lang.equalsIgnoreCase("PL"))
			dict=(HashMap)dicts_PL.get(dictName);
		else if (lang.equalsIgnoreCase("DE"))				
			dict=(HashMap)dicts_DE.get(dictName);		

		Collection values=dict.keySet();
		
		int[] keys=new int[values.size()];
		String[] items=new String[values.size()];
		
		Iterator iter=values.iterator();
		
		int i=0;
		
		while (iter.hasNext()) {
			String element = (String) iter.next();
			keys[i++]=Integer.parseInt(element);
		}
		
		Arrays.sort(keys);
		
		for (int j = 0; j < items.length; j++) {
			items[j]=(String)dict.get(""+keys[j]);
		}
		
		return items;
		
	}
}
