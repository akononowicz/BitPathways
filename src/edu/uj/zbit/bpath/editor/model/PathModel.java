/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.DataFlowCanvas;
import edu.uj.zbit.bpath.editor.Encoder;
import edu.uj.zbit.bpath.editor.PathwayFormatException;
import edu.uj.zbit.bpath.editor.command.LoadFromFileAction;
import edu.uj.zbit.bpath.editor.command.LoadFromServerAction;
import edu.uj.zbit.bpath.editor.command.NewPathCommmand;
import edu.uj.zbit.bpath.editor.dialogs.AddElementWizard;
import edu.uj.zbit.bpath.editor.model.converter.Bp1xTo2XConverter;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowEdge;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartElement;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowPath;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowStart;
import edu.uj.zbit.bpath.editor.model.elements.DataFlowSubpath;
import edu.uj.zbit.bpath.editor.model.events.PathModelChangeEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelEvent;
import edu.uj.zbit.bpath.editor.model.events.PathModelListener;


/**
 * <p>
 * 
 * Model of the pathway. 
 * The pathway elements are stored in two HashMaps vertices (for pathway nodes) and edges (for connections).
 * Additionally a connectionMatrix is used to check whether there is a connection between the given two
 * nodes. Vertices and Edges are identified by ordinal numbers. The first free number in both categories
 * is held in special variables. Additionally there is a special element for attributes of the whole
 * pathway 
 * 
 * The amount of nodes is limited to 1000.
 * 
 * </p>
 *
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public class PathModel {

  // state machine

  /** available state flags */
  public static final int FLAG_NULL=0;
  public static final int FLAG_EDITABLE=1;
  public static final int FLAG_MODIFIED=2;
  public static final int FLAG_PLACE_ELEMENT=4;
  public static final int FLAG_SHIFT_PRESSED=8;  
  
  /** Current state of the model */
  private int state=FLAG_NULL;

/** element storage */
  private final static int MAX_VERTICES=1000;
  private final static int MAX_SELECTED_VERTICES=MAX_VERTICES;
  
  private HashMap vertices;
  private HashMap edges;
  private DataFlowPath path;
  private boolean[][] connectionMatrix;
  
  private Template[] templates; 
  private Metadata metadata;
  
  private Document doc; 
  
  //index of the first free position in the vertices/edges matrix
  private int firstFreeVertexId=0;
  private int firstFreeEdgeId=0;

  private LinkedList listeners=new LinkedList();
  
  /** selected vertex */
  private FlowChartNode[] selectedVertices = new FlowChartNode[100];
  
  /** vertex to be added */
  private FlowChartNode tmpVertex = null;

  /** selected edge */
  private DataFlowEdge selectedEdge = null;
  
  /** the file into which the model was last saved */
  private File lastSavedFile=null;
  
  private Action[] actions;
 
  private BpathMainFrame main=null;
  
  private static final Logger log = Logger.getLogger(PathModel.class);
  

  
  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  
  public PathModel(BpathMainFrame _main) {main=_main;}
   
  //----------------------------------------------------------------------------------------------
  
  public FlowChartNode getTmpVertex() {return tmpVertex;}
  //----------------------------------------------------------------------------------------------

  public Metadata getMetadata() {return metadata;}
  //----------------------------------------------------------------------------------------------
  
  public void newModel(String _pathID, Metadata _metadata, Template[] _templates){
	  
	  resetModel();
	  
	  path=new DataFlowPath(_pathID);	  
	  metadata=_metadata;
	  templates=_templates;
	  
	  try{
		  DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		  doc=factory.newDocumentBuilder().newDocument();
	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  
	  path.setAttributes(getDefaultTemplate().constructAttribGroupSet(this,"PATH","0")); 
	  
	  // add start element
	  
	  String start_id=Integer.toString(firstFreeVertexId);
	  
	  firstFreeVertexId++;
	  DataFlowStart startelement=new DataFlowStart(start_id);

	  vertices.put(start_id,startelement);

	  AttribGroupSet attributes=getDefaultTemplate().constructAttribGroupSet(this, "START",start_id);
	  
	  startelement.setAttributes(attributes);
	  startelement.x=208;
	  startelement.y=1;
	  
	  
	  fireElementAdded(startelement);
	  
	  setState(FLAG_EDITABLE);
	  fireModelLoaded();
  }
  
  private void resetModel(){
	 connectionMatrix=new boolean[MAX_VERTICES][MAX_VERTICES];
	 vertices=new HashMap();
	 edges=new HashMap();
	 firstFreeVertexId=1;
	 firstFreeEdgeId=0;
	 deactivateSelection();
	 selectedEdge=null;
	 lastSavedFile=null;
	 path=null;
	 templates=null;
	 metadata=new Metadata();
  }
  

  public void setActions(Action[] a){
	  actions=a;
      setState(FLAG_NULL);
  }
  
  
  public void addVertex(Class<FlowChartNode> vClass)throws Exception{  
	  	
		String id=Integer.toString(firstFreeVertexId); 
	    firstFreeVertexId++;
	  
		Constructor<FlowChartNode> constructor=vClass.getConstructor(String.class);
		tmpVertex=constructor.newInstance(id);
		
		addStateFlag(FLAG_PLACE_ELEMENT);
		
		fireElementToBeAdded(tmpVertex);
  }
  
  public void comitVertex(){
	  	
	  removeStateFlag(FLAG_PLACE_ELEMENT);	  
	  
	  AddElementWizard wizard=tmpVertex.getWizard(main);
		
	  Object[][] wizardAttribs=null;
	  
	  if (wizard!=null){
		  wizard.setVisible(true);
		  
		if(wizard.isAccepted()){
			wizardAttribs=wizard.getAttributeSet().getAttributes();	  
		}else{
			//element rejected
			tmpVertex=null;
			//fireElementAddedRejected() ???? is needed ???
			return;
		}
	  }
	  
	  AttribGroupSet attributes=getDefaultTemplate().constructAttribGroupSet(this, tmpVertex.getType(), tmpVertex.getId());
	  tmpVertex.setAttributes(attributes);

	  vertices.put(tmpVertex.getId(),tmpVertex);
	  
	  fireElementAdded(tmpVertex);
	  
	  if(wizardAttribs!=null){
		 for (int i = 0; i < wizardAttribs.length; i++) {
			 attributes.setSpecialValue(wizardAttribs[i][0].toString(), (Object[])wizardAttribs[i][1]);
		}
	  }


  }
 
  
  /**
   * Returns all verticies
   */
  public FlowChartNode[] getAllVertices(){
    
	  FlowChartNode[] retTab=new FlowChartNode[vertices.size()];

	  Iterator iter=vertices.values().iterator();
	  int i=0;
	  
	  while (iter.hasNext()) {
		FlowChartNode element = (FlowChartNode) iter.next();
		retTab[i]=element;
		i++;
	  }
	  
     return retTab;
  }

  /**
   * Gibt den Knoten mit dem Index index in der Modell-Array zur�ck
   *
   * @param index int
   * @return DataFlowVertex
   */
  public FlowChartNode getVertex(String id){
    if (Integer.parseInt(id)>=MAX_VERTICES) return null;
    else return (FlowChartNode)vertices.get(id);
  }

  /**
   * Returns all edges in the model
   * @return DataFlowEdge[]
   */

  public DataFlowEdge[] getAllEdges(){
	  
	  DataFlowEdge[] retTab=new DataFlowEdge[edges.size()];

	  Iterator iter=edges.values().iterator();
	  int i=0;
	  
	  while (iter.hasNext()) {
		DataFlowEdge element = (DataFlowEdge) iter.next();
		retTab[i]=element;
		i++;
	  }
	  
     return retTab;
  }

  /**
   * Insert an edge into the model. if there is already a connection between the nodes,
   * the process is skipped.  

   *
   * @param source DataFlowVertex
   * @param srcHotRegion int
   * @param target DataFlowVertex
   * @param targetHotRegion int
   * @return boolean informs if the edge has been successful added into the model
   */
  public boolean addEdge(FlowChartNode source,  FlowChartNode target ){
    int indexSource=Integer.parseInt(source.getId());
    int indexTarget=Integer.parseInt(target.getId());
    // exit if there is already a connection between these edges
    if (connectionMatrix[indexSource][indexTarget]) return false;

    if ((indexSource>=0)&&(indexTarget>=0)){
      connectionMatrix[indexSource][indexTarget] = true;
      String id=Integer.toString(firstFreeEdgeId);
      firstFreeEdgeId++;
      DataFlowEdge edge=new DataFlowEdge(id,source,target);
      edges.put(id,edge);
      
      edge.setAttributes(getDefaultTemplate().constructAttribGroupSet(this, "SIMPLE_EDGE",id));
      return true;
    }
    else return false;
  }

  /**
   * Entfernt eine Kante aus dem Modell
   * @param edge DataFlowEdge
   * @return boolean Info ob die Kante erfolgreich entfernt worden ist
   */

  public boolean removeEdge(String id) {
    // remove the edge from edge vector

    DataFlowEdge edge=(DataFlowEdge)edges.get(id);
    
    if (edge==null) return false;
    
    edges.remove(id);
    
    // remove the edge from connection matrix

    int scrIndex=Integer.parseInt(edge.getScrVertex().getId());
    int dstIndex=Integer.parseInt(edge.getDestVertex().getId());

    if ((scrIndex==-1)||(dstIndex==-1)){

      return false;
    }

    connectionMatrix[scrIndex][dstIndex]=false;


    return false;
  }


  /**
   * Entfernt einen Knoten aus dem Modell
   * @param vertex DataFlowVertex
   * @return boolean Info ob der Knoten erfolgreich entfernt worden ist
   */


  public boolean removeVertex(String id){

    // remove all adjacent edges

    DataFlowEdge[] edges=getAllEdges();

    try{
    for (int i = 0; i < firstFreeEdgeId; i++) {
      // remove all outgoing or incoming edges
      if((edges[i].getScrVertex().getId().equals(id))||
         (edges[i].getDestVertex().getId().equals(id))){
        removeEdge(edges[i].getId());
      }
    }
    }catch(Exception e){
    	System.out.println("Edges problem:"+id);
    }

   FlowChartElement vertex=(FlowChartElement)vertices.get(id);
   vertices.remove(id);
   deactivateSelection();
   selectedEdge=null;
   
   
   fireElementRemoved(vertex);
   
   fireElementSelected(path);
   
   
    return true;
  }


/**
 * Liefert eine Liste der direkten Nachfolger des Knotns
 * @param vertex DataFlowVertex
 * @return DataFlowVertex[] Array mit den Nachfolger oder null falls
 * keine Nachflger oder die Knote im Modell nicht existiert
 */
  public FlowChartNode[] getDirectSuccessors(FlowChartNode vertex){

    int vIndex=Integer.parseInt(vertex.getId());
    if (vIndex==-1) return null;

    ArrayList list=new ArrayList();

    for (int i = 0; i < firstFreeVertexId; i++) {
      if (connectionMatrix[vIndex][i]) list.add(this.getVertex(Integer.toString(i)));
    }

    if (list.size()==0) return null;

    FlowChartNode[] successors=new FlowChartNode[list.size()];

    for (int i = 0; i < successors.length; i++) {
      successors[i]=(FlowChartNode)list.get(i);
    }

    return successors;
  }

  /**
   * Liefert eine Liste der direkten Vorg�nger des Knotns
   * @param vertex DataFlowVertex
   * @return DataFlowVertex[] Array mit den Vorg�nger oder null falls
   * keine Nachflger oder die Knote im Modell nicht existiert
   */

  public FlowChartNode[] getDirectPredecessor(FlowChartNode vertex){

    int vIndex=Integer.parseInt(vertex.getId());
    if (vIndex==-1) return null;

    ArrayList list=new ArrayList();

    for (int i = 0; i < firstFreeVertexId; i++) {
      if (connectionMatrix[i][vIndex]) list.add(this.getVertex(Integer.toString(i)));
    }

    if (list.size()==0) return null;

    FlowChartNode[] successors=new FlowChartNode[list.size()];

    for (int i = 0; i < successors.length; i++) {
      successors[i]=(FlowChartNode)list.get(i);
    }

    return successors;

  }
 
  public void addPathModelListener(PathModelListener listener){
	  listeners.add(listener);
  }

  public void removePathModelListener(PathModelListener listener){
	  listeners.remove(listener);
  }
  
  
  private void fireElementToBeAdded(Object source){
	  PathModelEvent event=new PathModelEvent(source);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.elementToBeAdded(event);
	  }
  }
  
  private void fireElementAdded(Object source){
	  	  	  
	  PathModelEvent event=new PathModelEvent(source);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.elementAdded(event);
	  }
  }

  public void fireElementRemoved(Object source){
	  PathModelEvent event=new PathModelEvent(source);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.elementRemoved(event);
	  }
  }
  
  private void fireElementSelected(Object source){
	  
	  if (source==null) source=path;
	  PathModelEvent event=new PathModelEvent(source);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.elementSelected(event);
	  }
  }

  
  public void fireModelLoaded(){
	  
	  PathModelEvent event=new PathModelEvent(path);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.modelLoaded(event);
	  }
  }
  
 void fireElementChanged(String objectId, String groupId, String attribId, Object oldValues, Object newValues){
	 
	 // TODO: Add ChangeAction to undo stack
	 FlowChartElement source=null;
	
	 
	  PathModelChangeEvent event=new PathModelChangeEvent(objectId,oldValues,newValues);
	  
	  Iterator it=listeners.iterator();
	  
	  while (it.hasNext()) {
		PathModelListener element = (PathModelListener) it.next();
		element.elementChanged(event);
	  }
 }
 
 public void saveModelToFile(File file){
	 
	 try{
		 Source source=new DOMSource(getModelAsDOM());
		 StringWriter strWriter=new StringWriter();
		 
		 Result result=new StreamResult(strWriter);
		 
		 
		 Transformer xformer = TransformerFactory.newInstance().newTransformer();
		 xformer.transform(source,result);
		 
		 String toEncode=strWriter.toString();
		 
		 //String encoded=Encoder.encrypt(toEncode);
		 
		 Writer bout=new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		 bout.write(toEncode);
		 bout.close();
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }
 }
 
 public void loadModelFromFile (File file) throws Exception{

	 try{
		 		 
		 DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
		 Document doc=builder.parse(file);
		 
		 NodeList nodelist=doc.getElementsByTagName("bitpathways");
		 
		 if (nodelist.item(0)== null){
			 
			 nodelist=doc.getElementsByTagName("bitpathway");
			 if (nodelist.item(0)== null){
				 throw new Exception(BpathMainFrame.messages.getString("ERROR.WRONGFORMAT"));
			 }else{
				 
				 		int answer=JOptionPane.showConfirmDialog(main, 
						 BpathMainFrame.messages.getString("ERROR.OLDFROMATQUESTION"), 
						 BpathMainFrame.messages.getString("ERROR.OLDFORMAT"),
						 JOptionPane.YES_NO_OPTION
						 );
				 if (answer==JOptionPane.YES_OPTION){
					 Bp1xTo2XConverter.convert(file,file);
					 doc=builder.parse(file);
					 nodelist=doc.getElementsByTagName("bitpathways");
					 setModelAsDOM((Element) nodelist.item(0));
				 }else{
					 throw new Exception(BpathMainFrame.messages.getString("ERROR.WRONGFORMAT"));
				 }
			 }
		 }else{
			 setModelAsDOM((Element) nodelist.item(0));
		 }
		 
	 }catch(Exception e){
		e.printStackTrace();
		throw e;
	 }
 }
 
 public void setModelAsDOM(Element bpathNode) throws PathwayFormatException{
	  // reset model
	    resetModel();
	    try{    	
	    	// 1. check if format complaint
	    	Node pathNode = XPathAPI.selectSingleNode(bpathNode.getOwnerDocument(),"/bitpathways/path");
	    	if (pathNode==null) throw new PathwayFormatException(BpathMainFrame.messages.getString("ERROR.PATHERROR"));
	    	
	    	// 2. load template
	    	NodeList templateNodes = XPathAPI.selectNodeList(pathNode,"templates/template_ref");
	    	
	    	templates=new Template[templateNodes.getLength()];
	    	
	    	for (int i = 0; i < templateNodes.getLength(); i++) {
	    		
	    		Element template=(Element)templateNodes.item(i);
	    		
	    		templates[i]=new Template("http://"+template.getAttribute("repository")+"/"+template.getAttribute("id")+".xml");
	    		
			}
	    	
	    	// 3. load metadata    	
	    	Element metadataElement=(Element)((Element)pathNode).getElementsByTagName("metadata").item(0);	    	
	    	XMLProcessor.readXMLmetadata(metadata,metadataElement);

	    	// 4. load header attributes  		    	
		    path=new DataFlowPath(((Element)pathNode).getAttribute("id"));
		    AttribGroupSet pattributes=getDefaultTemplate().constructAttribGroupSet(this, "PATH", path.getId());
		    path.setAttributes(pattributes);
		    loadProperties(path,(Element)XPathAPI.selectSingleNode(pathNode, "header/attribute_groups"));    
		    
	    	// 5. load elements			    
	    	NodeList elementNodeList = XPathAPI.selectNodeList(pathNode,"elements/element");
	    	
	    	for (int i = 0; i < elementNodeList.getLength(); i++) {
	    		
	    		Element elementNode = (Element) elementNodeList.item(i);
	    		
	    		String type=elementNode.getAttribute("type");
		    	String id=elementNode.getAttribute("id");
		    	String className="";
		    	
		    	//TODO: ----- to be changed by context file ------------------------
		    	
		    	if (type.equalsIgnoreCase("TASK")) className="DataFlowComponent";
		    	else if (type.equalsIgnoreCase("START")) className="DataFlowStart";
		    	else if (type.equalsIgnoreCase("END")) className="DataFlowEnd";
		    	else if (type.equalsIgnoreCase("DECISION")) className="DataFlowBranch";
		    	else if (type.equalsIgnoreCase("SUBPATH")) className="DataFlowSubpath";
		    	else if (type.equalsIgnoreCase("COMMENT")) className="DataFlowComment";		    	
		    	
		    	//------------------------------------------------------------------
		    	
		    	FlowChartNode vertex=null;
		    			    	
		    	try{
		    		Class dfClass=Class.forName("edu.uj.zbit.bpath.editor.model.elements."+className);
		    		Constructor constr= dfClass.getConstructor(new Class[]{String.class});
		    		vertex=(FlowChartNode) constr.newInstance(new Object[]{id}); 
		    		
		    		Element boundsNode=(Element)elementNode.getElementsByTagName("bounds").item(0);
			    	String boundsX=boundsNode.getAttribute("x");
			    	String boundsY=boundsNode.getAttribute("y");
			    	String boundsWidth=boundsNode.getAttribute("width");
			    	String boundsHeight=boundsNode.getAttribute("height");

			    	vertex.setBounds(
			    			Integer.parseInt(boundsX),
			    			Integer.parseInt(boundsY),
			    			Integer.parseInt(boundsWidth),
			    			Integer.parseInt(boundsHeight)
			    	);
		    		
			    	AttribGroupSet attributes=getDefaultTemplate().constructAttribGroupSet(this, vertex.getType(), vertex.getId());
			   	    vertex.setAttributes(attributes);
			   	    loadProperties(vertex,(Element)elementNode.getElementsByTagName("attribute_groups").item(0));
		    		
		    	}catch(Exception e){
		    		System.out.println("ERROR for type:"+className);
		    		e.printStackTrace();
		    		throw new PathwayFormatException("Error reading class:"+className);
		    	}
		    	
		    	vertices.put(id,vertex);
		    	int id_int=Integer.parseInt(id);
		    	if (id_int>=firstFreeVertexId) firstFreeVertexId=id_int+1;
				
			}
	    		    	
	    	// 6. load connections	    	
	    	NodeList edgeNodeList = XPathAPI.selectNodeList(pathNode,"connections/connection");
	    	
	    	
	    	for(int i=0;i<edgeNodeList.getLength();i++){
		    	Element connectionNode=(Element)edgeNodeList.item(i);
		    	
		    	FlowChartNode srcVertex;
		    	try{
		    		srcVertex=(FlowChartNode)vertices.get(connectionNode.getAttribute("src"));
		    	}catch(Exception e){
		    		throw new PathwayFormatException("Missing source vertex");
		    	}
		    	
		    	FlowChartNode destVertex;
		    	try{
		    		destVertex=(FlowChartNode)vertices.get(connectionNode.getAttribute("dest"));
		    	}catch(Exception e){
		    		throw new PathwayFormatException("Missing destination vertex");
		    	}

		    	String id=connectionNode.getAttribute("id");
		    	
		    	DataFlowEdge edge=new DataFlowEdge(id,srcVertex,destVertex);
		    	AttribGroupSet attributes=getDefaultTemplate().constructAttribGroupSet(this, edge.getType(), edge.getId());
		    	edge.setAttributes(attributes);
		    	loadProperties(edge,(Element)connectionNode.getElementsByTagName("attribute_groups").item(0));	    	
		    	
		    	edges.put(id,edge);
		    	int id_int=Integer.parseInt(id);
		    	if (id_int>=firstFreeEdgeId) firstFreeEdgeId=id_int+1;
		    	
		    	connectionMatrix[Integer.parseInt(srcVertex.getId())][Integer.parseInt(destVertex.getId())]=true;
	    	}	
  
	    }catch (TransformerException e){
	    	e.printStackTrace();
	    	throw new PathwayFormatException("File error");
	    }
	    
	    setState(FLAG_EDITABLE);   	
	    fireModelLoaded();
}

 private void loadProperties(FlowChartElement dfe,Element ags){
	 
	 try{
	
		 NodeList agList=XPathAPI.selectNodeList(ags,"attribute_group");
		 
		 for (int i = 0; i < agList.getLength(); i++) {
			 Element currentAG=(Element)agList.item(i);
			 String currentAG_type_id=currentAG.getAttribute("type_id");
			 
			 NodeList aList=XPathAPI.selectNodeList(currentAG, "attribute");
			 
			 for (int j = 0; j < aList.getLength(); j++) {
			
				 Element currentA=(Element)aList.item(j);
				 String currentA_type_id=currentA.getAttribute("type_id");
				 
				 NodeList itemList=XPathAPI.selectNodeList(currentA,"value/items/item");
				 
				 Object[] values;
				 
				 if(itemList.getLength()==0){
					 values=new Object[1];
					 values[0]=currentA.getElementsByTagName("value").item(0).getTextContent();
				 }else{
					values=new Object[itemList.getLength()];
					for (int k = 0; k < itemList.getLength(); k++) {
						

						NodeList itemElementChildren=itemList.item(k).getChildNodes();
						
						int childrenCount=itemElementChildren.getLength();
						
						values[k]=itemElementChildren.item(0).getTextContent();
						
						for (int k2 = 0; k2 < childrenCount; k2++) {
							if (itemElementChildren.item(k2) instanceof Element){
								values[k]=itemElementChildren.item(k2);
							}
						}							 
					}
				 } 
				 				 
				dfe.getAttributes().setValue(currentAG_type_id,currentA_type_id,values);
				 
			 }
			 
		 }
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }	 
	 
 }
 
 
 public String getModelAsXMLString(DataFlowCanvas canvas){
	 try{
		 Source source=new DOMSource(getModelAsDOM(canvas));
		 StringWriter strWriter=new StringWriter();
		 
		 Result result=new StreamResult(strWriter);
		 
		 
		 Transformer xformer = TransformerFactory.newInstance().newTransformer();
		 xformer.transform(source,result);
		 
		 return strWriter.toString();
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 
	 return "";
 }
 
 public Document getModelAsDOM(){
	 return getModelAsDOM(null);
 }
 
 
 
 public Document getModelAsDOM(DataFlowCanvas canvas){
	 
	 //update last_modified metadata
	 
	 Date date=new Date(System.currentTimeMillis());
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	 String dateStr=sdf.format(date);
	 metadata.get("last_modified")[0].setDate(dateStr);
	 
	 try{
		 
		 DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		 DocumentBuilder builder = factory.newDocumentBuilder();
		 Document doc=builder.newDocument();

		 // *** 1. create file "envelope" (bitpathways element)
		 Element rootNode=doc.createElement("bitpathways");
		 rootNode.setAttribute("version", "2.0");
 		
		 doc.appendChild(rootNode);
		 
		 // *** 2. create path root 
		 Element pathNode=doc.createElement("path");
		 pathNode.setAttribute("id",path.getId());
		 
		 // offset problem
		 if (canvas!=null){
			 Rectangle r=canvas.getImageRectangle();
			 pathNode.setAttribute("img_offset_x", ""+r.x);
			 pathNode.setAttribute("img_offset_y", ""+r.y);
		 }
		 
		 rootNode.appendChild(pathNode);
		 
		 // *** 3. create templates element
				 	
		 Element templatesNode=doc.createElement("templates");
		 
		 for (int i = 0; i < templates.length; i++) {
			 
			 Element templateNode=doc.createElement("template_ref");
			 templateNode.setAttribute("id", templates[i].getId());
			 templateNode.setAttribute("repository", templates[i].getRepository());
			 
			 templatesNode.appendChild(templateNode);
		 }
		 
		 pathNode.appendChild(templatesNode);
		 
		 // *** 4. create metadata element
		 
		 Element metadataNode=doc.createElement("metadata");
		 XMLProcessor.writeXMLmetadata(metadata,doc,metadataNode);
		 
		 pathNode.appendChild(metadataNode);
		 
		 // *** 5. create subpaths element
		 
		 Element subpathsNode=doc.createElement("subpaths");

		 FlowChartNode[] nodes=getAllVertices();
		 
		 for (FlowChartNode flowchartNode : nodes) {
			 
			 if (flowchartNode instanceof DataFlowSubpath){
				 
				 Object[] subpathValues=flowchartNode.getAttributes().getSpecialValue("subpath");
				 
				 if (subpathValues!=null){
					 String path_id=subpathValues[0].toString();
					 
					 if (path_id.trim().length()>0){			 
						 Element subpathNode=doc.createElement("subpath");
						 subpathNode.setAttribute("element_id", flowchartNode.getId());
						 subpathNode.setAttribute("id", path_id);
					 }
				 }
			 }
		 }
		 
		 pathNode.appendChild(subpathsNode); 
		 
		 // *** 6. create header element
		 
		 // Save path header
		 
		 Element headerNode=path.getElementAsDOM(doc);
		 pathNode.appendChild(headerNode); 
		  
		 // *** 7. create elements element

		 Element elementsNode=doc.createElement("elements");
		 pathNode.appendChild(elementsNode);
	
		 // Save verticies
		 
		 FlowChartNode[] vers=getAllVertices();

		 
		 for (int i = 0; i < vers.length; i++) {			 
			 Element elementNode=vers[i].getElementAsDOM(doc);
			 elementsNode.appendChild(elementNode);
		 }
		 
		 // *** 8. create connections element
		 
		 Element connectionsNode=doc.createElement("connections");
		 pathNode.appendChild(connectionsNode);	
		 
		 DataFlowEdge[] eds=getAllEdges();
		 DataFlowEdge edge;
		 
		 for (int i = 0; i < eds.length; i++) {
			 edge=eds[i];
			 
			 Element connectionNode=edge.getElementAsDOM(doc);
			 connectionsNode.appendChild(connectionNode);
		 }
		
		 return doc;
		 
	 }catch(Exception e){
		 e.printStackTrace();
	 }
	 
	 return null;
 }
 
 

 
 /**
  * gives each node in graph a number
  * indicating the chronological order 
  * @return
  */
 private int[] getElementOrder(){
	 int[] order=new int[firstFreeVertexId];
	 return order;
 }

 
 
 private void enablePathOptions(boolean enable){
	 if (actions==null) return;
	 
	 HashSet<Class> exclude=new HashSet<Class>();
	 exclude.add(LoadFromFileAction.class);
	 exclude.add(LoadFromServerAction.class);
	 exclude.add(NewPathCommmand.class);
	 
	 for (int i = 0; i < actions.length; i++) {
		if (!exclude.contains(actions[i].getClass())){
				actions[i].setEnabled(enable);	
		}
	 }
 }


public Template getDefaultTemplate(){
	return templates[0];
}

public void setDefaultTemplate(Template template){
	templates[0]=template;
}

public Document getDocument(){
	return doc;
}
 
public DataFlowPath getPathElement(){
	return path;
}

public DataFlowEdge getSelectedEdge() {
	return selectedEdge;
}

public void setSelectedEdge(DataFlowEdge selectedEdge) {
	this.selectedEdge = selectedEdge;
	fireElementSelected(selectedEdge);
}

public void setSelectedPath(){
	fireElementSelected(path);
}

public void deactivateSelection(){
	
	
	
	if (selectedEdge!=null) selectedEdge.setSelected(false);
	
	for (int i = 0; i < selectedVertices.length; i++) {
		
		if (selectedVertices[i]==null) break; 
		else {
			//System.out.println("Deactivated node at position "+i+" with ID="+selectedVertices[i].getId());
			selectedVertices[i].setSelected(false);
		}
	}
		
	this.selectedEdge=null;
	this.selectedVertices=new FlowChartNode[MAX_SELECTED_VERTICES];

}

public FlowChartNode getFirstSelectedVertex() {
	return selectedVertices[0];
}

public FlowChartNode[] getSelectedVertices() {

	FlowChartNode[] sv=new FlowChartNode[getSelectedVerticesCount()];
	for (int i = 0; i < sv.length; i++) {
		sv[i]=selectedVertices[i];
	}
	return sv;
}

public void setSelectedVertex(FlowChartNode selectedVertex) {
	if (selectedVertex!=getFirstSelectedVertex()){
		deactivateSelection();
		selectedVertices[0]=selectedVertex;
		selectedVertex.setSelected(true);
		fireElementSelected(selectedVertex);
	}
}

public void addVertexToSelection(FlowChartNode selectedVertex) {
		
		if (!selectedVertex.isSelected()){			
			selectedVertices[getSelectedVerticesCount()]=selectedVertex;
			selectedVertex.setSelected(true);
		}
}



public int getSelectedVerticesCount(){
	
	for (int i = 0; i < selectedVertices.length; i++) {
		if (selectedVertices[i]==null) return i;
	}
	return selectedVertices.length-1;
}


public AttribGroupSet getEdgeAttributes(String id){
	return ((DataFlowEdge)edges.get(id)).getAttributes();
}

public AttribGroupSet getNodeAttributes(String id){
	Object o=vertices.get(id);
	return ((FlowChartNode)vertices.get(id)).getAttributes();
}

public AttribGroupSet getPathAttributes(){
	return path.getAttributes();
}

public File getLastSavedFile() {
	return lastSavedFile;
}

public void setLastSavedFile(File lastSavedFile) {
	this.lastSavedFile = lastSavedFile;
}

public boolean isModified() {
	return (state>1);
}

public String getPathId() {
	return path.getId();
}

public void setPathId(String _pathid) {
	path.setId(_pathid);
}


public int getUid(){
	return main.getUser().getUid();
}

public String getPathTitle(){
	String ret="";
	
	if(metadata!=null){
		Object title=metadata.get("title");
		
		if (title!=null) ret+=((MetadataEntity[])title)[0].getName();
	}
	
	return ret;
}

//----------------------------------------------------------------------------------------------
// access state model
	
	public boolean isInState(int flag){
		
		if (flag==0) return state==flag;  
		return (state&flag)>0; 
	}
	
	public void addStateFlag(int flag){
		state=state|flag;
	}
	
	public void removeStateFlag(int flag){
		state=state^flag;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		
		if (state>0) enablePathOptions(true);
		else enablePathOptions(false);
	}


}
  
