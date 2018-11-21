/*
 * @(#) MethodVisitor.java
 *
 * Copyright 2015-2018 The Software Analysis Laboratory
 * Computer Science, The University of Nebraska at Omaha
 * 6001 Dodge Street, Omaha, NE 68182.
 */
package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import graph.model.GClassNode;
import graph.model.GConnection;
import graph.model.GMethodNode;
import graph.model.GNode;
import graph.model.GPackageNode;
import graph.provider.GModelProvider;

public class VariableVisitor extends ASTVisitor {

	public boolean visit(PackageDeclaration pkgDecl) {
		insertPackageNode(pkgDecl);
		return super.visit(pkgDecl);
	}

	private void insertPackageNode(PackageDeclaration pkgDecl) {
		IPackageBinding rBinding = pkgDecl.resolveBinding();
		String prjName = rBinding.getJavaElement().getJavaProject().getElementName();
		String nodeName = pkgDecl.getName().getFullyQualifiedName();
		String id = prjName + "." + nodeName;
		if (GModelProvider.instance().getNodeMap().containsKey(id) == false) {
			addNode(new GPackageNode(id, nodeName, prjName));
		}
	}

	/**
	 * A type declaration is the union of a class declaration
	 * and an interface declaration.
	 */
	@Override
	public boolean visit(TypeDeclaration typeDecl) {
		GNode typeNode = insertTypeNode(typeDecl);
		GNode pkgGNode = GModelProvider.instance().getNodeMap().get(typeNode.getParent());
		if (pkgGNode == null) {
			throw new RuntimeException();
		}
		addConnection(pkgGNode, typeNode, typeDecl.getStartPosition());
		return super.visit(typeDecl);
	}

	private GNode insertTypeNode(TypeDeclaration typeDecl) {
		ITypeBinding rBinding = typeDecl.resolveBinding();
		String prjName = rBinding.getPackage().getJavaElement().getJavaProject().getElementName();
		String pkgName = rBinding.getPackage().getName();

		String typeName = typeDecl.getName().getFullyQualifiedName();
		String id = prjName + "." + pkgName + "." + typeName;
		GClassNode n = new GClassNode(id, typeName, prjName + "." + pkgName);
		n.setPkgName(pkgName);
		return addNode(n);
	}

	@Override
	public boolean visit(MethodDeclaration methodDecl) {
		if(methodDecl.getName().toString().equals("mb1")) {
		GMethodNode methodNode = (GMethodNode) insertMethodNode(methodDecl);
		
		GNode typeNode = GModelProvider.instance().getNodeMap().get(methodNode.getParent());
		if (typeNode == null) {
			throw new RuntimeException();
		}
		addConnection(typeNode, methodNode, methodDecl.getStartPosition());
		}
		return super.visit(methodDecl);
	}

	private GNode insertMethodNode(MethodDeclaration methodDecl) {
		IMethodBinding rBinding = methodDecl.resolveBinding();
		ITypeBinding typeBinding = rBinding.getDeclaringClass();
		String prjName = typeBinding.getPackage().getJavaElement().getJavaProject().getElementName();
		String pkgName = typeBinding.getPackage().getName();
		String className = typeBinding.getName();

		String methodName = methodDecl.getName().getFullyQualifiedName();
		String parent = prjName + "." + pkgName + "." + className;
		String id = parent + "." + methodName;
		GMethodNode n = new GMethodNode(id, methodName, parent);
		n.setPrjName(prjName).setPkgName(pkgName).setClassName(className);
		return addNode(n);
	}
	
	@Override
	public boolean visit(VariableDeclarationFragment node) {
		GNode methodNode = (GNode) insertVariableNode(node);
		GNode typeNode = GModelProvider.instance().getNodeMap().get(methodNode.getParent());
		if (typeNode == null) {
			return false;		
		}
		addConnection(typeNode, methodNode, node.getStartPosition());
		return super.visit(node);
	}
	
	private GNode insertVariableNode(VariableDeclarationFragment vDecl) {
		IVariableBinding binding = vDecl.resolveBinding();
		ITypeBinding typeBinding = vDecl.resolveBinding().getType();
		String prjName = typeBinding.getPackage().getJavaElement().getJavaProject().getElementName();
		String pkgName = binding.getDeclaringMethod().getDeclaringClass().getPackage().getName();
		String className = binding.getDeclaringMethod().getDeclaringClass().getName();
		//String methodName = typeBinding.getElementType().getName();
		String methodName = binding.getDeclaringMethod().getName();//mb1
		//String m2 = binding.getDeclaringClass().getName();
		String m3 = binding.getJavaElement().getElementName();//m1
		String m4 = binding.getVariableDeclaration().getName();//m1

		String vName = vDecl.getName().getFullyQualifiedName();
		String parent = prjName + "." + pkgName + "." + className + "." + methodName ;
		String id = parent + "." + vName;
		GNode n = new GNode(id, vName, parent);

		//n.setPrjName(prjName).setPkgName(pkgName).setClassName(className).setMethodName(methodName);
		return addNode(n);
	}


	private void addConnection(GNode srcNode, GNode dstNode, int offset) {
		String conId = srcNode.getId() + dstNode.getId();
		String conLabel = "offset: " + offset;
		GConnection con = new GConnection(conId, conLabel, srcNode, dstNode);
		GModelProvider.instance().getConnections().add(con);
		srcNode.getConnectedTo().add(dstNode);
	}

	private GNode addNode(GNode n) {
		GModelProvider.instance().getNodes().add(n);
		GModelProvider.instance().getNodeMap().put(n.getId(), n);
		return n;
	}
}
