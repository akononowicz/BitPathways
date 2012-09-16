/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.xml.internal.bind.v2.TODO;

import edu.uj.zbit.bpath.editor.command.RemoveEdgeCommmand;
import edu.uj.zbit.bpath.editor.command.RemoveNodeCommmand;
import edu.uj.zbit.bpath.editor.model.*;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowComponent;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowPath;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowSubpath;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowTempEdge;
import edu.uj.zbit.bpath.editor.model.events.PathModelEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelListener;
import edu.uj.zbit.bpath.editor.repository.ExistConnection;

import java.util.*;


/**
 * <p>
 * Canvas displaying all the shapes from the model
 * </p>
 *
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public class DataFlowCanvas extends JComponent {

  //Desktop states
  public final static int STATE_NORMAL = 0;
  public final static int STATE_DRAG_VERTEX = 1;
  public final static int STATE_DRAG_NEW_EDGE = 2;
  public final static int STATE_RESIZE_VERTEX = 3;
  
  public final static int MIN_VERTEX_HEIGHT=30;
  public final static int MIN_VERTEX_WIDTH=50;
  
  public final static int COLUMN_1=10;
  public final static int COLUMN_2=120;
  public final static int COLUMN_3=230;
  public final static int COLUMN_4=340;
     
  
  // max. distance from an edge in that we can click to select the edge
  final double DELTA_P=5;

  //max. canvas size:
  final static public int MAX_WIDTH=5000;
  final static public int MAX_HEIGHT=5000;

  private int canvasWidth=0;
  private int canvasHeight=0;

  private JPanel parentPanel;
  private PathModel model; 
  private int state=STATE_NORMAL;
  
  /** temporary edge, currently being dragged */
  private DataFlowTempEdge tempEdge=null;

  private static final Logger log = Logger.getLogger(DataFlowCanvas.class);

  private DataFlowCanvas me;
  
  //----------------------------------------------------------------------------
  /**
   *  Constructor
   * @param parentPanel JPanel
   * @param context Context
   */
  public DataFlowCanvas(JPanel parentPanel, PathModel model) {
    this.parentPanel=parentPanel;
    this.model=model;
    me=this;

    MouseEventHandler meh=new MouseEventHandler();
    this.addMouseListener(meh);
    this.addMouseMotionListener(meh);
    this.addKeyListener(new KeyEventHandler());
    
    model.addPathModelListener(new PathModelListener(){

    	
      public void elementToBeAdded(PathModelEvent e){
    	  setCursor(new Cursor(Cursor.HAND_CURSOR)); 
      }
    		
    	
	  public void elementAdded(PathModelEvent e){
		  setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
		  
		  //TODO to ponizej jest chyba do kasacji (do adjustCanvas)
		  
		  /*	try{
		  		DataFlowNode element=(DataFlowNode)e.getSource();
		  		if ((element.x==0)&&(element.y==0)) placeAtFirstFreePosition(element);
		  	}catch(ClassCastException cce){}*/
		  	
		  	adjustCanvasSize();
		  	repaint();
	  }
	  public void elementSelected(PathModelEvent e){
		  	repaint();
	  }

	  public void elementRemoved(PathModelEvent e){
		  	setState(DataFlowCanvas.STATE_NORMAL);
		  	repaint();
	  }
	  public void elementChanged(PathModelEvent e){
		  
		  if (e.getSource() instanceof DataFlowPath){
			  changeTitle(e);
		  }
		  repaint();
	  }
	  public void modelLoaded(PathModelEvent e){

		  changeTitle(e);
		  adjustCanvasSize();
		  repaint();
	  }
	  
	  private void changeTitle(PathModelEvent e){
		  
		  
		  if (e.getSource() instanceof DataFlowPath){
			  JFrame jf=getParentFrame(getParent());
			  DataFlowPath dfp=(DataFlowPath) e.getSource(); 
			  

			  
			  String appTitle=BpathMainFrame.messages.getString("MAIN.TITLE");  

			  String name=getTitle();
			  
			  jf.setTitle(appTitle+" - "+name);
		  }
	  }
	  
	  private JFrame getParentFrame(Component c){
		  if (c.getClass()==BpathMainFrame.class) return (JFrame) c;
		  return getParentFrame(c.getParent());
	  }
	  
    });
    
    requestFocus();
   
  }
  
  private String getTitle(){
	  return model.getPathTitle();
  }
  
  //----------------------------------------------------------------------------

  public Dimension getPreferredSize(){
    return new Dimension(canvasWidth,canvasHeight);
  }
  //----------------------------------------------------------------------

  private void placeAtFirstFreePosition(FlowChartNode element){
	
	  if (element instanceof DataFlowComponent){
		  DataFlowComponent component=(DataFlowComponent)element;
		  if (component.getCategory().equals("")) 
			  nearestPostionPlacing(component);
		  else
			  componentPlacing(component);
	  }else{
		  nearestPostionPlacing(element);
	  }

  }
  
  private boolean isFree(FlowChartNode element,int x,int y,int margin){
	  element.x=x;
	  element.y=y;
	  return !checkIntersections(element);
  }
  
  private void nearestPostionPlacing(FlowChartNode element){
     
	  int margin=4;
      int n=MAX_WIDTH/(element.width+margin);
 	  int m=MAX_HEIGHT/(element.height+margin);
 	  
 	  int rx=0,ry=0;
 	  
 	  if (isFree(element, 1+rx*(element.width+margin), 1+ry*(element.height+margin), margin)) return;
 	  
 	  while(rx<n && ry<m){
 		 
 		  if((ry+1)<m){ 
 			  ry++;
 			  if (isFree(element, 1+rx*(element.width+margin), 1+ry*(element.height+margin), margin)) return;
 		  }
 		 
 		  while ((ry-1>=0)&&(rx+1<n)){
 			  ry--;rx++;
 			  if (isFree(element, 1+rx*(element.width+margin), 1+ry*(element.height+margin), margin)) return;
 		  }
 		  
 		 if ((rx+1)<n){
 			 rx++;
 			 if (isFree(element, 1+rx*(element.width+margin), 1+ry*(element.height+margin), margin)) return;
 		 }
 		 
 		  while ((rx-1>=0)&&(ry+1<m)){
 			  ry++;rx--;
 			  if (isFree(element, 1+rx*(element.width+margin), 1+ry*(element.height+margin), margin)) return;
 		  }
 		 
 	  }
  }
  
  private void componentPlacing(DataFlowComponent component){
	  String cat=component.getCategory();
	  int x,y;
	  if (cat.equalsIgnoreCase("Arztdienst")) x=COLUMN_1;
	  else if (cat.equalsIgnoreCase("Behandlungsdienste")) x=COLUMN_2;
	  else if (cat.equalsIgnoreCase("Pflegedienst")) x=COLUMN_3;
	  else x=COLUMN_4;
	  
	  y=50;
	  
	  while (!isFree(component, x, y, 4)){
		  //System.out.println("x:"+x+" y:"+y+" nicht getroffen");
		y+=component.height+10;
	  	if (y>(MAX_HEIGHT-component.height)){
	  		y=50;
	  		x+=component.width+10;
	  	}
	  }  
  }
  
  //----------------------------------------------------------------------
 
  public void setState(int newState){
	  int oldState=state;
	  
	  state=newState;
  }

  //----------------------------------------------------------------------------
  /**
   * Passt die Grï¿½ï¿½e der graphischer Flï¿½che den aktuellen Positionen
   * der Knoten an. Wird durch die viewerActivated()-Methode benutzt.
   * Wird aber nicht fï¿½r das Ereignis MOUSE_DRAGGED benutzt, da man dort
   * eine effizienter Methode dafï¿½r hat
   */

  private void adjustCanvasSize(){
    FlowChartNode[] vertices=model.getAllVertices();
    if (vertices==null) return;

    int maxX=0; int maxY=0;

    for (int i = 0; i < vertices.length; i++) {
      if ((vertices[i].x+vertices[i].width)>maxX) maxX=vertices[i].x+vertices[i].width;
      if ((vertices[i].y+vertices[i].height)>maxY) maxY=vertices[i].y+vertices[i].height;
    }

    if(maxX>canvasWidth) canvasWidth=maxX;

    if(maxY>canvasHeight) canvasHeight=maxY;

    revalidate();
  }

  public Rectangle getImageRectangle(){
	  int minX=MAX_WIDTH,maxX=0,minY=MAX_HEIGHT,maxY=0;
	  FlowChartNode[] vertices=model.getAllVertices();
	    if (vertices==null) return (Rectangle) null;

	    for (int i = 0; i < vertices.length; i++) {
	      if ((vertices[i].x)<minX) minX=vertices[i].x;
	      if ((vertices[i].y)<minY) minY=vertices[i].y;	      
	      if ((vertices[i].x+vertices[i].width)>maxX) maxX=vertices[i].x+vertices[i].width;
	      if ((vertices[i].y+vertices[i].height)>maxY) maxY=vertices[i].y+vertices[i].height;
	    }

	    return new Rectangle(minX,minY,maxX-minX,maxY-minY);
  }
  
  //----------------------------------------------------------------------------
  /**
   * Sucht im Modell nach einer Kante die DELTA_P vom Punkt p entfernt ist.
   * @param p - ein Punkt auf dem Desktop
   * @return Die gefundene Kante oder null (falls nicht gefunden).
   */

   private DataFlowEdge getEdgeNear(Point p){

      DataFlowEdge[] edgeArray=model.getAllEdges();
      
      for (int i=0;i<edgeArray.length;i++){
           if(edgeArray[i]!=null){
            if(edgeArray[i].near(p)) return edgeArray[i];
           }
      }
      return null;
  }

  //----------------------------------------------------------------------------

  /**
   * Sucht im Modell nach einem Knoten der den Punkt p beinhaltet.
   * @param p - ein Punkt auf dem Desktop
   * @return Knoten oder null (falls nicht gefunden).
   */

  private FlowChartNode getVertexAt(Point p){

     FlowChartNode[] vertexArray=model.getAllVertices();

     for (int i=0;i<vertexArray.length;i++) {
          if(vertexArray[i]!=null){
           if(vertexArray[i].contains(p)) return vertexArray[i];
          }
     }
     return null;
   }

  //----------------------------------------------------------------------------
  /**
   * Prï¿½ft ob sich der Knoten in der jetzigen Position mit einer anderen
   * Kante ï¿½berschneidet
   * @param vertex DataFlowVertex
   * @return boolean True- der Knoten ï¿½berscheidte sich mit einem anderen Knoten
   */

  private boolean checkIntersections(FlowChartNode vertex){
    FlowChartNode[] vertices= model.getAllVertices();
    for (int i = 0; i < vertices.length; i++) {
      if(vertices[i]==vertex) continue;
      if(vertices[i].intersects(vertex))return true;
    }
    return false;
  }

  //----------------------------------------------------------------------------

  public void paint(Graphics g){
	  paintModel((Graphics2D) g);
  }

  public RenderedImage saveImage(){
	  // Create a buffered image in which to draw
	  BufferedImage bufferedImage = new BufferedImage(getWidth()+10,getHeight()+10, BufferedImage.TYPE_INT_RGB);
	  Graphics2D g2 = bufferedImage.createGraphics();
	  paintModel(g2);
	  g2.dispose();
	  
	  Rectangle r=getImageRectangle();
	  int x=r.x; if (x<0) x=0;
	  int y=r.y; if (y<0) y=0;
	  
	  return bufferedImage.getSubimage(x, y, r.width+1, r.height+1);
  }
  
  public void paintModel(Graphics2D g){
		 if (model==null) return;
		  
		    //Aktualisierung der Grï¿½ï¿½e des Panels

		    if (canvasWidth<(int)parentPanel.getSize().getWidth()-5)
		      canvasWidth=(int)parentPanel.getSize().getWidth()-5;

		    if (canvasHeight<(int)parentPanel.getSize().getHeight()-5)
		      canvasHeight=(int)parentPanel.getSize().getHeight()-5;

		    // Zeichne den Hintergrund

		   if (model.isInState(PathModel.FLAG_NULL)){
			   g.setColor(Color.gray);
			   g.fillRect(0,0,canvasWidth,canvasHeight);
			   g.setColor(Color.black);
			   return;
			   
		   }
		    
		   g.setColor(Color.white);
		   g.fillRect(0,0,canvasWidth,canvasHeight+1);
		   g.setColor(Color.black);

		   
		     // Draw vertex
		     FlowChartNode[] vertexArray = model.getAllVertices();
		     
		     if (vertexArray!=null)
		     for (int i = 0; i < vertexArray.length; i++) {
		       if (vertexArray[i] != null) {
		         vertexArray[i].paint(g);
		       }
		     

		     // the selectedvertices are redrawn at the end to remain on top
		       
		     if (model.getFirstSelectedVertex() != null){
		    	 FlowChartNode[] selected=model.getSelectedVertices();
		    	 
		    	 for (int j = 0; j <selected.length ; j++) {
		    		 if (selected[j]==null) break;
		    		 else selected[j].paint(g);	
				}
		    	 
		     }

		     // Zeichne die Pfeile (Kanten)
		     DataFlowEdge[] edgeArray = model.getAllEdges();
		     if (edgeArray!=null)
		     for (int j = 0; j < edgeArray.length; j++) {
		       if (edgeArray[j] != null) {
		          edgeArray[j].paint(g);
		       }
		     }

		     // Zeichne den Pfeil der zurzeit gezeichnet wird
		     if (tempEdge!=null) tempEdge.paint(g);

		   }
  }

  //----------------------------------------------------------------------------

  /**
   * Handles all mouse events
   */

  private class MouseEventHandler extends MouseAdapter implements MouseMotionListener{

    /** mouse cursor position in the drag from the last event */
    private Point oldPointDrag=null;
    /** last correct position of the vertex */
    private Point lastCorrectVertexPoint=null;

    public void mousePressed(MouseEvent e){
    	
    	requestFocus();

      if (model.isInState(PathModel.FLAG_NULL)) return;
      
      if (model.isInState(PathModel.FLAG_PLACE_ELEMENT)){
    	  model.getTmpVertex().setLocation(e.getPoint());
    	  model.comitVertex();
      }else{
	      oldPointDrag=null;
	      selectPoint(e.getPoint());
	
	      if((e.getModifiers()&e.BUTTON1_MASK) != 0){
	        //if (e.getClickCount()>1) doubleClick(e.getPoint());
	      }
	
	      if((e.getModifiers()&e.BUTTON3_MASK) != 0){
	        JPopupMenu contextMenu=createContextMenu(e.getPoint());
	        contextMenu.show(e.getComponent(),e.getX(),e.getY());
	      }
      }
    }
    //--------------------------------------------------------------------------
    public void mouseReleased(MouseEvent e){
        if (model.isInState(PathModel.FLAG_NULL)) return;
      if((e.getModifiers()&e.BUTTON1_MASK) != 0)
        leftMouseReleased(e.getPoint());
    }
    //--------------------------------------------------------------------------
    public void mouseDragged(MouseEvent e) {

      // state machine
      // here is decided in which state should the appliaction change
      //
      // if the mouse is dragged and ...
      //
      // if a vertex is selected and the hot region east,south,west
      // is activated -> STATE_DRAG_NEW_EDGE
      //
      // if a vertex is selected and the hot region resize 
      // is activated -> STATE_RESIZE_VERTEX
      //
      // if a vertex is selected but no output hot region is activated
      // -> STATE_DRAG_VERTEX
      //
      // else STATE NORMAL
      
      if (model.isInState(PathModel.FLAG_NULL)) return;
        
      if ((state==STATE_NORMAL)&&(model.getFirstSelectedVertex()!=null)){
   
    	 int selectedVertexHotRegion=model.getFirstSelectedVertex().getHotRegionAt(e.getPoint());

    	 if((selectedVertexHotRegion==FlowChartNode.HP_EAST_ID)||
            (selectedVertexHotRegion==FlowChartNode.HP_SOUTH_ID)||
            (selectedVertexHotRegion==FlowChartNode.HP_WEST_ID)) {
            state = STATE_DRAG_NEW_EDGE;
            tempEdge=new DataFlowTempEdge(model.getFirstSelectedVertex(),
                                  selectedVertexHotRegion,
                                  e.getPoint());
         }else if (selectedVertexHotRegion==FlowChartNode.HP_RESIZE_ID){
        	state = STATE_RESIZE_VERTEX; 
         }else state=STATE_DRAG_VERTEX;
    	 
    	 model.addStateFlag(PathModel.FLAG_MODIFIED);

      }

      if (state==STATE_DRAG_VERTEX){
    	  
    	  if (oldPointDrag==null) oldPointDrag=e.getPoint();
    	  int vectorX=e.getX() - oldPointDrag.x;
    	  int vectorY=e.getY() - oldPointDrag.y;
    	  
    	      	  
    	  // drag all vertices
    	  FlowChartNode[] vertices=model.getSelectedVertices();
    	  
    	  for (int i = 0; i < vertices.length; i++) {
    		  if (vertices[i]==null) break;
    		  else dragVertex(e,vertices[i], vectorX, vectorY);
		  }
    	  
      }
      else if (state==STATE_DRAG_NEW_EDGE) dragNewEdge(e);
      else if  (state==STATE_RESIZE_VERTEX) resizeVertex(e);

      oldPointDrag=e.getPoint();

    }
    //--------------------------------------------------------------------------
    public void mouseMoved(MouseEvent e) {
    }
    //--------------------------------------------------------------------------
    /**
     * Moves selected vertex on the screen
     * @param e MouseEvent
     */
    private void dragVertex(MouseEvent e,FlowChartNode selectedVertex, int vectorX, int vectorY){

       
      // translate the vertex
      selectedVertex.translate(vectorX, vectorY);

      if (selectedVertex.x < 0) selectedVertex.x=0;
      if (selectedVertex.y < 0) selectedVertex.y=0;
      
      
      if (model.getSelectedVertices().length==1){
      
	     if(selectedVertex.contains(e.getX() , e.getY())){
	    	  selectedVertex.x=e.getX()-selectedVertex.width/2;
	    	  selectedVertex.y=e.getY()-selectedVertex.height/2;
	      }  

	      // check if vertex is after dragging in correct possition
	      // not correct possitions include intersection with other vetices
	      // or transgression of the max. canvas size
	/*      if (checkIntersections(selectedVertex) ||
	          (selectedVertex.x < 0) ||
	          (selectedVertex.x + selectedVertex.width > MAX_WIDTH) ||
	          (selectedVertex.y < 0) ||
	          (selectedVertex.y + selectedVertex.height > MAX_HEIGHT)
	          ) selectedVertex.setCorrectPos(false);
	      else {
	        selectedVertex.setCorrectPos(true);
	        lastCorrectVertexPoint = new Point(selectedVertex.x, selectedVertex.y);
	       } */
      
      }
   
  

      // resize the window if dragged over border (X-Dimension)
      if ( (selectedVertex.x + selectedVertex.width < MAX_WIDTH) &&
          (selectedVertex.x + selectedVertex.width > canvasWidth)) {
        canvasWidth = selectedVertex.x + selectedVertex.width;
        revalidate();
      }

      // the same as above but for Y-Dimension
      if ( (selectedVertex.y + selectedVertex.height < MAX_HEIGHT) &&
          (selectedVertex.y + selectedVertex.height > canvasHeight)) {
        canvasHeight = selectedVertex.y + selectedVertex.height;
        revalidate();
      }

      // check if the dargged objects fits into the viewport
      // if not - move the viewport

      try {
        JViewport view= (JViewport)getParent();
        Point transVect=translationToInculde(selectedVertex,view.getViewRect());

        if(!((transVect.x==0)&&(transVect.y==0))){
          Point newViewPoint=view.getViewPosition();
          newViewPoint.translate(transVect.x,transVect.y);
          view.setViewPosition(newViewPoint);
        }

        revalidate();
      }
      catch (Exception ex) { }

      if (lastCorrectVertexPoint==null) lastCorrectVertexPoint=new Point(selectedVertex.x,selectedVertex.y);
      repaint();

    }
    
    //--------------------------------------------------------------------------
    /**
     * Rechnet den Translations-Vektor aus, um den das Rechteck rect verschieben werden sollte
     * um den Knoten vollständig zu enthalten.
     * @param rect Rectangle
     * @return Point der Translations-Vektor
     */

      public Point translationToInculde(FlowChartNode shape, Rectangle rect){
	      Point transVect=new Point();
	
	      // x-Axis
	      if ((shape.x+shape.width)>(rect.x+rect.width))
	        transVect.x = (shape.x + shape.width) - (rect.x + rect.width);
	      else if((shape.x<rect.x)&&(shape.x>0))
	        transVect.x=shape.x-rect.x;
	
	      // y-Axis
	      if ((shape.y+shape.height)>(rect.y+rect.height))
	        transVect.y=(shape.y+shape.height)-(rect.y+rect.height);
	      else if((shape.y<rect.y)&&(shape.y>0))
	        transVect.y=shape.y-rect.y;
	
	      return transVect;
     }

    //--------------------------------------------------------------------------
      
    private void resizeVertex(MouseEvent e){

    	  if (oldPointDrag==null) return;

          FlowChartNode selectedVertex=model.getFirstSelectedVertex();
          // translate the vertex
          
          int dw=e.getX() - oldPointDrag.x;
          int dh=e.getY() - oldPointDrag.y;
          
          if ((selectedVertex.height+dh)>MIN_VERTEX_HEIGHT)
        	  selectedVertex.height+=dh;
          
          if ((selectedVertex.width+dw)>MIN_VERTEX_WIDTH)
        	  selectedVertex.width+=dw;
          
          // resize the window if dragged over border (X-Dimension)
          if ( (selectedVertex.x + selectedVertex.width < MAX_WIDTH) &&
              (selectedVertex.x + selectedVertex.width > canvasWidth)) {
            canvasWidth = selectedVertex.x + selectedVertex.width;
            revalidate();
          }
          
          // the same as above but for Y-Dimension
          if ( (selectedVertex.y + selectedVertex.height < MAX_HEIGHT) &&
              (selectedVertex.y + selectedVertex.height > canvasHeight)) {
            canvasHeight = selectedVertex.y + selectedVertex.height;
            revalidate();
          }

          // check if the dargged objects fits into the viewport
          // if not - move the viewport

          try {
            JViewport view= (JViewport)getParent();
            Point transVect=translationToInculde(selectedVertex,view.getViewRect());

            if(!((transVect.x==0)&&(transVect.y==0))){
              Point newViewPoint=view.getViewPosition();
              newViewPoint.translate(transVect.x,transVect.y);
              view.setViewPosition(newViewPoint);
            }

           revalidate();
          }catch(Exception e1){}
          
          repaint();
    }

    //--------------------------------------------------------------------------
    /**
     * Draws a new arrow while dragging.
     * @param e MouseEvent
     */
    public void dragNewEdge(MouseEvent e){
      if(tempEdge==null) return;

      tempEdge.setArrowEnd(e.getPoint());

      // resize the window if the arrow is dragged over border (X-Dimension)
      if ( (tempEdge.getArrowEnd().x < MAX_WIDTH) &&
          (tempEdge.getArrowEnd().x > canvasWidth)) {
        canvasWidth = tempEdge.getArrowEnd().x;
        revalidate();
      }

      // the same as above but for Y-Dimension
      if ( (tempEdge.getArrowEnd().y < MAX_HEIGHT) &&
          (tempEdge.getArrowEnd().y > canvasHeight)) {
        canvasHeight = tempEdge.getArrowEnd().y;
        revalidate();
      }

      // check if the dargged objects fits into the viewport
      // if not - move the viewport

      try {
        JViewport view = (JViewport) getParent();

        Point transVect = new Point(0,0);

        Rectangle viewRect=view.getViewRect();

        // move the viewport in the horizontal direction
        if (tempEdge.getArrowEnd().x>viewRect.getX()+viewRect.getWidth())
          transVect.x=tempEdge.getArrowEnd().x-(int)viewRect.getX()-(int)viewRect.getWidth();
        else if ( (tempEdge.getArrowEnd().x < viewRect.getX()) &&
                 (tempEdge.getArrowEnd().x > 0))
          transVect.x = tempEdge.getArrowEnd().x - (int)viewRect.getX();

        // move the viewport in the vertical direction
      if (tempEdge.getArrowEnd().y>viewRect.getY()+viewRect.getHeight())
          transVect.y=tempEdge.getArrowEnd().y-(int)viewRect.getY()-(int)viewRect.getHeight();
      else if ((tempEdge.getArrowEnd().y<viewRect.getY())&&
               (tempEdge.getArrowEnd().y>0))
          transVect.y=tempEdge.getArrowEnd().y-(int)viewRect.getY();

        if (! ( (transVect.x == 0) && (transVect.y == 0))) {
          Point newViewPoint = view.getViewPosition();
          newViewPoint.translate(transVect.x, transVect.y);
          view.setViewPosition(newViewPoint);
        }

        revalidate();
      }
      catch (Exception ex) {}

      repaint();
    }

    //--------------------------------------------------------------------------
    private void selectPoint(Point p){
	
      
      if (model.getSelectedEdge()!=null) {
    	  model.getSelectedEdge().setSelected(false);
      }

      if(!model.isInState(PathModel.FLAG_SHIFT_PRESSED)) model.deactivateSelection();
      
      // Check what element has been selected

      FlowChartNode vertex=getVertexAt(p);
      if (vertex!=null){
    	  
      
        if(model.getSelectedVerticesCount()>0){
        	model.addVertexToSelection(vertex);	
        }else model.setSelectedVertex(vertex);
     	
        vertex.setSelected(p);
     	
      }else{
        DataFlowEdge edge=getEdgeNear(p);
        if (edge!=null){
          edge.setSelected(true);
          model.setSelectedEdge(edge);
        }else{
      	  model.setSelectedPath();
        }
      }
      repaint();
    }

    //--------------------------------------------------------------------------
/*    private void doubleClick(Point p){
    	DataFlowNode node=model.getFirstSelectedVertex();
    	String pathid="";
    	
    	if (node==null) return;
    	
    	
    	
        if (node instanceof DataFlowSubpath){
      	  model.getFirstSelectedVertex().setSelected(true);
      	  Object value=node.getAttributes().getValue(Dict.SUBPATHWAY_LINK);
      	        	  
  	  	  if (value instanceof String){
  	  		    JOptionPane.showMessageDialog(parentPanel, BpathMainFrame.messages.getString("ERROR.SUBPATH"));	
      		return;
  	  	  }else{
  	  		  try{
  	  			if (value instanceof Element){	
	  	  			Element subpathElement=(Element)value;
	  	  			Element idElement=(Element)subpathElement.getElementsByTagName("id").item(0);
	  	  			
	  	  			pathid=idElement.getChildNodes().item(0).getNodeValue();
	  	  			
  	  			}
  	  		
  	  		  if (pathid!=null){
  	  			int response=JOptionPane.showConfirmDialog(parentPanel, BpathMainFrame.messages.getString("ACTION.CONFIRMSAVE"));
  	  			  
  	  			  if (response==JOptionPane.OK_OPTION) ExistConnection.save(model.getPathId(), model, me, ""+model.getUid());
  	  			
  	  			  if (response!=JOptionPane.CANCEL_OPTION){
	  	  			  Document doc=(Document)ExistConnection.getPath(pathid);
					  NodeList nodelist=doc.getElementsByTagName("bitpathway");
					  model.setModelAsDOM((Element)nodelist.item(0));
  	  			  }
  	  			
			  }
				 
			  
  	  			  
  	  		  }catch(Exception e){
  	  			  e.printStackTrace();
  	  		  }
  	  		  
  	  	  }
  	  	  
        }
    	
    }*/
    //--------------------------------------------------------------------------
    private void leftMouseReleased(Point p) {

      if(state==STATE_DRAG_VERTEX){
        if (!model.getFirstSelectedVertex().isCorrectPos()) {
        	model.getFirstSelectedVertex().setLocation(lastCorrectVertexPoint);
        	model.getFirstSelectedVertex().setCorrectPos(true);
          repaint();
        }
        lastCorrectVertexPoint = null;
      }else if(state==STATE_DRAG_NEW_EDGE){
        FlowChartNode targetVertex=getVertexAt(p);

        // if a vertex has been clicked
        if (	(targetVertex!=null) && 
        		(targetVertex!=tempEdge.getScrVertex()) &&
        		(!(targetVertex instanceof DataFlowStart))
        	){

            // create new edge in the model (the model changes itself and
            // delegates the code modification to the code manager
        	model.addEdge(tempEdge.getScrVertex(),targetVertex);
        }
        repaint();
        tempEdge=null;
      }

      oldPointDrag = null;
      state = STATE_NORMAL;
    }
    
    //--------------------------------------------------------------------------
    /**
     * Creates context menu for the given point
     * @param p Point
     * @return JPopupMenu
     */
    private JPopupMenu createContextMenu(Point p){

      JPopupMenu popup = new JPopupMenu();

      // --- Nodes

      FlowChartElement sVertex=model.getFirstSelectedVertex();
      
      if((sVertex!=null)&&
    	  (!(sVertex instanceof DataFlowPath)&&
    	  (!(sVertex instanceof DataFlowStart)))
    	){
        popup.add(new RemoveNodeCommmand((FlowChartNode)sVertex,model));
      }
      
      // --- Edges
      
      DataFlowEdge sEdge=model.getSelectedEdge();
      
      if(sEdge!=null){
        popup.add(new RemoveEdgeCommmand(sEdge,model));
      }
      
      return popup;
    }
  }

  
	public boolean isFocusTraversable()
	{
	 return true;
	}
  
  //---- end of MouseEventHandler-Class ----------------------------------------


  /**
   * Handles all key events
   */
  private class KeyEventHandler extends KeyAdapter{
  
	  public void keyPressed(KeyEvent e){
		  
      if (e.getKeyCode()==KeyEvent.VK_DELETE) deleteEvent(e);
      if (e.getKeyCode()==KeyEvent.VK_SHIFT){
    	  if (model.getState()!=PathModel.FLAG_NULL) model.addStateFlag(PathModel.FLAG_SHIFT_PRESSED);
      }
    }

    public void keyTyped(KeyEvent e){
    }

    public void keyReleased(KeyEvent e){
    	if (e.getKeyCode()==KeyEvent.VK_SHIFT){
      	  if (model.getState()!=PathModel.FLAG_NULL) model.removeStateFlag(PathModel.FLAG_SHIFT_PRESSED);
        }
    }


    private void deleteEvent(KeyEvent e){
      if (model.getSelectedEdge()!=null){
       // remove edge

      }else if (model.getFirstSelectedVertex()!=null){
       // remove vertex
      }
    }
  }

  //---- end of KeyEventHandler-Class ----------------------------------------
  

}
