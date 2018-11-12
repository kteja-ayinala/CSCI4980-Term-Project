/*
 * @(#) ZestLabelProvider.java
 *
 */
package graph.provider;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;

import graph.model.GClassNode;
import graph.model.GNode;
import graph.model.GNodeType;
import graph.model.GPackageNode;

public class GLabelProvider extends LabelProvider implements IEntityStyleProvider {
   @Override
   public String getText(Object element) {
      // Create a label for node.
      if (element instanceof GNode) {
         GNode myNode = (GNode) element;
         return myNode.getName();
      }
      // Create a label for connection.
      if (element instanceof EntityConnectionData) {
         EntityConnectionData eCon = (EntityConnectionData) element;
         if (eCon.source instanceof GNode) {
            return GModelProvider.instance().getConnectionLabel( //
                  ((GNode) eCon.source).getId(), //
                  ((GNode) eCon.dest).getId());
         }
      }
      return "";
   }

   @Override
   public boolean fisheyeNode(Object arg0) {
      return false;
   }

   @Override
   public Color getBackgroundColour(Object o) {
      return getNodeColor(o);
   }

   @Override
   public Color getNodeHighlightColor(Object o) {
      return getNodeColor(o);
   }

   @Override
   public Color getForegroundColour(Object arg0) {
      return ColorConstants.black;
   }

   @Override
   public Color getBorderHighlightColor(Object arg0) {
      return ColorConstants.red;
   }

   private Color getNodeColor(Object o) {
      if (o instanceof GPackageNode) {
         return ColorConstants.lightGreen;
      }
      if (o instanceof GClassNode) {
         return ColorConstants.lightBlue;
      }
      return ColorConstants.yellow;
   }

   @Override
   public Color getBorderColor(Object o) {
      if (o instanceof GNode && ((GNode) o).getNodeType() == GNodeType.UserSelection) {
         return ColorConstants.red;
      }
      return null;
   }

   @Override
   public int getBorderWidth(Object arg0) {
      return 0;
   }

   @Override
   public IFigure getTooltip(Object arg0) {
      return null;
   }
}
