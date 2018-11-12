/*
 * @(#) NodeModelContentProvider.java
 *
 */
package graph.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.model.GConnection;
import graph.model.GNode;

public class GModelProvider {
   private static List<GConnection>  connections = new ArrayList<GConnection>();
   private static List<GNode>        nodes       = new ArrayList<GNode>();

   private static Map<String, GNode> nodeMap     = new HashMap<String, GNode>();

   static GModelProvider             singleton   = null;

   public GModelProvider() {
   }

   public static GModelProvider instance() {
      if (singleton == null) {
         singleton = new GModelProvider();
      }
      return singleton;
   }

   public List<GNode> getNodes() {
      return nodes;
   }

   public List<GConnection> getConnections() {
      return connections;
   }

   public Map<String, GNode> getNodeMap() {
      return nodeMap;
   }

   public String getConnectionLabel(String srcId, String dstId) {
      for (GConnection iCon : connections) {
         if (iCon.getSource().getId().equals(srcId) && //
               iCon.getDestination().getId().equals(dstId)) {
            return iCon.getLabel();
         }
      }
      return "";
   }

   public void reset() {
      nodes.clear();
      nodeMap.clear();
   }
}
