/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uj.zbit.bpath.editor.model.AttribGroup;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;

/**
 * <p>
 *  Data flow element
 * </p>
 *
 * @author Andrzej Kononowicz
 * @version 1.0
 */


public interface  FlowChartElement {
	abstract String getId();
	abstract String getType();
	abstract String getTypeName();
	abstract AttribGroupSet getAttributes();
	abstract void setAttributes(AttribGroupSet attributes);
	abstract Element getElementAsDOM(Document doc);
	abstract Element getElementAsDOM();	
}
