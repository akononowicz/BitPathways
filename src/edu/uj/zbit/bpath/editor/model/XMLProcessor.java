/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.TextImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.propertyPanel.DurationCellRenderer;

public class XMLProcessor {
	
	 static Templates templateXSLTelement;
	 static Templates templateXSLThtml2text;
	 private static final Logger log = Logger.getLogger(XMLProcessor.class);
	 
	 static{
		 try {
             // Create transformer factory
             TransformerFactory factory = TransformerFactory.newInstance();
 
             templateXSLTelement = factory.newTemplates(new StreamSource(
           		  XMLProcessor.class.getClass().getResourceAsStream("/xslt/element.xslt")	  
             ));
                          
             templateXSLThtml2text = factory.newTemplates(new StreamSource(
           		  XMLProcessor.class.getClass().getResourceAsStream("/xslt/html2text.xslt")	  
             ));
 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	 }
	
	
	  
	  
	  public static String html2textlabel(String html){
		  
		  html="<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>"+html+"</html>";
	  
		  try {
                          
              // Use the template to create a transformer
              Transformer xformer = templateXSLThtml2text.newTransformer();
  
              // Prepare the input and output files
              Source source = new StreamSource(new StringReader(html));
              StringWriter resultWriter=new StringWriter();
              Result result = new StreamResult(resultWriter);
  
              // Apply the xsl file to the source file and write the result to the output file
              xformer.transform(source, result);
              
              String ret_html=resultWriter.toString().trim();
              StringBuffer trimed_html=new StringBuffer();
              
              int j=0;
              int space=0;
              
              for (int i = 0; ((i < ret_html.length())&&(j<200)); i++) {
				
            	  char c=ret_html.charAt(i);
            	  
            	  if ((c==' ')||(c=='\t')||(c=='\n')||(c=='\r')){
            		  space++;
            		  c=' ';
            	  }else{
            		  space=0;
            	  }
            	  
            	 if (space<2){
					j++;
					trimed_html.append(c);
				}
			  }
              
              
              return  trimed_html.toString();
          } catch (Exception e) {
          }
		  
		  return "";
	  }
	  
	  public static String transformElement(FlowChartElement dfe){
		  
		  String html="<html>";		
		  
		  		html+="<style type='text/css'>"+
				"<!--"+
				".element {"+
				"	background-color: #87B4FB;"+
				"	font-weight:bold;"+
				"}"+
				".attribgroup {"+
				"	background-color: #C7CAEA;"+
				"}"+
				".attrib {"+
				"	background-color: #F0F0F0;"+
				"}"+
				".value {"+
				"	background-color: #FFFFFF;"+
				"}"+				
				"table {"+
				"	font-family: Verdana, Arial, Helvetica, sans-serif;"+
				"	font-size: 8px;"+
				"	border: 1px solid #000000;"+
				"}"+
				"td{"+
				"	vertical-align:top;"+
				"	border: 1px solid #000000;"+
				"}"+
				"-->"+
				"</style>";
		  
		  
		  // create type header
		  html+="<table width='100%' cellspacing='0' class='element'><tr><td>"+dfe.getTypeName()+"</td></tr></table>";
			
		  // for each attribute group
		  for (int i = 0; i < dfe.getAttributes().getSize(); i++) {
			  AttribGroup ag=dfe.getAttributes().get(i);
			  if (!ag.isAttribGroupEmpty()) html+=transformAttibGroup(dfe.getAttributes().get(i),100,"attribgroup","attrib","value");
		  }
			 
		  html+="</html>";

		  return html;
	  }
	  
	  
	  public static String transformElement2(FlowChartElement dfe){
		  
		 
		  
		  // create type header
		  String html="<table width='90%' class='main' cellspacing='0' cellpadding='0' ><tr><td class='blineh'><b>"+dfe.getTypeName()+"</b></td></tr></table>";
			
		  // for each attribute group
		  for (int i = 0; i < dfe.getAttributes().getSize(); i++) {
			  AttribGroup ag=dfe.getAttributes().get(i);
			  if (!ag.isAttribGroupEmpty()) html+=transformAttibGroup(dfe.getAttributes().get(i),90,"blineh","blineh","bline");
		  }
			 
		  return html;
	  }
	  
	  
	  private static String transformAttibGroup(AttribGroup ag, int percent, String cssClass1, String cssClass2,String cssClass3){
		  String html="<table width='"+percent+"%' cellspacing='0' border='1'>";
		 
		  //header row
		  html+="<tr class='"+cssClass1+"'><td colspan='2' class='"+cssClass1+"'><b>";
		  html+=ag.getName();
		  html+="</b></td></tr>";
		  
		  // for each attribute
		  
		  String[] aNames=ag.getAttribNames();
		  
		  for (int i = 0; i < aNames.length; i++) {
			
			if(!ag.isAttribEmpty(aNames[i])){
				html+="<tr class='"+cssClass1+"'>";
				// attrib label
				html+="<td width='100px' class='"+cssClass2+"'><b>";
				html+=ag.getAttribLabel(aNames[i]);
				html+="</b></td>";
				// attrib value
				html+="<td class='"+cssClass3+"'>";
				html+=transformAttrib(ag,aNames[i]);
				html+="</td>";
				html+="</tr>";
			}
		  }
		  
		  // if attribute not empty 
		  
		  
		  html+="</table>";
		  return html;
	  }
	
	  
	  private static String transformAttrib(AttribGroup ag,String attrib_id){
		  
		  String html="";
		  String type=ag.getAttribType(attrib_id);
		  
		  if(type.equalsIgnoreCase("list")){
			 html+="<ul>";
			 
			 Object[] values=ag.getAttrib(attrib_id);
			 
			 for (int i = 0; i < values.length; i++) {
				
				 if (values[i]==null) continue; 
				 if ((values[i] instanceof String)&&(values[i].toString().trim().length()==0)) continue;
				 if ((values[i] instanceof Element)&&(((Element)values[i]).getTextContent().trim().length()==0)) continue;
				 
				 html+="<li>";
				 html+=transformValue(ag.getAttribItemType(attrib_id), values[i]);
				 html+="</li>";
			 }
			 
			 html+="</ul>";
		  }else{
			 html+=transformValue(type,ag.getAttrib(attrib_id)[0]); 
		  }
		  
		  return html;
	  }
	  
	  
	  private static String transformValue(String type, Object value){
		  
		  String html="";
		  try{
			  if(type.equalsIgnoreCase("link")){

				  Element valueE=(Element)value;	  
				  Element href=(Element)valueE.getElementsByTagName("href").item(0);
				  Element dscr=(Element)valueE.getElementsByTagName("dscr").item(0);		
				  html+="<a href='link'>"+href.getTextContent()+"</a><br/>";
				  html+=dscr.getTextContent();
				  
			  }else if(type.equalsIgnoreCase("cite")){
				  Element cite=(Element)((Element)value).getElementsByTagName("cite").item(0);
				  Element abstr=(Element)((Element)value).getElementsByTagName("abstract").item(0);			  
				  html+=cite.getTextContent()+"<br/>"+abstr.getTextContent();
			  }else if(type.equalsIgnoreCase("duration")){
				  html+=DurationCellRenderer.getDurationLabel(value.toString(), BpathMainFrame.language);
			  }else{
				  if (value instanceof String){
					  html+=value;
				  }
				  else{
					  System.out.println("type:"+type);
					  html+=((Element)value).getTextContent();
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return html;
	  }
	  
	  public static NodeList getElements(String importXML){
		  try{
				 DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				 DocumentBuilder builder = factory.newDocumentBuilder();
				 Document doc=builder.parse(new InputSource(new StringReader(importXML)));
				 
				 NodeList elements=(NodeList)doc.getElementsByTagName("element");
				 return elements;
				 
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			 
		 return (NodeList)null;
			 
	  }
	  
	  public static String getText(NodeList list){
		  StringBuffer text=new StringBuffer("");
		  
		  for (int i = 0; i < list.getLength(); i++) {
			Node element=list.item(i);
			if (element instanceof TextImpl){
				TextImpl txtElement=(TextImpl)element;
				text.append(txtElement.getTextContent());
			}
		  }
		  return text.toString().trim();
	  }
	  
	  /** Skips all TextNodes :( */
	  public static Element getFirstElement(NodeList list){
		  Element element=null;
		  
		  for (int i = 0; i < list.getLength(); i++) {
			    if (list.item(i) instanceof Element) return (Element) list.item(i);
		  }
		  return element;
	  }
	  
	  
	  public static void writeXMLmetadata(Metadata metadata,Document doc, Element metadataNode){
		  String[] metadata_fields={"created","last_modified","teacher","group","topic","authors","title","access_rights"};

		  for (int i = 0; i < metadata_fields.length; i++) {
			  writeXMLmetadataField(metadata,doc,metadataNode,metadata_fields[i]);
		  }
 
	  }
	  
	  private static void writeXMLmetadataField(Metadata metadata,Document doc, Element metadataNode,String key){
			 if (metadata.containsKey(key)){
				 Element keyNode=doc.createElement(key);
				 MetadataEntity[] entities=metadata.get(key);
				 for (int i = 0; i < entities.length; i++) {
					 Element entity=doc.createElement("entity");
					 
					 if (entities[i].getId()!=null){
						 Element en=doc.createElement("id");
						 en.setTextContent(entities[i].getId());
						 entity.appendChild(en);
					 }
					 
					 if (entities[i].getCatalog()!=null){
						 Element en=doc.createElement("catalog");
						 en.setTextContent(entities[i].getCatalog());
						 entity.appendChild(en);
					 }
					 
					 if (entities[i].getName()!=null){
						 Element en=doc.createElement("name");
						 en.setTextContent(entities[i].getName());
						 entity.appendChild(en);
					 }
					 
					 if (entities[i].getDate()!=null){
						 Element en=doc.createElement("date");
						 en.setTextContent(entities[i].getDate());
						 entity.appendChild(en);
					 }
					 
					 keyNode.appendChild(entity);
					 metadataNode.appendChild(keyNode);
				 }
			 }
		 }
		 
	  
	  
	  
	  	public static void readXMLmetadata(Metadata metadata,Element metadataElement){
	    	String[] metadata_fields={"created","last_modified","teacher","group","topic","authors","title","access_rights"};
	    	
	    	for (int i = 0; i < metadata_fields.length; i++) {
		    	if(metadataElement.getElementsByTagName(metadata_fields[i]).getLength()>0){
		    		Element metadataField= (Element) metadataElement.getElementsByTagName(metadata_fields[i]).item(0);
		    		readXMLmetadataField(metadata,metadataField.getElementsByTagName("entity"),metadata_fields[i]);
		    	}
			}
	  	}
	  
	  
		 private static void readXMLmetadataField(Metadata metadata,NodeList entityList, String key){
			 	 
			MetadataEntity[] meTab=new MetadataEntity[entityList.getLength()];
			 
			 for (int i = 0; i < meTab.length; i++) {
				
				Element entityNode=(Element)entityList.item(i);
				MetadataEntity entity = new MetadataEntity(); 
			
				if (entityNode.getElementsByTagName("id").getLength()>0){
					entity.setId(entityNode.getElementsByTagName("id").item(0).getTextContent());
				}
				if (entityNode.getElementsByTagName("catalog").getLength()>0){
					entity.setCatalog(entityNode.getElementsByTagName("catalog").item(0).getTextContent());
				}
				if (entityNode.getElementsByTagName("name").getLength()>0){
					entity.setName(entityNode.getElementsByTagName("name").item(0).getTextContent());
				}
				if (entityNode.getElementsByTagName("date").getLength()>0){
					entity.setDate(entityNode.getElementsByTagName("date").item(0).getTextContent());
				}
				
				meTab[i]=entity;
			 }
			 
			 
			 metadata.put(key, meTab);
			 
		 }
	  
}
