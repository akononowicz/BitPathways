/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.LineEnumeration;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;

public class DataFlowBranch extends FlowChartNode {

	public String getType(){return "DECISION";}
    public String getTypeName(){return BpathMainFrame.messages.getString("ELEMENT.BRANCH");}
    public String getWizardName(){return null;}
    
  	private Color selectionColor=new Color(180,7,7);
	
	public DataFlowBranch(String id){
		super(id);
		height=20;
		width=50;	
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
		
		int[] p_xs={x+pos_x(0),x+pos_x(12),x+pos_x(24),x+pos_x(12)};
		int[] p_ys={y+pos_y(6),y+pos_y(0),y+pos_y(6),y+pos_y(12)};
		
		Polygon branchSpape=new Polygon(p_xs,p_ys,p_xs.length);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillPolygon(branchSpape);
		
		if (!selected)
		  g2.setColor(Color.black);
		else {	     
		  g2.setColor(selectionColor);
		  drawHotRegions(g2);
		}
		
		g2.drawPolygon(branchSpape);
		
		try{
			drawText(g2,getLabel());
		}catch(Exception e){
			// old versions does not support labeling branches
		}

	}

	  public Point getConnectionPoint(int hotRegion){
		  	
		    if ((hotRegion>-1)&&(hotRegion<4))
		      return getHotPoints()[hotRegion];
		    else
		      return new Point(x+width/2,y+height/2);
	  }
	
	  protected void drawText(Graphics2D g, String str){
			 
			
			 LineEnumeration linewrapper=new LineEnumeration(g,vertexFont,str,2*width);
			 int lineheight=g.getFontMetrics().getHeight();
			 int baseline=y+pos_y(12)+1+lineheight;
			 
			 while( (linewrapper.hasMoreElements())
				   ){
				 String toPrint=linewrapper.nextElement().toString();
				 int xPos=centerText(g,toPrint);
				 g.drawString(toPrint,xPos,baseline);
				 baseline+=lineheight;
					
			 }
		 }
	  
}
