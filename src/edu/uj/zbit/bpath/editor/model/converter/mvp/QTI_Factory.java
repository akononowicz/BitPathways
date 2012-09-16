/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.converter.mvp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.propertyPanel.TestQuestion;

/**
 * Converts questions in native format into CASUS specific QTI format
 * 
 * @author Andrzej Kononowicz
 *
 */

public class QTI_Factory {

	public final static String QTI_NAMESPACE="http://www.imsglobal.org/xsd/imsqti_v2p1";
	
	public static Element getQuestionAsQTI(Document doc, TestQuestion question, String nodeId, String questionId){

		
		
		// AssessmentItem
		
		Element assessmentItemElement=doc.createElementNS(QTI_NAMESPACE, "qti:assessmentItem");
		
		assessmentItemElement.setAttribute("adaptive", "false");
		assessmentItemElement.setAttribute("timeDependent", "false");
		assessmentItemElement.setAttribute("title", question.getName());
		assessmentItemElement.setAttribute("identifier", "QTI_"+nodeId+"_"+questionId);
		
		// ResponseDeclaration
		
		Element responseDeclarationElement=doc.createElementNS(QTI_NAMESPACE, "qti:responseDeclaration");
		responseDeclarationElement.setAttribute("baseType", "identifier");
		responseDeclarationElement.setAttribute("identifier", "RESP_"+nodeId+"_"+questionId);
		responseDeclarationElement.setAttribute("cardinality", "multiple");
		assessmentItemElement.appendChild(responseDeclarationElement);
		
		Element correctResponseElement=doc.createElementNS(QTI_NAMESPACE, "qti:correctResponse");
		responseDeclarationElement.appendChild(correctResponseElement);
		
		for (int i = 0; i < 4; i++) {
			if (question.isAnswerCorrect(i)){
				Element valueElement=doc.createElementNS(QTI_NAMESPACE, "qti:value");
				correctResponseElement.appendChild(valueElement);
				valueElement.setTextContent("sc_"+nodeId+"_"+questionId+"_"+i);
			}
		}
		
		// ItemBody
		
		Element itemBodyElement=doc.createElementNS(QTI_NAMESPACE, "qti:itemBody");
		assessmentItemElement.appendChild(itemBodyElement);
		
		Element choiceInteractionElement=doc.createElementNS(QTI_NAMESPACE, "qti:choiceInteraction");
		choiceInteractionElement.setAttribute("responseIdentifier", "RESP_"+nodeId+"_"+questionId);
		choiceInteractionElement.setAttribute("shuffle", "false");
		choiceInteractionElement.setAttribute("maxChoices", "4");
		itemBodyElement.appendChild(choiceInteractionElement);
		
		for (int i = 0; i < 4; i++) {

			Element scElement=doc.createElementNS(QTI_NAMESPACE, "qti:simpleChoice");
			choiceInteractionElement.appendChild(scElement);
			scElement.setAttribute("identifier", "sc_"+nodeId+"_"+questionId+"_"+i);
			scElement.setAttribute("fixed", "false");				
			scElement.setTextContent(question.getAnswerText(i));

		}
		
		
		return assessmentItemElement;
	}
	
}
