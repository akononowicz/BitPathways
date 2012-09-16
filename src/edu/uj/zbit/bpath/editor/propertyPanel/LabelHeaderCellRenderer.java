/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.*;

import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import java.io.Serializable;

public class LabelHeaderCellRenderer extends AbstractLabelRowCellRenderer
    implements TableCellRenderer, Serializable{
	

    public LabelHeaderCellRenderer() {
	    super();
	    Font oldFont=getFont();			
		Font newFont=new Font(oldFont.getFamily(),Font.BOLD,oldFont.getSize());
		labelComp.setFont(newFont);
    }

}