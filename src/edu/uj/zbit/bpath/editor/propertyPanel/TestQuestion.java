/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class TestQuestion {
	
	private String name;
	private String questionText;
	private TestQuestionAnswer[] answers=new TestQuestionAnswer[4];
	private Document doc;
	
	public TestQuestion(){
		
		for (int i = 0; i < answers.length; i++) {
			answers[i]=new TestQuestionAnswer();
		}
	}
	
	public TestQuestion(Element testElement){
		
		doc=testElement.getOwnerDocument();
		
		// general properties
		
		Element nameElement=(Element)testElement.getElementsByTagName("name").item(0);
		name=nameElement.getChildNodes().item(0).getNodeValue();
		
		Element questionTextElement=(Element)testElement.getElementsByTagName("question_text").item(0);
		questionText="";
		NodeList questionTextChildern=questionTextElement.getChildNodes();
		for (int i = 0; i < questionTextChildern.getLength(); i++) {
			questionText+=questionTextChildern.item(i).getNodeValue();
		}
		
		// answers
		
		for (int i = 0; i < answers.length; i++) {
			answers[i]=new TestQuestionAnswer();
		}
		
		NodeList anwersChildren=testElement.getElementsByTagName("answer");
		
		for (int i = 0; i < anwersChildren.getLength(); i++) {
			Element answerElement=(Element)anwersChildren.item(i);
			// answer
			TestQuestionAnswer answer=new TestQuestionAnswer();
			answer.setCorrect(new Boolean(answerElement.getAttribute("correct")).booleanValue());
			
			String answerText="";
			NodeList answerTextElementChildern=answerElement.getElementsByTagName("answer_text").item(0).getChildNodes();
			for (int j = 0; j < answerTextElementChildern.getLength(); j++) {
				answerText+=answerTextElementChildern.item(j).getNodeValue();
			}
			answer.setAnswerText(answerText);

			String answerComment="";
			NodeList answerCommentElementChildern=answerElement.getElementsByTagName("answer_comment").item(0).getChildNodes();
			for (int j = 0; j < answerCommentElementChildern.getLength(); j++) {
				answerComment+=answerCommentElementChildern.item(j).getNodeValue();
			}
			answer.setAnswerComment(answerComment);
			
			answers[i]=answer;
		}
	}
	
	private class TestQuestionAnswer{
		private boolean correct=false;
		private String answerText="";
		private String answerComment="";
		
		public TestQuestionAnswer(){}
		
		public TestQuestionAnswer(boolean _correct,String _answerText, String _answerComment){
			correct=_correct;
			answerText=_answerText;
			answerComment=_answerComment;
		}
		
		public String getAnswerComment() {
			return answerComment;
		}
		public void setAnswerComment(String answerComment) {
			this.answerComment = answerComment;
		}
		public String getAnswerText() {
			return answerText;
		}
		public void setAnswerText(String answerText) {
			this.answerText = answerText;
		}
		public boolean isCorrect() {
			return correct;
		}
		public void setCorrect(boolean correct) {
			this.correct = correct;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public void setAnswer(int pos, boolean correct, String answerText,String answerComment){
		TestQuestionAnswer answer=new TestQuestionAnswer(correct,answerText,answerComment);
		answers[pos]=answer;
	}
	
	public boolean isAnswerCorrect(int i){
		return answers[i].isCorrect();
	}
	
	public String getAnswerComment(int i){
		return answers[i].getAnswerComment();
	}
	
	public String getAnswerText(int i){
		return answers[i].getAnswerText();
	}	
	
	public Element getAsDOM(){
	

		try{
			if (doc==null){
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				doc=factory.newDocumentBuilder().newDocument();
			}
			
			Element testElement=doc.createElement("test");

			Element nameElement=doc.createElement("name");
			Text nameValue=doc.createTextNode(name);
			nameElement.appendChild(nameValue);
			testElement.appendChild(nameElement);
			
			Element questionTextElement=doc.createElement("question_text");
			Text questionTextValue=doc.createTextNode(questionText);
			questionTextElement.appendChild(questionTextValue);
			testElement.appendChild(questionTextElement);
			
			Element answersElement=doc.createElement("answers");
			testElement.appendChild(answersElement);
			
			for(int i=0;i<answers.length;i++){
				Element answerElement=doc.createElement("answer");
				answerElement.setAttribute("correct", ""+answers[i].correct);
				answersElement.appendChild(answerElement);
				
				Element answerTextElement=doc.createElement("answer_text");				
				Text answerTextValue=doc.createTextNode(answers[i].answerText);
				answerTextElement.appendChild(answerTextValue);
				answerElement.appendChild(answerTextElement);

				Element answerCommentElement=doc.createElement("answer_comment");
				Text answerCommentValue=doc.createTextNode(answers[i].answerComment);
				answerCommentElement.appendChild(answerCommentValue);
				answerElement.appendChild(answerCommentElement);
			}
			
			return testElement;
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return (Element)null;
	}
	
}
