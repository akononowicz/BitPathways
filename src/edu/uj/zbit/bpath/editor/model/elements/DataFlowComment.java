/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import edu.uj.zbit.bpath.editor.BpathMainFrame;

public class DataFlowComment extends FlowChartNode {

	public String getType(){return "COMMENT";}
    public String getTypeName(){return BpathMainFrame.messages.getString("ELEMENT.COMMENT");}
    public String getWizardName(){return null;}
	
	private Color bgColor = new Color(0, 192, 0);
  	private Color selectionColor=new Color(180,7,7);
	
	public DataFlowComment(String _id){
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



		    if (!selected)
		      g2.setColor(Color.black);
		    else {	     
		      g2.setColor(selectionColor);
		      drawHotRegions(g2);
		    }
		    
		    g2.drawLine(x, y, x, y+pos_y(12));	    
		    g2.drawLine(x, y, x+10, y);
		    g2.drawLine(x, y+pos_y(12), x+10, y+pos_y(12));		    

		    Stroke oldStroke=g2.getStroke();
		    float dash1[] = {7.0f};
		    BasicStroke dashedStroke=new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
		    g2.setStroke(dashedStroke);   
		    g2.drawLine(x, y+pos_y(6), x-40, y+pos_y(6));		    
		    g2.setStroke(oldStroke);
		    
		    // write text

		    drawText(g2,getLabel());

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
