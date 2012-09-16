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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.Serializable;

public abstract class AbstractLabelRowCellRenderer extends JPanel
    implements TableCellRenderer, Serializable{
	
    protected Border cellBorder = new EmptyBorder(1, 10, 1, 1); 
   protected final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 10, 1, 1);
    
    protected Color unselForeground; 
    protected Color unselBackground; 
    
    protected JLabel labelComp;


    public AbstractLabelRowCellRenderer() {
	super();
		setOpaque(true);
		setLayout(new BorderLayout());
        setBorder(getCellBorder());
        setBackground(new Color(240,240,240));

        labelComp=new JLabel();
        labelComp.setBackground(new Color(240,240,240));
        add(labelComp,BorderLayout.CENTER);
        
    }

    protected  Border getCellBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        } else {
            return cellBorder;
        }
    	//return cellBorder;
    }

    
    public void setForeground(Color c) {
        super.setForeground(c); 
        unselForeground = c; 
    }
    
    public void setBackground(Color c) {
        super.setBackground(c); 
        unselBackground = c; 
    }

    public void updateUI() {
        super.updateUI(); 
        setForeground(null);
        setBackground(null);
    }
    

    public Component getTableCellRendererComponent(JTable table, Object value,
                          boolean isSelected, boolean hasFocus, int row, int column) {

		if (isSelected) {
		   super.setForeground(table.getSelectionForeground());
		   super.setBackground(table.getSelectionBackground());
		   labelComp.setForeground(table.getSelectionForeground());
		}
		else {
		    super.setForeground((unselForeground != null) ? unselForeground 
		                                                       : table.getForeground());
		    labelComp.setForeground((unselForeground != null) ? unselForeground 
                    : table.getForeground());
		    super.setBackground((unselBackground != null) ? unselBackground 
		                                                       : table.getBackground());
		}
	
		if (hasFocus) {
		    setBorder( getCellBorder() );
		    if (table.isCellEditable(row, column)) {
		        super.setForeground( UIManager.getColor("Table.focusCellForeground") );
		        labelComp.setForeground( UIManager.getColor("Table.focusCellForeground") );
		        super.setBackground( UIManager.getColor("Table.focusCellBackground") );
		    }
		} else {
		    setBorder(getCellBorder());
		}
	
	        setValue(value); 
	
		return this;
    }
    

    public boolean isOpaque() { 
		Color back = getBackground();
		Component p = getParent(); 
		if (p != null) { 
		    p = p.getParent(); 
		}
		// p should now be the JTable. 
		boolean colorMatch = (back != null) && (p != null) && 
		    back.equals(p.getBackground()) && 
				p.isOpaque();
		return !colorMatch && super.isOpaque(); 
    }

/*
    public void validate() {}
    public void revalidate() {}
    public void repaint(long tm, int x, int y, int width, int height) {}
    public void repaint(Rectangle r) { }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {	
	if (propertyName=="text") {
	    super.firePropertyChange(propertyName, oldValue, newValue);
	}
    }
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) { }
   */ 
    protected void setValue(Object value) {
    	labelComp.setText((value == null) ? "" : value.toString());
    }
/*
    public static class UIResource extends AbstractLabelRowCellRenderer 
        implements javax.swing.plaf.UIResource
    {
    }
*/
	public void setCellBorder(Border cellBorder) {
		this.cellBorder = cellBorder;
	}

}