package graph.model;

public class GVariableNode extends GNode {
   private String prjName;
   private String pkgName;
   private String className;
   private String methodName;

   public GVariableNode(String id, String name, String parent) {
      super(id, name, parent);
   }

   public GVariableNode setPrjName(String prjName) {
      this.prjName = prjName;
      return this;
   }

   public GVariableNode setPkgName(String pkgName) {
      this.pkgName = pkgName;
      return this;
   }

   public GVariableNode setClassName(String className) {
      this.className = className;
      return this;
   }
   
   public GVariableNode setMethodName(String methodName) {
	      this.methodName = methodName;
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
   
   public String getMethodName() {
	      return methodName;
	   }

   public boolean isParent(GMethodNode n) {
	  boolean eqMethodName = this.methodName.equals(n.getName());
      boolean eqClassName = this.className.equals(n.getClassName());
      boolean eqPkgName = this.pkgName.equals(n.getPkgName());
      return eqMethodName && eqClassName && eqPkgName;
   }

   public String toString() {
      return this.pkgName + "." + this.className + "." + this.getMethodName() + "." +  this.getName();
   }
}

