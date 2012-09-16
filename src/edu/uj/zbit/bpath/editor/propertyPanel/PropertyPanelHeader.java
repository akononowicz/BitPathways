/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.propertyPanel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class PropertyPanelHeader extends JPanel{

	String name;
	PropertyPanel panel;
	boolean active=false;
	Header header=new Header();
	public static Color GROUP_HEADER=new Color(199, 202, 234);
	public static Color CLASS_HEADER=new Color(135,180,251);
	private Color hbgColor;
	
	class Header extends Canvas{
		
		
		Header(){
			hbgColor=GROUP_HEADER;
			setBackground(hbgColor);
			this.addMouseListener(
					new MouseAdapter(){
						public void mouseClicked(MouseEvent e){
							panel.fireSelected(name);
						}
					}
			);	
		}
		
		public void paint(Graphics g){
			Graphics2D g2=(Graphics2D) g;
			
			if (hbgColor==CLASS_HEADER){
				Font oldFont=g2.getFont();
				Font newFont=new Font(oldFont.getName(),Font.BOLD,oldFont.getSize());
				g2.setFont(newFont);
				g2.drawString(name,10,15);
				g2.setFont(oldFont);
			}else{
				g2.drawString(name,10,15);
			}
		}
		
		public Dimension getPreferredSize(){
			return new Dimension(100,20);
		}
	}
	
	public PropertyPanelHeader(PropertyPanel _panel,String _name){
		name=_name;
		panel=_panel;
		
		setLayout(new BorderLayout());
		add(header,BorderLayout.NORTH);
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		header.repaint();
	}

	public PropertyPanel getPanel() {
		return panel;
	}

	public void setPanel(PropertyPanel panel) {
		this.panel = panel;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Color getBgColor() {
		return hbgColor;
	}

	public void setHBgColor(Color bgColor) {
		this.hbgColor = bgColor;
		header.setBackground(bgColor);
		repaint();
	}
	
}
