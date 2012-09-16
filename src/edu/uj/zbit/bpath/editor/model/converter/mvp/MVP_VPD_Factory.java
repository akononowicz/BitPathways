/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter.mvp;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axis.encoding.ser.ArrayDeserializer.ArrayListExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


import edu.uj.zbit.bpath.editor.model.elements.DataFlowBranch;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEnd;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.editor.propertyPanel.TestQuestion;

public class MVP_VPD_Factory {
	
	public final static String NAMESPACE="http://ns.medbiq.org/virtualpatientdata/v1/";
	public final static String XHTML_NAMESPACE="http://www.w3.org/1999/xhtml";
	
	

	//-----------------------------------------------------------------------------------------------	
	
	static public Document createVirtualPatientData(FlowChartNode[] nodes){

		Document document=null;
		
		// create empty document	
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			 document = documentBuilder.newDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root=document.createElementNS(NAMESPACE, "VirtualPatientData");
		root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		root.setAttribute("xmlns:qti", "http://www.imsglobal.org/xsd/imsqti_v2p1");
		root.setAttribute("xsi:schemaLocation", "http://ns.medbiq.org/virtualpatientdata/v1/ virtualpatientdata.xsd http://www.imsglobal.org/xsd/imsqti_v2p1 http://www.imsglobal.org/xsd/imsqti_v2p1.xsd");
		
		document.appendChild(root);
		
		String text,id;
		
		ArrayList<Element> myQTIQuestionList=new ArrayList<Element>();
		
		for (FlowChartNode node : nodes) {
		
			if (isClassAccepted(node.getClass())){
		
				// create VPD text
				id=node.getId();
				text=node.getAttributes().getValue("card", "narrative")[0].toString();		
				createVPDText(root,"INFO",id,text);
				
				// check whether contains questions
				
				Object[] mcqs=node.getAttributes().getSpecialValue("mcq_questions");
				
				for (int i = 0; i < mcqs.length; i++) {
					if (mcqs[i] instanceof Element){
						// create XtensibleInfo
						TestQuestion question=new TestQuestion((Element)mcqs[i]);
						myQTIQuestionList.add(QTI_Factory.getQuestionAsQTI(document, question,node.getId(),""+i));
						// create question VPDText
							
							// create QEST
							Element qestElement=document.createElement("VPDText");
							qestElement.setAttribute("id", "QEST_"+node.getId()+"_"+i);
							qestElement.setAttribute("textType", "narrative");
							Element div1Element=document.createElement("div");
							div1Element.setAttribute("xmlns", XHTML_NAMESPACE);
							div1Element.setTextContent(question.getQuestionText());
							qestElement.appendChild(div1Element);
							root.appendChild(qestElement);
							
							// create ANSWERCOMMENT
							Element acElement=document.createElement("VPDText");
							acElement.setAttribute("id", "ANSWERCOMMENT_"+node.getId()+"_"+i);
							acElement.setAttribute("textType", "narrative");
							Element div2Element=document.createElement("div");
							div2Element.setAttribute("xmlns", XHTML_NAMESPACE);
							div2Element.setTextContent(question.getAnswerComment(0));
							acElement.appendChild(div2Element);
							root.appendChild(acElement);
							
					}
				}
				
				// add expert comments (if present)
				
				String expert=node.getAttributes().getSingleValueAsString("card", "author_comment");
				
				if((expert!=null)&&(expert.length()>0)){
					createVPDText(root,"EXPT",id,expert);
				}
				
			}
		}
		
		// add XtensibleInfo to root
		
		if (myQTIQuestionList.size()>0){
			
			Element xtensibleInfo=document.createElement("XtensibleInfo");
			root.appendChild(xtensibleInfo);
			
			for (Element element : myQTIQuestionList) {
				xtensibleInfo.appendChild(element);
			}
		
		}
		
		return document;
	}

	//-----------------------------------------------------------------------------------------------	
	
	private static void createVPDText(Element root,String prefix, String id,String text){
		Element vpdElement=root.getOwnerDocument().createElement("VPDText");
		vpdElement.setAttribute("id", prefix+"_"+id);
		vpdElement.setAttribute("textType", "narrative");
		root.appendChild(vpdElement);
		
		try{
			
			InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(htmlize(text)));			
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(is);			
			
			Element html=(Element)root.getOwnerDocument().adoptNode(d.getFirstChild().cloneNode(true));
			html.setAttribute("xmlns", XHTML_NAMESPACE);
			
			vpdElement.appendChild(html);
		}catch(Exception e){
			
			e.printStackTrace();
		}
	}
	
//-----------------------------------------------------------------------------------------------	
	
	private static String htmlize(String str){
		String result=str;
			
		result=result.replaceAll("!spacja", "&nbsp;");
		result=result.replaceAll("&lt;", "<");
		result=result.replaceAll("&gt;", ">");		
		result=result.replaceAll("&amp;", "&");	
		
		return "<div>"+result+"</div>";
	}

	
//----------------------------------------------------------------------------------------------- 
	
	public static boolean isClassAccepted(Class elementClass){
		
		HashSet<Class> acceptedVPTextTypes=new HashSet<Class>();
		acceptedVPTextTypes.add(DataFlowStart.class);
		acceptedVPTextTypes.add(DataFlowEnd.class);
		acceptedVPTextTypes.add(DataFlowComponent.class);
		acceptedVPTextTypes.add(DataFlowBranch.class);
		
		return acceptedVPTextTypes.contains(elementClass);
	}
	
}
