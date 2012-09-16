/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.*;
import java.awt.geom.*;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



import sun.awt.HorizBagLayout;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.AttribGroupSet;
import edu.uj.zbit.bpath.editor.model.XMLProcessor;
import edu.uj.zbit.bpath.editor.model.AttribGroup;

/**
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public class DataFlowEdge implements FlowChartElement{

  // Id 
 protected String id;
	
  // Source Vertex
  protected FlowChartNode srcVertex;
  // Gewählter Verknüpfungspunkt am Ausgangsknoten
  protected int srcHotRegion;

  //Target Vertex
  protected FlowChartNode destVertex;
  // Gewählter Verknüpfungspunkt am Zielknoten
  protected int destHotRegion;

  protected boolean selected=false;

  protected static final int ARROW_HEAD_SIZE=10;
  protected static final int HOT_REC_SIZE=2;
  protected static final int EDGE_LABEL_MARGIN=5;
  
  protected static final int ORIENTATION_UP=0;
  protected static final int ORIENTATION_DOWN=1;
  protected static final int ORIENTATION_LEFT=2;
  protected static final int ORIENTATION_RIGHT=3;
  
  // Kantenfarbe
  protected Color edgeColor=Color.black;
  protected Color selectionColor=new Color(159,9,9);
  
  protected int x1,x2,y1,y2;
  
  protected Line2D.Double[] lines = new Line2D.Double[10];
  protected int lineIndex=0;
  
  private AttribGroupSet attributes;
  
  public AttribGroupSet getAttributes(){
	  return attributes;
  }
  
  public void setAttributes(AttribGroupSet attributes){
	  this.attributes=attributes;
  }

  // --------- Constructor ----------------------------------------------------

  public DataFlowEdge(String id,FlowChartNode v1,  FlowChartNode v2) {
    this.id=id;
	this.srcVertex=v1;
    this.destVertex=v2;

  }

  // ----------- Paint --------------------------------------------------------

  public void paint(Graphics g)
   {
     Point p;
     p=srcVertex.getConnectionPoint(srcHotRegion);
     x1=p.x; y1=p.y;
     p=destVertex.getConnectionPoint(destHotRegion);
     x2=p.x; y2=p.y;

     drawEdgeFromTo(g);
     drawLabel(g);

   } // end of paint-function

   /**
    * Draws an edge (arrow) from one point to another
    * @param p1 Point
    * @param p2 Point
    */
   protected void drawEdgeFromTo(Graphics g){
	   	 lineIndex=0;
	   
	     Graphics2D g2=(Graphics2D)g;
	    
	     Stroke oldStroke=g2.getStroke();

	     if (selected){
	       g.setColor(selectionColor);
	       float dash1[] = {7.0f};
	       BasicStroke dashedStroke=new BasicStroke(1.0f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
	       g2.setStroke(dashedStroke);
	     }
	     else g.setColor(edgeColor);

	     
	     // identify the section
	     
	     Rectangle r1=srcVertex;
	     Rectangle r2=destVertex;
	     
	     if (r2==null) r2=new Rectangle((int)x2,(int)y2,1,1);
	     
	     Point p1=new Point(r1.x+r1.width/2,r1.y+r1.height/2);
	     Point p2=new Point(r2.x+r2.width/2,r2.y+r2.height/2);
	     
	     if (r2.y<p1.y){ //1,5,6,7
	    	 if (r2.x+r2.width<r1.x){ //5

	    		 srcHotRegion=3;
	    		 destHotRegion=1;
	    		 Point lp1=srcVertex.getConnectionPoint(srcHotRegion);
	    		 
	    		 //g2.drawLine(lp1.x,lp1.y,lp1.x,p2.y);
	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,lp1.x,p2.y);
	    		 //g2.drawLine(lp1.x,p2.y,r2.x+r2.width,r2.y+r2.height/2);
	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,p2.y,r2.x+r2.width,r2.y+r2.height/2);
	    		 
	    		 drawArrowHead(g2,r2.x+r2.width,r2.y+r2.height/2,ORIENTATION_LEFT);
	    		 
	    	 }else{
	    		 if (r2.x>=r1.x+r1.width){ //1

	    			 srcHotRegion=1;
		    		 destHotRegion=3;
		    		 
		    		 Point lp1=srcVertex.getConnectionPoint(srcHotRegion);
		    		 
		    		 //g2.drawLine(lp1.x,lp1.y,lp1.x,p2.y);
		    		 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,lp1.x,p2.y);
		    		 //g2.drawLine(lp1.x,p2.y,r2.x,r2.y+r2.height/2);		 
		    		 lines[lineIndex++]=new Line2D.Double(lp1.x,p2.y,r2.x,r2.y+r2.height/2);
		    		 
		    		 drawArrowHead(g2,r2.x,r2.y+r2.height/2,ORIENTATION_RIGHT);
	    		 }
	    		 else{ 
	    			 if (p2.x<p1.x){ //6
	    				 
	    				 srcHotRegion=3;
	    	    		 destHotRegion=3;
	    	    		 Point lp1=srcVertex.getConnectionPoint(srcHotRegion);
	    	    		 
	    	    		 if (r2.x<r1.x){
	    	    			 //g2.drawLine(lp1.x,lp1.y,r2.x-10,lp1.y);
	    	    			 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,r2.x-50,lp1.y);
	    	    			 lp1=new Point(r2.x-50,lp1.y);
	    	    		 }
	    	    		 
	    	    		 //g2.drawLine(lp1.x,lp1.y,lp1.x,r2.y+r2.height/2);
	    	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,lp1.x,r2.y+r2.height/2);
	    	    		 //g2.drawLine(lp1.x,r2.y+r2.height/2,r2.x,r2.y+r2.height/2);
	    	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,r2.y+r2.height/2,r2.x,r2.y+r2.height/2);
	    	    		 drawArrowHead(g2,r2.x,r2.y+r2.height/2,ORIENTATION_RIGHT);
	    	    		 
	    			 }else{ //7
	    				 srcHotRegion=1;
	    	    		 destHotRegion=1;
	    	    		 
	    	    		 Point lp1=srcVertex.getConnectionPoint(srcHotRegion);
	    	    		 
	    	    		 if (r2.x+r2.width>=r1.x+r1.width){
	    	    			 //g2.drawLine(lp1.x,lp1.y,r2.x+r2.width+10,lp1.y);
	    	    			 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,r2.x+r2.width+50,lp1.y);
	    	    			 lp1=new Point(r2.x+r2.width+50,lp1.y);
	    	    		 }
	    	    		 
	    	    		 //g2.drawLine(lp1.x,lp1.y,lp1.x,r2.y+r2.height/2);
	    	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,lp1.x,r2.y+r2.height/2);
	    	    		 //g2.drawLine(lp1.x,r2.y+r2.height/2,r2.x+r2.width,r2.y+r2.height/2);
	    	    		 lines[lineIndex++]=new Line2D.Double(lp1.x,r2.y+r2.height/2,r2.x+r2.width,r2.y+r2.height/2);
	    	    		 drawArrowHead(g2,r2.x+r2.width,r2.y+r2.height/2,ORIENTATION_LEFT);
	    			 }
	    		 }
	    	 }
	     }else{ //2,3,4
	    	 if (p2.x<r1.x){ //4
	    		 srcHotRegion=3;
	    		 destHotRegion=0;

	    	 }else{
	    		 if (p2.x>r1.x+r1.width){ //2
	    			 srcHotRegion=1;
		    		 destHotRegion=0;

	    		 }else{//3
	    			 srcHotRegion=2;
		    		 destHotRegion=0;
	
	    		 }
	    	 }
	    	 Point lp1=srcVertex.getConnectionPoint(srcHotRegion);
	    	 
    		 //g2.drawLine(lp1.x,lp1.y,p2.x,lp1.y);
    		 lines[lineIndex++]=new Line2D.Double(lp1.x,lp1.y,p2.x,lp1.y);
    		 //g2.drawLine(p2.x,lp1.y,p2.x,r2.y);
    		 lines[lineIndex++]=new Line2D.Double(p2.x,lp1.y,p2.x,r2.y);
    		 
    		 drawArrowHead(g2,p2.x,r2.y,ORIENTATION_DOWN);
	     }
	     
	     for (int i = 0; i < lineIndex; i++) {
			g2.draw(lines[i]);
		 }

	     lines[lineIndex]=null;
	     g2.setStroke(oldStroke);
	     
   }
   
   
  
   protected void drawLabel(Graphics g){
	   if (lineIndex==0) return;

	   // get text
	   Object labelObj=attributes.getSpecialValue("label")[0];
	   
	   String edgeLabelTxt=labelObj.toString().replaceAll("&lt;","<").replaceAll("&gt;", ">");
	   
	   if (edgeLabelTxt.trim().length()==0) return;
	   
	   Graphics2D g2=(Graphics2D)g;
	   
	   double longestLineLenght=0;
	   boolean longestLineHorizontal=false;
	   int longestLineIndex=0;
	   
	   
	   // find longest line
	   for (int i = 0; i < lineIndex; i++) {
		   Line2D.Double line=lines[i];
		
		   // find the line orientation
		   boolean horizontal=false;
		   double lineLength=0;
		   
		   if (line.x1==line.x2){
			   horizontal=false;
			   lineLength=Math.abs(line.y2-line.y1);
		   }else{
			   horizontal=true;
			   lineLength=Math.abs(line.x2-line.x1);
		   }
		   
		   if (longestLineLenght<lineLength){
			   longestLineLenght=lineLength;
			   longestLineIndex=i;
			   longestLineHorizontal=horizontal;
		   }	   
	   }
	 	   
	   // draw line
	   
	   int sx,sy;
	   Line2D.Double ll=lines[longestLineIndex];
	   
	   if (longestLineHorizontal){
		   Rectangle2D bound=g2.getFontMetrics().getStringBounds(edgeLabelTxt,g2);
		   sx=(int)((ll.x1+ll.x2)/2-(bound.getWidth()/2));
		   sy=(int)(ll.y1-EDGE_LABEL_MARGIN);
	   }else{
		   sx=(int)(ll.x1+EDGE_LABEL_MARGIN);
		   sy=(int)((ll.y1+ll.y2)/2);
	   }
	   
	   if (!g2.getFont().getName().equalsIgnoreCase("Helvetica")){
		   Font f=new Font("Helvetica",Font.PLAIN,10);
		   g2.setFont(f);
	   }
	   
	   g2.drawString(edgeLabelTxt,sx,sy);  
	   
	   
   }
  
   public Element getElementAsDOM(Document doc){
	   	 Element connectionNode=doc.createElement("connection");
		 
		 connectionNode.setAttribute("id",""+getId());
		 connectionNode.setAttribute("src",""+getScrVertex().getId());
		 connectionNode.setAttribute("dest",""+getDestVertex().getId());
		 connectionNode.setAttribute("type",""+getType());
		 
		 // Save connections' attributes
		 connectionNode.appendChild(attributes.toDOM(doc));
		 
		 return connectionNode;
   }
   
   public Element getElementAsDOM(){
	   try{
		   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		   Document doc = factory.newDocumentBuilder().newDocument();
		   return getElementAsDOM(doc);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   return (Element)null;
   }
   
   // ------------- Getters and Setters ----------------------------------------

  protected void drawArrowHead(Graphics2D g, int x,int y,int orient){
	  
	  int[][] xo={{1,-1},{-1,1},{1,1},{-1,-1}};
	  int[][] yo={{1,1},{-1,-1},{-1,1},{-1,1}};
	  
	  int[] xtabl=new int[3];
	  xtabl[0]=x;
	  xtabl[1]=x+ARROW_HEAD_SIZE*xo[orient][0];
	  xtabl[2]=x+ARROW_HEAD_SIZE*xo[orient][1];
	  
	  int[] ytabl=new int[3];
	  ytabl[0]=y;
	  ytabl[1]=y+ARROW_HEAD_SIZE*yo[orient][0];
	  ytabl[2]=y+ARROW_HEAD_SIZE*yo[orient][1];
	  
	  g.fillPolygon(xtabl,ytabl,3);
  }
  
  public boolean near(Point p){
	  for (int i = 0; i < lines.length; i++) {
		if (lines[i]==null) return false;
		Line2D.Double l = lines[i];
		if (l.ptSegDist(p)<5) return true;
	 }
	 return false; 
  }
  
  public int getDestHotpoint() {
    return destHotRegion;
  }
  public FlowChartNode getDestVertex() {
    return destVertex;
  }
  public FlowChartNode getScrVertex() {
    return srcVertex;
  }
  public boolean isSelected() {
    return selected;
  }
  public int getSrcHotPoint() {
    return srcHotRegion;
  }
  public void setDestHotpoint(int destHotpoint) {
    this.destHotRegion = destHotpoint;
  }
  public void setDestVertex(FlowChartNode destVertex) {
    this.destVertex = destVertex;
  }
  public void setScrVertex(FlowChartNode scrVertex) {
    this.srcVertex = scrVertex;
  }
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  public void setSrcHotPoint(int srcHotPoint) {
    this.srcHotRegion = srcHotPoint;
  }

  
  public String getTypeName(){
	  return BpathMainFrame.messages.getString("ELEMENT.EDGE");
  }
  
  public String getType(){return "SIMPLE_EDGE";}

  public String getId() {
	  return id;
  }
  
}
