/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.dialogs;


import javax.swing.JDialog;
import javax.swing.JFrame;

import edu.uj.zbit.bpath.editor.model.WizardAttributeSet;

public abstract class AddElementWizard extends JDialog {

	boolean accepted=false;
	WizardAttributeSet attribs=new WizardAttributeSet();
	
	public AddElementWizard(JFrame parent) {
		super (parent,true);
	}

	public boolean isAccepted(){
		return accepted;
	}

	public WizardAttributeSet getAttributeSet(){
		return attribs;
	}
	
}
