/*
 * @(#) ASTAnalyzer.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package analysis;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import graph.model.GClassNode;
import graph.model.GMethodNode;
import graph.provider.GModelProvider;
import visitor.DeclarationVisitor;
import visitor.VariableVisitor;

public class MethodAnalyzer {
   private static final String JAVANATURE = "org.eclipse.jdt.core.javanature";
   protected String prjName, pkgName;
   
   private GMethodNode method;
   private GClassNode classNode;
   private IMethod methodElem;
   private IType typeElem;
   private ILocalVariable var;
   private ICompilationUnit iCUnitInvolved;
   

   public MethodAnalyzer() {
	   
   }

   public void analyze() {
      GModelProvider.instance().reset();

      // =============================================================
      // 1st step: Project
      // =============================================================
      try {
         IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
         for (IProject project : projects) {
    	  
            if (!project.isOpen() || !project.isNatureEnabled(JAVANATURE)) { // Check if we have a Java project.
               continue;
            }
            prjName = project.getName();
            if(prjName.equals(method.getPrjName())) {
            analyzePackages(JavaCore.create(project).getPackageFragments());
            //exit loop
            break;
            }
         }
      } catch (JavaModelException e) {
         e.printStackTrace();
      } catch (CoreException e) {
         e.printStackTrace();
      }
   }

   protected void analyzePackages(IPackageFragment[] packages) throws CoreException, JavaModelException {
      // =============================================================
      // 2nd step: Packages
      // =============================================================
      for (IPackageFragment iPackage : packages) {
         if (iPackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
            if (iPackage.getCompilationUnits().length < 1) {
               continue;
            }
            pkgName = iPackage.getElementName();
            if(pkgName.equals(method.getPkgName())) {
            analyzeCompilationUnit(iPackage.getCompilationUnits());
            break;
            }
         }
      }
   }

   public void analyzeCompilationUnit(ICompilationUnit[] iCompilationUnits) throws JavaModelException {
	   // =============================================================
	   // 3rd step: ICompilationUnits
	   // =============================================================
	   for (ICompilationUnit iUnit : iCompilationUnits) {
		   
		   if (iUnit.getElementName().equals(method.getClassName() +".java")) {
			  /* for (IJavaElement elem : iUnit.getResource().getChildren()) {
				   if (elem.getElementName().equals(method)) {*/
			   IType type = iUnit.findPrimaryType();
	           IMethod[] methods = type.getMethods();
	            for(IMethod imethod : methods) {
	            	if (imethod.getElementName().equals(method.getName()) ) {
	            	   CompilationUnit compilationUnit = parse(iUnit);
					   VariableVisitor declVisitor = new VariableVisitor(method);
					   compilationUnit.accept(declVisitor);
				   }
			   }
		   }
	   };


   }



public boolean visit() {
	return false;

}
   
   
   public String getPrjName() {
	return prjName;
}

public void setPrjName(String prjName) {
	this.prjName = prjName;
}

public String getPkgName() {
	return pkgName;
}

public void setPkgName(String pkgName) {
	this.pkgName = pkgName;
}

public GMethodNode getMethod() {
	return method;
}

public void setMethod(GMethodNode method) {
	this.method = method;
}

public GClassNode getClassNode() {
	return classNode;
}

public void setClassNode(GClassNode classNode) {
	this.classNode = classNode;
}

public IMethod getMethodElem() {
	return methodElem;
}

public void setMethodElem(IMethod methodElem) {
	this.methodElem = methodElem;
}

public IType getTypeElem() {
	return typeElem;
}

public void setTypeElem(IType typeElem) {
	this.typeElem = typeElem;
}

public ILocalVariable getVar() {
	return var;
}

public void setVar(ILocalVariable var) {
	this.var = var;
}

public ICompilationUnit getiCUnitInvolved() {
	return iCUnitInvolved;
}

public void setiCUnitInvolved(ICompilationUnit iCUnitInvolved) {
	this.iCUnitInvolved = iCUnitInvolved;
}

private static CompilationUnit parse(ICompilationUnit unit) {
      ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      parser.setSource(unit);
      parser.setResolveBindings(true);
      return (CompilationUnit) parser.createAST(null); // parse
   }
}