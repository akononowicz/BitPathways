/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DeferredElementNSImpl;
import org.apache.xerces.dom.DeferredTextImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.*;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.ControledVocabularyDict;
import edu.uj.zbit.bpath.editor.propertyPanel.CiteCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.CiteCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.DurationCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.DurationCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.HTMLCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.HTMLCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.HTMLcodeCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.LabelHeaderCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.LabelListItemCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.LinkCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.LinkCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.LinkEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.ListControlCell;
import edu.uj.zbit.bpath.editor.propertyPanel.PersonCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.PersonCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.ReadOnlyCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.ReadOnlyCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.StringCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.StringCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.SubpathCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.SubpathCellRenderer;
import edu.uj.zbit.bpath.editor.propertyPanel.TestQuestionCellEditor;
import edu.uj.zbit.bpath.editor.propertyPanel.TestQuestionCellRenderer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * 
 * Attributes are divided into groups. 
 * This class represented a single group of attributes.
 * It is aggregated by an instance of AttribGroupSet which is directly 
 * attached to a descendant of the DataFlowNode class.
 * Access to attributes is given by the DataFlowElement attribute.
 * 
 * Values of the attributes are stored as XML Objects (DOM API)
 * and kept in the xmlValues array. If an element contains a list it has only one value element and 
 * within this element there is a set of &lt;item&gt; elements. 
 * 
 * @author Andrzej A. Kononowicz
 *
 */


public class AttribGroup extends AbstractTableModel {

	
	/** Id of the element to which this group is attached*/
	private String element_id; 
	
	/** Attrib group name */
	private String group_id; 
	
	/**
	 * stores attrib identifier and list of values for this attribute (more>1 if type=dict) 
	 */
	private Hashtable<String, Object[]> attribValues;
		
	/**
	 * Ordered list of attrib names
	 */
	private String[] attribNames;

	/** Informs how many rows in the table are occupied by the attribute. 
	 * The number is greater than 1 in the case the attribute contains a value list*/
	private int[] rowCount;
	
	/** Reference to the model */
	private PathModel model;
	
	/** object holding all renderers and editors */
	private RenderEditorModel renderEditorModel;
	
    private static final Logger log = Logger.getLogger(AttribGroup.class);
    
	//------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public AttribGroup(String _element_id, String _group_id, PathModel _model, Hashtable<String, Object[]> _attribValues, String[] _attribNames){
		
		group_id=_group_id;
		element_id=_element_id;
		model=_model;
		attribValues=_attribValues;
		attribNames=_attribNames;
		
		rowCount=new int[attribNames.length];
		
		for (int i = 0; i < rowCount.length; i++) {
			rowCount[i]=1;
		}
				
		updateRenderEditorModel();	
	}
	
	
	
	/**
	 * prepares the way in which the table is displayed, that means for instance which editors should
	 * be used for which row
	 */
	public void updateRenderEditorModel(){
		renderEditorModel=new RenderEditorModel(this);
		boolean listItem=false;
		
		for (int i = 0; i < getRowCount(); i++) {
			String type=getRowType(i);
					
			JTable jtable=new JTable();
			
			//	====== renders for name(label) column ====
			
			if (type.equalsIgnoreCase("list")){
				if (getListOffset(i)>0){
					renderEditorModel.addRendererForRow(i,0,new LabelListItemCellRenderer());
				}else{
					renderEditorModel.addRendererForRow(i,0,new LabelHeaderCellRenderer());
				}
			}else{
				renderEditorModel.addRendererForRow(i,0,new LabelHeaderCellRenderer());
			}
			
			
			// ====== renders and editors for value column ====
			
			// TODO: THIS PART SHOULD BE IMPLEMENTED BY context.xml
			
			if (type.equalsIgnoreCase("list")){
				if (getListOffset(i)>0){
					type=getListItemType(i);
					listItem=true;
				}
			}
			
			if (type.equalsIgnoreCase("string")){
				renderEditorModel.addEditorForRow(i,new StringCellEditor(model.getDocument()));
				renderEditorModel.addRendererForRow(i,1,new StringCellRenderer());
				
			}else if (type.equalsIgnoreCase("boolean")){
				renderEditorModel.addEditorForRow(i,jtable.getDefaultEditor(Boolean.class));
				renderEditorModel.addRendererForRow(i,1,jtable.getDefaultRenderer(Boolean.class));
				
			}/*else if (type.equalsIgnoreCase("combo")){
				String dictName=xmlValues[i].getAttribute("dict");
				JComboBox comboBox=new JComboBox(ControledVocabularyDict.getDictItems(dictName,BpathMainFrame.language));
				renderEditorModel.addEditorForRow(i,new DefaultCellEditor(comboBox));
				renderEditorModel.addRendererForRow(i,1,jtable.getDefaultRenderer(String.class));
				
			}*/else if (type.equalsIgnoreCase("list")){		
				ListControlCell lct=new ListControlCell(this,i);
				renderEditorModel.addRendererForRow(i,1,lct);
				renderEditorModel.addEditorForRow(i,lct);
			}else if (type.equalsIgnoreCase("html")){		
				renderEditorModel.addEditorForRow(i,new HTMLCellEditor());
				renderEditorModel.addRendererForRow(i,1,new HTMLCellRenderer());	
			}else if (type.equalsIgnoreCase("htmlcode")){		
				renderEditorModel.addEditorForRow(i,new HTMLcodeCellEditor());
				renderEditorModel.addRendererForRow(i,1,new HTMLCellRenderer());	
			}else if (type.equalsIgnoreCase("link")){		
				renderEditorModel.addEditorForRow(i,new LinkCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new LinkCellRenderer());
			}else if (type.equalsIgnoreCase("cite")){		
				renderEditorModel.addEditorForRow(i,new CiteCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new CiteCellRenderer());	
			}else if (type.equalsIgnoreCase("duration")){		
				renderEditorModel.addEditorForRow(i,new DurationCellEditor());
				renderEditorModel.addRendererForRow(i,1,new DurationCellRenderer());	
			}else if (type.equalsIgnoreCase("person")){		
				renderEditorModel.addEditorForRow(i,new PersonCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new PersonCellRenderer());	
			}else if (type.equalsIgnoreCase("subpath")){		
				renderEditorModel.addEditorForRow(i,new SubpathCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new SubpathCellRenderer());	
			}/*else if (type.equalsIgnoreCase("activity")){		
				renderEditorModel.addEditorForRow(i,new ActivityCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new ActivityCellRenderer());	
			}*/else if (type.equalsIgnoreCase("test")){		
				renderEditorModel.addEditorForRow(i,new TestQuestionCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new TestQuestionCellRenderer());	
			}/*else if (type.equalsIgnoreCase("code_p")){		
				renderEditorModel.addEditorForRow(i,new ClassificationCellEditor(this, ClassificationCellEditor.TYPE_PROCEDURE));
				renderEditorModel.addRendererForRow(i,1,new ClassificationCellRenderer());	
			}else if (type.equalsIgnoreCase("code_d")){		
				renderEditorModel.addEditorForRow(i,new ClassificationCellEditor(this,ClassificationCellEditor.TYPE_DIAGNOSIS));
				renderEditorModel.addRendererForRow(i,1,new ClassificationCellRenderer());	
			}*/else if (type.equalsIgnoreCase("mcq")){		
				renderEditorModel.addEditorForRow(i,new TestQuestionCellEditor(this));
				renderEditorModel.addRendererForRow(i,1,new TestQuestionCellRenderer());	
			}else if (type.equalsIgnoreCase("readonly")){		
				renderEditorModel.addRendererForRow(i,1,new ReadOnlyCellRenderer());	
				renderEditorModel.addEditorForRow(i,new ReadOnlyCellEditor());
				/*renderEditorModel.addEditorForRow(i,new StringCellEditor());
				renderEditorModel.addRendererForRow(i,1,new StringCellRenderer());*/
			}else{
				renderEditorModel.addEditorForRow(i,jtable.getDefaultEditor(String.class));
				renderEditorModel.addRendererForRow(i,1,jtable.getDefaultRenderer(String.class));	
			}
			
			// END OF PART TO BE IMPLEMENTED BY context.xml
		}
	}
		
	public Object getValueAt(int row, int column){
		
		String attrib_id=attribNames[getElementIndex(row)];
		
		if (column==0) return getRowLabel(attrib_id,row);			
		else /*(column==1)*/ return getRowContent(attrib_id,row);
	}	
	
	private String getRowLabel(String attib_id, int row){
		String rowLabel="";
		
		if (getListOffset(row)==0) 
			rowLabel=model.getDefaultTemplate().getLabel(group_id, attib_id, BpathMainFrame.language);   
		else 
			rowLabel=model.getDefaultTemplate().getItemLabel(group_id, attib_id, BpathMainFrame.language);
		
		return rowLabel;
	}

	private Object getRowContent(String attrib_id, int row) {

		String rowType=getRowType(row);
		
		if (!rowType.equalsIgnoreCase("list")){
			return attribValues.get(attrib_id)[0];
		}else{
			int listOffset=getListOffset(row);
			if (listOffset==0) return null;
			else return attribValues.get(attrib_id)[listOffset-1];
		}
	}
	
	/** 
	 * argument value can be either String or Element
	 */
	public void setValueAt(Object value,int row,int column){
		
		if (column==1){
		
			Object oldValue=null;
			
			String attrib_id=attribNames[getElementIndex(row)];
			String rowType=getRowType(row);
			
			if (!rowType.equalsIgnoreCase("list")){
				oldValue=attribValues.get(attrib_id)[0];
				attribValues.get(attrib_id)[0]=value;
			}else{
				int listOffset=getListOffset(row);
				if (listOffset!=0){ 
					oldValue=attribValues.get(attrib_id)[listOffset-1];
					attribValues.get(attrib_id)[listOffset-1]=value;
				}
			}
			
			model.fireElementChanged(element_id,group_id, attrib_id, oldValue, value);
			
		}
	}	
	
	public int getRowCount(){
		
		int rc=0;
		for (int i = 0; i < rowCount.length; i++) {
			rc+=rowCount[i];
		}
		return rc;
	}

	public boolean isCellEditable (int row, int col){
		if (col==1) return true; else return false;
	}
	
	public int getColumnCount(){
		return 2;
	}
	
	/**
	 * @param listIndex - index in the attributes table
	 */
	public void addRowToList(int row){
		int ai=getElementIndex(row);

		Object[] oldNodes=attribValues.get(attribNames[ai]);
		Object[] newNodes=new Object[oldNodes.length+1];
		
		for (int i = 0; i < oldNodes.length; i++) {
			newNodes[i]=oldNodes[i];
		}
		
		attribValues.put(attribNames[ai], newNodes);
		
		rowCount[ai]+=1;	
		updateRenderEditorModel();
		
		fireTableStructureChanged();
	}
	
	public void removeRowFromList(int row){
		
		int offset=getListOffset(row);
		if (offset==0) return;
		
		int ai=getElementIndex(row);

		Object[] oldNodes=attribValues.get(attribNames[ai]);
		
		if (oldNodes.length==1){
			oldNodes[0]=null;
		}else{
			Object[] newNodes=new String[oldNodes.length-1];
			
			int skip=0;
			
			for (int i = 0; i < newNodes.length; i++) {
				if (i==(offset-1)) skip=1;
				
				newNodes[i]=oldNodes[i+skip];
			}
			attribValues.put(attribNames[ai], newNodes);
		}
		
		
		rowCount[ai]-=1;	
		updateRenderEditorModel();
		
		fireTableStructureChanged();
	}
	
	
	/**
	 * Informs on which position in a list is the given row located 
	 * if the row doesn't belong to any list 0 is returned
	 * 0 is also return in the case of the row header
	 * @param row row in the property table
	 * @return
	 */
	private int getListOffset(int row){
		int ai=0,cum=0;
		
		while(cum<=row){
			cum+=rowCount[ai];
			ai++;
		}
		
		return row - (cum-rowCount[ai-1]);
	}
	
	/**
	 * Returns the index of the element to which the row belongs
	 * 
	 * @param row
	 * @return
	 */
	protected int getElementIndex(int row){
		int ai=0,cum=0;
		
		while(cum<=row){
			cum+=rowCount[ai];
			ai++;
		}
		
		return (ai-1);
	}

	/**
	 * Returns type of the element in ith row. Return "list" if pointing to a list item 
	 */
	protected String getRowType(int row){
		
		int ai=getElementIndex(row);
		String type=model.getDefaultTemplate().getType(group_id, attribNames[ai]);	
		return type;
		
	}

	
	/**
	 * Returns list item type of the selected row
	 */
	
	protected String getListItemType(int row){
		
		if (getListOffset(row)>0){
			int ai=getElementIndex(row);
			String listItemType=model.getDefaultTemplate().getItemType(group_id, attribNames[ai]);	
			return listItemType;
		}
		
		return getRowType(row);
	}
	
	
	public RenderEditorModel getRenderEditorModel(){
		return renderEditorModel;
	}
	

	public String getName(){
		return model.getDefaultTemplate().getLabel(group_id, BpathMainFrame.language);
	}
	

	
	/** Finds the row for the given attribute index */
	private int getRowForIndex(int index){
		int ret=0;
		for (int i = 0; i < index; i++) {
			ret+=rowCount[i];
		}
		return ret;
	}
	
	private int getIndexForAttrib(String attrib){
		
		int ret=-1;
		
		for (int i = 0; i < attribNames.length; i++) {
			
			if (attrib.equalsIgnoreCase(attribNames[i])){
				ret=i;
				break;
			}
		}
		return ret;
	}
	
	public Object[] getAttrib(String attrib_id){
		return attribValues.get(attrib_id);
	}
	
	public void setAttrib(String attrib_id, Object[] value){
		if (attribValues.containsKey(attrib_id)){ 
			attribValues.put(attrib_id, value);
			
			String type=model.getDefaultTemplate().getType(group_id, attrib_id);
			
			if (type.equalsIgnoreCase("list")){
				int index=getIndexForAttrib(attrib_id);
				
				if (value.length==1){
					//lists with just one item
					if(value[0].toString().trim().length()>0){
						rowCount[index]=2;
					}//else, empty list
				}else{
					rowCount[index]=value.length+1;	
				}
				
				updateRenderEditorModel();
				fireTableStructureChanged();
			}	
		}
		else System.out.println("Attribute'"+attrib_id+"' not found in this template.");
	}
	
	public boolean existAttrib(String attrib_id){
		return attribValues.containsKey(attrib_id);
	}
	
	public Element[] toDOM(Document doc){
		
		Element[] retAttribs=new Element[attribNames.length];
		Element a;
		
		for (int i = 0; i < attribNames.length; i++) {
			a=doc.createElement("attribute");
			a.setAttribute("type_id", attribNames[i]);

			Element value=doc.createElement("value");
			a.appendChild(value);
			
			
			boolean isList=model.getDefaultTemplate().getType(group_id, attribNames[i]).equalsIgnoreCase("list");
			
			if (isList){
				if (rowCount[i]>1){
				
					Element items=doc.createElement("items");
					Object[] aTab=attribValues.get(attribNames[i]);
					value.appendChild(items);
					
					for (int j = 0; j < aTab.length; j++) {
						
						Element item=doc.createElement("item");
						Object ov=aTab[j];
						
						if (ov instanceof String){
							if (ov.toString().trim().length()>0){		
								item.setTextContent(ov.toString());
								items.appendChild(item);
							}
						}else{
							if(ov!=null){
								Node nov=(Node)ov;					
								item.appendChild(doc.adoptNode(nov.cloneNode(true)));
								items.appendChild(item);
							}
						}	
					}
				}
			}else{
				Object ov=attribValues.get(attribNames[i])[0];
				if (ov instanceof String){value.setTextContent(ov.toString());}
				else{
					Node nov=(Node)ov;
					value.appendChild(nov.cloneNode(true));
				}
			}
			
			retAttribs[i]=a;

		}
		
		return retAttribs;
	}
	
	
	public boolean isAttribGroupEmpty(){
		
		for (int i = 0; i < attribNames.length; i++) {
			if (!isAttribEmpty(attribNames[i])) return false;
		}
		
		return true;
		
	}
	
	public boolean isAttribEmpty(String attribName){
		
		Object v1=attribValues.get(attribName)[0];
		
		if (v1==null) return true;
		else if (v1 instanceof String ){
			return (v1.toString().trim().length()==0);
		} else return false; 
		
	}
	
	public String getAttribLabel(String attrib_id){
		return model.getDefaultTemplate().getLabel(group_id, attrib_id, BpathMainFrame.language);
	}
	
	public String getAttribType(String attrib_id){
		return model.getDefaultTemplate().getType(group_id, attrib_id);
	}
	
	public String getAttribItemType(String attrib_id){
		return model.getDefaultTemplate().getItemType(group_id, attrib_id);
	}
	
	/** notify pathway model that an attribute has been changed */
	public void pathwayModified(){
		model.addStateFlag(PathModel.FLAG_MODIFIED);
	}

	public String[] getAttribNames() {
		return attribNames;
	}
	

}