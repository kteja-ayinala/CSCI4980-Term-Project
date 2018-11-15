package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import graph.model.GConnection;
import graph.model.GMethodNode;
import graph.model.GNode;
import graph.model.GVariableNode;
import graph.provider.GModelProvider;

public class VariableVisitor extends ASTVisitor {
	GMethodNode methodNode;

	public VariableVisitor(GMethodNode method) {
		this.methodNode = method;
	}

	@Override
	public boolean visit(MethodDeclaration md) {
		if (md.getName().toString().equals(methodNode.getName())) {
			md.accept(new ASTVisitor() {
				public boolean visit(VariableDeclarationFragment vDecl) {

					String prjName = methodNode.getPrjName();
					String pkgName = methodNode.getPkgName();
					String clsName = methodNode.getClassName();
					String methodName = methodNode.getName();
					String vName = vDecl.getName().getFullyQualifiedName();
					String id = prjName + "." + pkgName + "." + clsName + "." + methodName + "." + vName;
					GVariableNode vNode = new GVariableNode(id, vDecl.getParent().toString(),
							prjName + "." + pkgName + "." + clsName + "." + methodName);
					addNode(vNode);
					addNode(methodNode);
					addConnection(methodNode, vNode, vDecl.getStartPosition());
					return false;
				}
			});
		}
		return false;
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
