/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.table.DefaultTableCellRenderer;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class TestQuestionCellRenderer extends DefaultTableCellRenderer{

	public TestQuestionCellRenderer() {
		super();
	}
	
	protected void setValue(Object value){
		
		if (value instanceof Element){
			Element testElement=(Element)value;
			Element questionTextElement=(Element)testElement.getElementsByTagName("name").item(0);
			super.setValue(questionTextElement.getChildNodes().item(0).getNodeValue());		
		}else{
			super.setValue("");
		}
		
	}

}
