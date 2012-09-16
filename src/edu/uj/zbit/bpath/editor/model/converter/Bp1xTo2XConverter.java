/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter;

import java.io.File;
import java.io.FileReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Class for converting Bit Pathways v.1.x models into Bit Pathways 2 files.
 * By default everything is converted into the t_cp_v001 template. 
 * Property mapping file (1x_mapping.properties) in the resources folder is driving 
 * the mapping process. The converting is carried out by invoking the convert method with
 * inputfile and outputfile path strings as input parameters. Class can work as 
 * a standalone application (is independent from the current BitPathways model and
 * does not use any external classes.   
 *  
 * 
 * @author Andrzej Kononowicz
 */

public class Bp1xTo2XConverter {
	
	static Properties aMap;
	
	static{
		try {
			aMap=new Properties();
			aMap.load((Bp1xTo2XConverter.class.getResourceAsStream("/resources/1x_mapping.properties")));
			System.out.println("Read me:"+aMap.get("1.0.0"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void convert(String inputFileName, String outputFileName){
		convert(new File(inputFileName),new File(outputFileName));
	}
	
	public static void convert(File inputFile, File outputFile){
		
		try {
			Document inDoc=null,outDoc=null;
			
			//Read inDoc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			inDoc = db.parse(inputFile);
			
			// Create outDoc;
			outDoc = db.newDocument(); 
			
			// Convert
			initStructure(inDoc,outDoc);
			copyMetadata(inputFile,inDoc,outDoc);
			copyHeader(inDoc,outDoc);			
			copyElements(inDoc,outDoc);
			copyConnections(inDoc,outDoc);
			copySubpaths(outDoc);
			
			Source source = new DOMSource(outDoc);

	        // Prepare the output file
	 
	        Result result = new StreamResult(outputFile);

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        xformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void initStructure(Document inDoc, Document outDoc){
		
		Element bpE=outDoc.createElement("bitpathways");
		bpE.setAttribute("version", "2.0");

		Element pathE=outDoc.createElement("path");
		
		Element inRootE=(Element)inDoc.getElementsByTagName("path").item(0);
		pathE.setAttribute("id", inRootE.getAttribute("id"));
		pathE.setAttribute("catalog","puls.cm-uj.krakow.pl:8080/exist");
		pathE.setAttribute("lang","pl");
			
		String[] sectionNames={"templates","metadata","subpaths","header","elements","connections"};
		
		for (int i = 0; i < sectionNames.length; i++) {
			Element element=outDoc.createElement(sectionNames[i]);
			pathE.appendChild(element);
		}

		Element templates=(Element)pathE.getElementsByTagName("templates").item(0);
		Element tempE=outDoc.createElement("template_ref");
		
		tempE.setAttribute("id", "t_cp_v001");
		tempE.setAttribute("repository", "puls.cm-uj.krakow.pl/bpath/templates");
		
		templates.appendChild(tempE);
		
		bpE.appendChild(pathE);
		outDoc.appendChild(bpE);
	}
	
	private static void copyMetadata(File inFile,Document inDoc, Document outDoc){
		
		try {
			Element outMetadata=(Element)outDoc.getElementsByTagName("metadata").item(0);
				
			// created
			
			Date date=new Date(inFile.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			String dateStr=sdf.format(date);
			
			Element created=(Element)outDoc.createElement("created");
			addMetadataEntity(outDoc,created,null,"Kononowicz;Andrzej",dateStr);
			outMetadata.appendChild(created);
			
			//last_modified
			
		    date=new Date(System.currentTimeMillis());
			String dateStr2=sdf.format(date);

			Element last_modified=(Element)outDoc.createElement("last_modified");
			addMetadataEntity(outDoc,created,null,"Kononowicz;Andrzej",dateStr2);
			outMetadata.appendChild(last_modified);
			
			//teacher
			
			Attr t=(Attr)XPathAPI.selectSingleNode(inDoc, "/bitpathway/path/@teacher");
			
			if((t!=null)&&(!t.getTextContent().equalsIgnoreCase("none"))){
				Element title=(Element)outDoc.createElement("teacher");
				addMetadataEntity(outDoc,title,null,t.getTextContent(),null);
				outMetadata.appendChild(title);
			}
			
			//group
			
			Attr g=(Attr)XPathAPI.selectSingleNode(inDoc, "/bitpathway/path/@group");
			
			if((g!=null)&&(!g.getTextContent().equalsIgnoreCase("none"))){
				Element title=(Element)outDoc.createElement("group");
				addMetadataEntity(outDoc,title,null,g.getTextContent(),null);
				outMetadata.appendChild(title);
			}
			
			//topic
			
			//title
			
			Text m=(Text)XPathAPI.selectSingleNode(inDoc, "//attrib[@class_id='0.0.0']/value/text()");
			if(m!=null){
				Element title=(Element)outDoc.createElement("title");
				addMetadataEntity(outDoc,title,null,m.getTextContent(),null);
				outMetadata.appendChild(title);
			}
			
			//authors
			
			Text a=(Text)XPathAPI.selectSingleNode(inDoc, "//attrib[@class_id='0.0.4']/value/text()");
			if(a!=null){
				Element authors=(Element)outDoc.createElement("authors");
				addMetadataEntity(outDoc,authors,null,"Kononowicz;Andrzej",null);
				outMetadata.appendChild(authors);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addMetadataEntity(Document outDoc,Element metadata, String id, String name, String date){
		Element enity=outDoc.createElement("entity");

		if (id!=null){
			Element idE=outDoc.createElement("id");
			idE.setTextContent(id);
			enity.appendChild(idE);
		}
				
		
		if (name!=null){
			Element nameE=outDoc.createElement("name");
			nameE.setTextContent(name);
			enity.appendChild(nameE);
		}
		
		if (date!=null){
			Element dateE=outDoc.createElement("date");
			dateE.setTextContent(date);
			enity.appendChild(dateE);
		}
		
		metadata.appendChild(enity);
	}

	private static void copyHeader(Document inDoc, Document outDoc){	
		Element hOut=(Element)outDoc.getElementsByTagName("header").item(0);
		Element agsEOut=outDoc.createElement("attribute_groups");
		Element hIn=(Element)inDoc.getElementsByTagName("header").item(0);
		copyAttribGroups(hIn,outDoc,agsEOut);	
		hOut.appendChild(agsEOut);
	}
	
	private static void copyAttribGroups(Element inAttribGroups, Document outDoc, Element outAttribGroups){
		NodeList list=inAttribGroups.getElementsByTagName("attrib_group");
		
		for (int i = 0; i < list.getLength(); i++) {
			Element agEIn=(Element)list.item(i);
	
			if (aMap.getProperty(agEIn.getAttribute("class_id"))!=null){
				Element agEOut=outDoc.createElement("attribute_group");
				copyAttribGroup(agEIn,outDoc,agEOut);
				outAttribGroups.appendChild(agEOut);
			}
			
		}
	}
	
	private static void copyAttribGroup(Element inAttribGroup, Document outDoc, Element outAttribGroup){
		outAttribGroup.setAttribute("template", "t1");
		outAttribGroup.setAttribute("type_id", aMap.getProperty(inAttribGroup.getAttribute("class_id")));
		
		NodeList list=inAttribGroup.getElementsByTagName("attrib");
		
		for (int i = 0; i < list.getLength(); i++) {
			Element aEIn=(Element)list.item(i);
			
			if (aMap.getProperty(aEIn.getAttribute("class_id"))!=null){			
				Element aEOut=outDoc.createElement("attribute");
				copyAttrib(aEIn,outDoc,aEOut);
				outAttribGroup.appendChild(aEOut);
			}
			
		}	
	}
	
	private static void copyAttrib(Element inAttrib,Document outDoc,Element outAttrib){
		outAttrib.setAttribute("type_id", aMap.getProperty(inAttrib.getAttribute("class_id")));
		
		if (inAttrib.getElementsByTagName("item").getLength()>0){
			Element vE=outDoc.createElement("value");
			Element iE=outDoc.createElement("items");
			NodeList list=inAttrib.getElementsByTagName("item");
			
			for (int i = 0; i < list.getLength(); i++) {
				iE.appendChild(outDoc.adoptNode(list.item(i).cloneNode(true)));
			}
			
			vE.appendChild(iE);
			outAttrib.appendChild(vE);
		}else{
			Node v=inAttrib.getElementsByTagName("value").item(0);
			outAttrib.appendChild(outDoc.adoptNode(v.cloneNode(true)));
		}
	}	
	
	private static void copyElements(Document inDoc, Document outDoc){
		
		NodeList list=inDoc.getElementsByTagName("element");
		Element esOut=(Element)outDoc.getElementsByTagName("elements").item(0);
		
		
		for (int i = 0; i < list.getLength(); i++) {
			
			Element elementIn=(Element)list.item(i);
			Node boundsIn=elementIn.getElementsByTagName("bounds").item(0);
			
			Element elementOut=outDoc.createElement("element");
			elementOut.setAttribute("id", elementIn.getAttribute("id"));
			elementOut.setAttribute("type", aMap.getProperty(elementIn.getAttribute("class")));			
			
			elementOut.appendChild(outDoc.adoptNode(boundsIn.cloneNode(true)));
			Element agsEOut=outDoc.createElement("attribute_groups");
			
			copyAttribGroups(elementIn,outDoc,agsEOut);
			
			elementOut.appendChild(agsEOut);
			esOut.appendChild(elementOut);
		}				
	}

	private static void copyConnections(Document inDoc, Document outDoc){
		
		NodeList list=inDoc.getElementsByTagName("connection");
		Element connectionsOut=(Element)outDoc.getElementsByTagName("connections").item(0);
		
		
		for (int i = 0; i < list.getLength(); i++) {
			
			Element connectionIn=(Element)list.item(i);
			
			Element connectionOut=outDoc.createElement("connection");
			connectionOut.setAttribute("id", connectionIn.getAttribute("id"));
			connectionOut.setAttribute("type", "SIMPLE_EDGE");
			connectionOut.setAttribute("src",connectionIn.getAttribute("src"));
			connectionOut.setAttribute("dest",connectionIn.getAttribute("dest"));			
			
			Element agsEOut=outDoc.createElement("attribute_groups");
			
			copyAttribGroups(connectionIn,outDoc,agsEOut);
			
			connectionOut.appendChild(agsEOut);
			connectionsOut.appendChild(connectionOut);
		}				
		
	}
	
	private static void copySubpaths(Document inDoc){
		//TODO
	}
	
}
