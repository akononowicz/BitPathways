/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.*;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.Rectangle;

import java.io.Serializable;

public class LabelListItemCellRenderer extends AbstractLabelRowCellRenderer
    implements TableCellRenderer, Serializable{
	
    protected Border cellBorder = new EmptyBorder(1, 30, 1, 1); 
    
    protected JButton removeButton;
    

    public LabelListItemCellRenderer() {
	    super();
		setOpaque(true);
        setBorder(getCellBorder());
        setBackground(new Color(250,250,250));
        removeButton=new JButton("Del");
        add(removeButton,BorderLayout.EAST);
    }

    protected  Border getCellBorder() {
    	return cellBorder;
    }
    
    public Rectangle getDelRectangle(){
    	return removeButton.getBounds();
    }

}