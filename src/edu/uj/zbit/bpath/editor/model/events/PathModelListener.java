/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.events;

public interface PathModelListener {
	public void elementAdded(PathModelEvent e);
	public void elementToBeAdded(PathModelEvent e);
	public void elementSelected(PathModelEvent e);
	public void elementRemoved(PathModelEvent e);
	public void elementChanged(PathModelEvent e);
	public void modelLoaded(PathModelEvent e);
	
	
}
