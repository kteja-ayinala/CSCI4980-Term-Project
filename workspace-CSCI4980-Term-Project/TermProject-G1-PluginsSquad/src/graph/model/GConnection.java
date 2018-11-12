/*
 * @(#) MyConnection.java
 *
 */
package graph.model;

public class GConnection {
   final String id;
   final String label;
   final GNode  source;
   final GNode  destination;

   public GConnection(String id, String name, GNode source, GNode destination) {
      this.id = name;
      this.label = name;
      this.source = source;
      this.destination = destination;
   }

   public String getLabel() {
      return label;
   }

   public GNode getSource() {
      return source;
   }

   public GNode getDestination() {
      return destination;
   }
}
