/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.command;

import java.lang.reflect.Constructor;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import edu.uj.zbit.bpath.editor.BpathMainFrame;
import edu.uj.zbit.bpath.editor.model.PathModel;
import edu.uj.zbit.bpath.editor.model.elements.FlowChartNode;

public class AddGenericVertex extends BpathAction{
	
	private Class<FlowChartNode> vClass;
	
	public AddGenericVertex(PathModel model,JFrame parent,Class<FlowChartNode> vertexClass,ImageIcon icon){
		
		super(
				BpathMainFrame.messages.getString("ELEMENT."+vertexClass.getSimpleName()),
				icon,
				model,
				parent
			);
				
		this.vClass=(Class<FlowChartNode>)vertexClass;
	}
	
	public void doIt(){
		super.doIt();
		try{
			model.addVertex(vClass);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void undoIt(){
		//TODO
	}
	
}
