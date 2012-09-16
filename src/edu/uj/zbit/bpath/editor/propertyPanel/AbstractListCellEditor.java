/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.uj.zbit.bpath.editor.model.AttribGroup;

public abstract class AbstractListCellEditor extends AbstractCellEditor {
	
	final JPopupMenu menu=new JPopupMenu();
	protected AttribGroup attribGroup;
	protected JTableX tableX;
	
	public AbstractListCellEditor(AttribGroup _attribGroup){
		super();
		attribGroup=_attribGroup;
		JMenuItem jmenu=new JMenuItem("Löschen");
		jmenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				attribGroup.removeRowFromList(tableX.getSelectedRow());
			}
		});
		menu.add(jmenu);
		
	}
	
	protected void rightClick(MouseEvent evnt){
		if (tableX==null){
			tableX=(JTableX)evnt.getComponent().getParent();	
		}
		menu.show(evnt.getComponent(),evnt.getX(),evnt.getY());
	
		
		
	}
	
}
