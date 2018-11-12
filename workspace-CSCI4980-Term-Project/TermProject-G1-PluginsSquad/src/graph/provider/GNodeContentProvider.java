/*
 * @(#) ZestNodeContentProvider.java
 *
 */
package graph.provider;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import graph.model.GNode;

public class GNodeContentProvider extends ArrayContentProvider implements IGraphEntityContentProvider {
   /* ArrayContentProvider - This implementation of IStructuredContentProvider handles 
    * the case where the viewer input is an unchanging array or collection of elements. 
    */
   @Override
   public Object[] getConnectedTo(Object entity) {
      if (entity instanceof GNode) {
         GNode node = (GNode) entity;
         return node.getConnectedTo().toArray();
      }
      throw new RuntimeException("Type not supported");
   }
}