/*
Bit Pathways Editor by Andrzej A. Kononowicz, 2012

Academic Free License ("AFL") v. 3.0

You should have received a copy of the Academic Free License along with this library.
*/

package edu.uj.zbit.bpath.editor.model.elements;

import java.awt.*;


/**
 * <p>
 * Stellt einen Pfeil dar, der noch gezeichnet wird.
 * Der Pfeil hat nur ein Ende verankert, das andere Ende
 * kann frei bewegt werden
 * </p>
 * 
 * @author Andrzej Kononowicz
 * @version 1.0
 */

public class DataFlowTempEdge extends DataFlowEdge {

  protected Point arrowEnd;

  public DataFlowTempEdge(FlowChartNode source, int sourceIndex, Point p) {
    super("-1",source, null);
    arrowEnd = p;
  }

  public void paint(Graphics g) {
    Point p;
    p = srcVertex.getConnectionPoint(srcHotRegion);
    x1 = p.x;
    y1 = p.y;
    p = arrowEnd;
    x2 = p.x;
    y2 = p.y;

    drawEdgeFromTo(g);
  }
  public Point getArrowEnd() {
    return arrowEnd;
  }
  public void setArrowEnd(Point arrowEnd) {
    this.arrowEnd = arrowEnd;
  }
}
