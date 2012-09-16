/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.events;

import org.w3c.dom.Node;


public class PathModelChangeEvent extends PathModelEvent {
	
	Object oldValue,newValue;
	
	public PathModelChangeEvent(String source, Object _oldValue, Object _newValue){
		super(source);
		oldValue=_oldValue;
		newValue=_newValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Node oldValue) {
		this.oldValue = oldValue;
	}
}
