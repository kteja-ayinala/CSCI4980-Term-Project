package util;

import java.util.EventObject;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphNode;

import graph.model.GClassNode;
import graph.model.GMethodNode;
import graph.model.GNode;
import graph.model.GNodeType;
import graph.model.GPackageNode;

public class UtilNode {
   public static void resetDstNode(GraphNode graphNode, GNode node) {
      if (graphNode == null || graphNode.isDisposed() || !(node instanceof GClassNode))
         return;
      graphNode.setForegroundColor(ColorConstants.black);
      graphNode.setBackgroundColor(ColorConstants.lightBlue);
      graphNode.setBorderColor(ColorConstants.lightBlue);
      graphNode.setHighlightColor(ColorConstants.lightBlue);
      graphNode.setBorderHighlightColor(ColorConstants.lightBlue);
      node.setNodeType(GNodeType.InValid);
   }

   public static void resetPackageNode(GraphNode graphNode, GNode node) {
      if (graphNode == null || graphNode.isDisposed() || !(node instanceof GPackageNode)) {
         return;
      }
       graphNode.setForegroundColor(ColorConstants.black);
       graphNode.setBackgroundColor(ColorConstants.lightGreen);
       graphNode.setBorderColor(ColorConstants.red);
       graphNode.setHighlightColor(ColorConstants.lightGreen);
       graphNode.setBorderHighlightColor(ColorConstants.red);
       node.setNodeType(GNodeType.InValid);
   }

   public static boolean isPackageNode(EventObject e) {
      Object obj = e.getSource();
      Graph graph = (Graph) obj;

      boolean result = false;
      result = (!graph.getSelection().isEmpty() && //
         graph.getSelection().get(0) instanceof GraphNode && //
				((GraphNode) graph.getSelection().get(0)).getData() instanceof GPackageNode);
      return result;
   }

   public static boolean isClassNode(EventObject e) {
      Object obj = e.getSource();
      Graph graph = (Graph) obj;

      return (!graph.getSelection().isEmpty() && //
            graph.getSelection().get(0) instanceof GraphNode && //
            ((GraphNode) graph.getSelection().get(0)).getData() instanceof GClassNode);
   }

   public static boolean isMethodNode(EventObject e) {
      Object obj = e.getSource();
      Graph instGraph = (Graph) obj;
      List<?> selection = instGraph.getSelection();
      return ((!selection.isEmpty() && selection.get(0) instanceof GraphNode) && //
            (((GraphNode) selection.get(0)).getData() instanceof GMethodNode));
   }
}
