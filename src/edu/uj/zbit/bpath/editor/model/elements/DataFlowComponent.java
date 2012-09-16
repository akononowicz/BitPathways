/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

public class DataFlowComponent extends FlowChartNode {
	
	public String getType(){return "TASK";}
    public String getTypeName(){return BpathMainFrame.messages.getString("ELEMENT.COMPONENT");}
    public String getWizardName(){return "edu.uj.zbit.bpath.editor.dialogs.NewTaskWizard";}
	
	/* Farben */
	private Color bgColor = new Color(199, 202, 234);
	private Color bgColorArzt = new Color(255, 202, 201);
	private Color bgColorBehandlung = new Color(168, 255, 85);	
	private Color bgColorPflege = new Color(233, 151, 55);
	
  	private Color colorConnector = new Color(102, 102, 197);
  	private Color selectionColor=new Color(180,7,7);

  	
  	public DataFlowComponent(String id){
  		super(id);
  		height=30;
  	    width=100;
  	}
  	
  	public Point[] getHotPoints(){
  		Point[] hotpoints=new Point[5];
  	    hotpoints[HP_NORTH_ID]=new Point(x+pos_x(12),y+pos_y(0));
  	    hotpoints[HP_EAST_ID]=new Point(x+pos_x(24),y+pos_y(6));
  	    hotpoints[HP_SOUTH_ID]=new Point(x+pos_x(12),y+pos_y(12));
  	    hotpoints[HP_WEST_ID]=new Point(x+pos_x(0),y+pos_y(6));
  	    hotpoints[HP_RESIZE_ID]=new Point(x+pos_x(24),y+pos_y(12));
  	    return hotpoints;
  	}
  	
	protected void drawShape(Graphics2D g2){
	    g2.setFont(vertexFont);


	    // filling of the box
	    g2.setColor(getColor4Component());
	    g2.fillRect(x, y, width, height);
	    g2.setColor(Color.black);

	    if (!selected)
	      g2.setColor(Color.black);
	    else {	     
	      g2.setColor(selectionColor);
	      drawHotRegions(g2);
	    }
	    g2.drawRect(x,y,pos_x(24),pos_y(12));
	    
	    // write text

	    drawText(g2,getLabel());

	    // illegal position
	    if (!correctPos){
	      g2.setColor(Color.black);
	      g2.drawLine(x,y,x+width,y+height);
	      g2.drawLine(x,y+height,x+width,y);
	    }
  }
	
	protected Color getColor4Component(){
		String category=getCategory();
		if (
			(category.equalsIgnoreCase("Arztdienst"))||
			(category.equalsIgnoreCase("Lekarz"))||
			(category.equalsIgnoreCase("Physician"))
		   )return bgColorArzt;
		else if (
			(category.equalsIgnoreCase("Behandlungsdienste"))||
			(category.equalsIgnoreCase("Technik medyczny"))||
			(category.equalsIgnoreCase("Technician"))
		   ) return bgColorBehandlung;
		else if (
			(category.equalsIgnoreCase("Pflegedienst"))||
			(category.equalsIgnoreCase("Pielêgniarka"))||
			(category.equalsIgnoreCase("Nurse"))
		   ) return bgColorPflege;
		
		return bgColor;
	}


		  //----------------------------------------------------------------------------

		  /**
		   * Gibt den Verankerungspunkt für ein Kanteende zurück.
		   * @param hotRegion int Nummer des Anschlusspunkt
		   * @return Point Verankerungspunkt
		   */
		  public Point getConnectionPoint(int hotRegion){
			  
		    if ((hotRegion>-1)&&(hotRegion<4))
		      return getHotPoints()[hotRegion];
		    else
		      return new Point(x+width/2,y+height/2);
		  }
		 
		  
		  public String getCategory() {
			  
			  return "popraw";
			  /*try{
				  Object cat=attributes.getValue(Dict.ELEMENT_CATEGORY);
				  if (cat!=null) return cat.toString();
			  }catch(Exception e){}
			  return "";*/
		  }
		  
		  
}
