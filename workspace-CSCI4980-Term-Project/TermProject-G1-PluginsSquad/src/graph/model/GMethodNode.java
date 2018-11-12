/*
 * @(#) GMethodNode.java
 *
 */
package graph.model;

public class GMethodNode extends GNode {
   private String prjName;
   private String pkgName;
   private String className;

   public GMethodNode(String id, String name, String parent) {
      super(id, name, parent);
   }

   public GMethodNode setPrjName(String prjName) {
      this.prjName = prjName;
      return this;
   }

   public GMethodNode setPkgName(String pkgName) {
      this.pkgName = pkgName;
      return this;
   }

   public GMethodNode setClassName(String className) {
      this.className = className;
      return this;
   }

   public String getPrjName() {
      return prjName;
   }

   public String getPkgName() {
      return pkgName;
   }

   public String getClassName() {
      return className;
   }

   public boolean isParent(GClassNode n) {
      boolean eqClassName = this.className.equals(n.getName());
      boolean eqPkgName = this.pkgName.equals(n.getPkgName());
      return eqClassName && eqPkgName;
   }

   public String toString() {
      return this.pkgName + "." + this.className + "." + this.getName();
   }
}
