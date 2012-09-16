/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

public class DataFlowSubpath extends FlowChartNode {

	public String getType(){return "SUBPATH";}
    public String getTypeName(){return BpathMainFrame.messages.getString("ELEMENT.SUBPATHDEFAULTLABEL");}
    public String getWizardName(){return "edu.uj.zbit.bpath.editor.dialogs.SubpathWizard";}
	
	private Color bgColor = new Color(255,158,46);
  	private Color selectionColor=new Color(0, 192, 0);
	
	public DataFlowSubpath(String _id){
  		super(_id);
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
		    g2.setColor(bgColor);
		    g2.fillRect(x, y, width, height);
		    g2.setColor(Color.black);

		    if (!selected)
		      g2.setColor(Color.black);
		    else {	     
		      g2.setColor(selectionColor);
		      drawHotRegions(g2);
		    }
		    g2.drawRect(x,y,pos_x(24),pos_y(12));
		    g2.drawLine(x+pos_x(3), y, x+pos_x(3), y+pos_y(12));
		    g2.drawLine(x+pos_x(21), y, x+pos_x(21), y+pos_y(12));		    
		    
		    // write text

		    drawText(g2,getLabel(),pos_x(4)+2*TEXT_MARGIN);

		    // illegal position
		    if (!correctPos){
		      g2.setColor(Color.black);
		      g2.drawLine(x,y,x+width,y+height);
		      g2.drawLine(x,y+height,x+width,y);
		    }
	}

	public Point getConnectionPoint(int hotRegion){
		  
	    if ((hotRegion>-1)&&(hotRegion<4))
	      return getHotPoints()[hotRegion];
	    else
	      return new Point(x+width/2,y+height/2);
	}
	

}
