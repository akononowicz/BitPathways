/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import edu.uj.zbit.bpath.editor.LineEnumeration;
import edu.uj.zbit.bpath.editor.dialogs.AddElementWizard;
import edu.uj.zbit.bpath.editor.model.*;

/**
 * <p>
 * Represents a vertex in the pathways graph
 * 
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public abstract class FlowChartNode extends Rectangle implements FlowChartElement {

  public abstract String getWizardName();
	
  /** Id der Komponente */
  protected String id;

  /** Informiert ob diese Komponente gewählt wurde */
  protected boolean selected = false;
  
  /**
   * Informiert ob sich der Knoten in einer korrekten Position auf dem
   * Bildschirm befindet. Ein Knoten befindet sich nicht in einer korrekten
   * Position wenn er drüber oder drunter einer anderen Komponente liegt.
   */
  protected boolean correctPos=true;
  
  /** Graphische Eigenschaften*/
  protected Font vertexFont = new Font("Helvetica", Font.PLAIN, 10);
  protected final int HR_SIZE=6;
  protected final int TEXT_MARGIN=2;
  
  public static final int HP_NORTH_ID=0;
  public static final int HP_EAST_ID=1;
  public static final int HP_SOUTH_ID=2;
  public static final int HP_WEST_ID=3;
  public static final int HP_RESIZE_ID=4;
  
  protected AttribGroupSet attributes;
  
  public AttribGroupSet getAttributes(){
	  return attributes;
  }
  
  public void setAttributes(AttribGroupSet attributes){
	  this.attributes=attributes;
  }
  

  
  //-----------------------------------------------------------------------
  // Constructors 
  //-----------------------------------------------------------------------
  // Constructor - creates a new shape (data flow element)
  public FlowChartNode(String _id){
	  id=_id;
  }

  //-----------------------------------------------------------------------
  // Abstract methods 
  //-----------------------------------------------------------------------
  
  /**
   * Gibt den Verankerungspunkt für ein Kanteende zurück.
   * @param hotRegion int Nummer des Anschlusspunkt
   * @return Point Verankerungspunkt
   */
  abstract public Point getConnectionPoint(int hotRegion);
  abstract public Point[] getHotPoints();
  
  abstract protected void drawShape(Graphics2D g);

  
  //-----------------------------------------------------------------------
  // Model methods 
  //-----------------------------------------------------------------------
  
  
  public Element getElementAsDOM(Document doc){
	  	Element elementNode=doc.createElement("element");
		 
		 elementNode.setAttribute("type",getType());
		 elementNode.setAttribute("id",""+getId());
		 
		 Element positionNode=doc.createElement("bounds");
		 
		 positionNode.setAttribute("x",""+x);
		 positionNode.setAttribute("y",""+y);
		 positionNode.setAttribute("width",""+width);
		 positionNode.setAttribute("height",""+height);

		 elementNode.appendChild(positionNode);
		 
		 elementNode.appendChild(attributes.toDOM(doc));
		 		 
		 return elementNode;
  }


  //----------------------------------------------------------------------------
  // -------- draw functions ----------------
  //----------------------------------------------------------------------------

  /* Dient der Verteilung der graphischen Elemente. Die graphische
   * Oberfläche wurde 24x12 Felder aufgeteilt. Die Funktionen pos_x und pos_y
   * transformieren die "virtuellen" Koordinaten in Pixels  */
  protected int pos_x(double rx){
    return (int)Math.ceil((rx/24.0)*width);
  }
  protected int pos_y(double ry){
    return (int)Math.ceil((ry/12.0)*height);
  }

//------------------------------------------------------------------------------

  public void paint(Graphics g){
	  Graphics2D g2=(Graphics2D)g;
	  drawShape(g2);	  
  }

//------------------------------------------------------------------------------
  protected void drawHotRegions(Graphics2D g2){

	  if ((this instanceof DataFlowComponent)||
		  (this instanceof DataFlowStart)
		  ){

		  Polygon p=new Polygon();
		  p.addPoint(x,y+pos_y(6));
		  p.addPoint(x+HR_SIZE,y+pos_y(6)-HR_SIZE);
		  p.addPoint(x+HR_SIZE,y+pos_y(6)+HR_SIZE);		  
		  g2.fill(p);
		  
		  p=new Polygon();
		  p.addPoint(x+pos_x(12),y+pos_y(12));
		  p.addPoint(x+pos_x(12)-HR_SIZE,y+pos_y(12)-HR_SIZE);
		  p.addPoint(x+pos_x(12)+HR_SIZE,y+pos_y(12)-HR_SIZE);
		  g2.fill(p);
		  
		  p=new Polygon();
		  p.addPoint(x+pos_x(24),y+pos_y(6));
		  p.addPoint(x+pos_x(24)-HR_SIZE,y+pos_y(6)-HR_SIZE);
		  p.addPoint(x+pos_x(24)-HR_SIZE,y+pos_y(6)+HR_SIZE);		  
		  g2.fill(p);
	  }

	  //Color oldColor=g2.getColor();
	  //g2.setColor(Color.BLACK);
	  
	  if (!(this instanceof DataFlowBranch)){
		  g2.drawRect(x+width-HR_SIZE, y+height-HR_SIZE, HR_SIZE, HR_SIZE);
		  g2.fillRect(x+width-HR_SIZE/4, y+height-HR_SIZE/4, HR_SIZE/2, HR_SIZE/2);
  	  }
	  //g2.setColor(oldColor);
  }

//------------------------------------------------------------------------------
	 protected void drawText(Graphics2D g, String str, int margin){
		 
		
		 LineEnumeration linewrapper=new LineEnumeration(g,vertexFont,str,width-margin);
		 int lineheight=g.getFontMetrics().getHeight();
		 int baseline=y+TEXT_MARGIN+lineheight;
		 
		 while( (baseline<(y+height-TEXT_MARGIN))&&
				 (linewrapper.hasMoreElements())
			   ){
			 String toPrint=linewrapper.nextElement().toString();
			 int xPos=centerText(g,toPrint);
			 g.drawString(toPrint,xPos,baseline);
			 baseline+=lineheight;
		 }
	 }
	 
	//------------------------------------------------------------------------------
	 protected void drawText(Graphics2D g, String str){
		 drawText(g, str, 2*TEXT_MARGIN);
	 }	 
	 
//		------------------------------------------------------------------------------
		  protected int centerText(Graphics2D g,String string){

		    int leftTextBoundary=x+pos_x(5);
		    int rightTextBoundary=x+pos_x(19);

		    g.setFont(vertexFont);
		    int str_width=g.getFontMetrics().stringWidth(string);
		    int middle = (rightTextBoundary - leftTextBoundary)/2;

		    return (leftTextBoundary +(middle-(str_width/2)));
		}
//		------------------------------------------------------------------------------
		  protected String fitText(Graphics2D g,String string){

		    int leftTextBoundary=x+pos_x(5);
		    int rightTextBoundary=x+pos_x(19);

		    String abrStr=string;
		    int cutOffset=3;

		    while(g.getFontMetrics().stringWidth(abrStr)>(rightTextBoundary-leftTextBoundary)){
		      abrStr=string.substring(0,string.length()-cutOffset)+"...";
		      cutOffset++;
		    }

		    return abrStr;
		  }
		  
  /** Markiert den Knoten als ausgewählt. Je nach Stelle des Mausklicks
   * werden entsprechende Regionen des Kontens aktiviert
   *
   * @param p Point Punkt auf den geklickt wurde
   * @return Nummer der ausgewählten Region des Knotens (sieh Abbildung in der Doku)
   */

  public int setSelected(Point p) {
    int selectedHotRegion=getHotRegionAt(p);

    selected=true;
    return selectedHotRegion;
  }
  //----------------------------------------------------------------------------
  // Setters and getters
  //----------------------------------------------------------------------------

  /**
   * Entscheidet welche Region des Knotens gewählt wurde. Die Bezeichnungen der
   * @param p Point
   * @return int
   */

  public int getHotRegionAt(Point p){
	  
	  Point[] hotpoints=getHotPoints();
	  
	  
	  
	  for(int i=0;i<hotpoints.length;i++){
		  
		  Point hp=getHotPoints()[i];
		  if (hp==null) continue;
		  
		  Rectangle rect=new Rectangle(
				  hp.x-2*HR_SIZE,
				  hp.y-2*HR_SIZE,
				  4*HR_SIZE, 
				  4*HR_SIZE);
		  
		  if (rect.contains(p)) return i;
	  }
	  
	  return -1;
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
  

  public boolean isSelected() {
   return selected;
  }
  
  public void setSelected(boolean selected) {
   this.selected = selected;
  }

  public boolean isCorrectPos() {
    return correctPos;
  }
  
  public void setCorrectPos(boolean correctPos) {
    this.correctPos = correctPos;
  }
  
  public String getId() {
		return id;
  }
  
  
  public AddElementWizard getWizard(JFrame parent){
	  if (getWizardName()!=null){
		  try{
			  Class<AddElementWizard> clazz=(Class<AddElementWizard>)Class.forName(getWizardName());
			  Constructor<AddElementWizard> constructor=clazz.getConstructor(JFrame.class);
			  AddElementWizard wizard=constructor.newInstance(parent);
			  return wizard;
		  }catch (Exception e) {e.printStackTrace();}
	  }
	  return null;
  }

	public String getLabel(){

		String label="";
		
		try {
			label=(attributes.getSpecialValue("label"))[0].toString();
		} catch (Exception e) {}
		
		return label; 
	}
  
  
}
