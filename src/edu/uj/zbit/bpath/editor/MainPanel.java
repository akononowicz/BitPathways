/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor;

import javax.swing.*;

import edu.uj.zbit.bpath.editor.model.PathModel;

import java.awt.*;

/**
 * <p>
 * Contains a scroll pane with the canvas 
 * presenting the flow chart (DataFlowCanvas)
 * </p>
 *
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public class MainPanel extends JPanel{

  private JScrollPane scrollPanel= new JScrollPane();
  DataFlowCanvas canvas;
  private BorderLayout br=new BorderLayout();
  private PathModel model;
   
  public MainPanel(PathModel _model) {

	model=_model;  
	  
    // Construct DataFlowCanvas
    canvas=new DataFlowCanvas(this,model);
    
    // Construct GUI
    this.setLayout(br);
    this.add(scrollPanel, BorderLayout.CENTER);
    scrollPanel.getViewport().add(canvas, null);

  }
  
  DataFlowCanvas getCanvas(){
	  return canvas;
  }
  
}
