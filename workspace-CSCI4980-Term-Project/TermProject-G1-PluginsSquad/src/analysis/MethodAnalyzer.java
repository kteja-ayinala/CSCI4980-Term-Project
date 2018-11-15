package analysis;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import graph.model.GMethodNode;
import graph.provider.GModelProvider;
import visitor.VariableVisitor;

public class MethodAnalyzer {
	private static final String JAVANATURE = "org.eclipse.jdt.core.javanature";
	protected String prjName, pkgName;

	private GMethodNode method;

	public MethodAnalyzer() {
		// this.method = method;

	}

	public void analyze() {
		GModelProvider.instance().reset();

		// =============================================================
		// 1st step: Project
		// =============================================================
		try {
			IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject project : projects) {

				if (!project.isOpen() || !project.isNatureEnabled(JAVANATURE)) { // Check
																					// if
																					// we
																					// have
																					// a
																					// Java
																					// project.
					continue;
				}
				prjName = project.getName();
				if (prjName.equals(method.getPrjName())) {
					analyzePackages(JavaCore.create(project).getPackageFragments());
					// exit loop
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
				if (pkgName.equals(method.getPkgName())) {
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
			CompilationUnit compilationUnit = parse(iUnit);
			if (iUnit.getElementName().equals(method.getClassName() + ".java")) {

				VariableVisitor declVisitor = new VariableVisitor(method);
				compilationUnit.accept(declVisitor);
			};

		}

	}

	public GMethodNode getMethod() {
		return method;
	}

	public void setMethod(GMethodNode method) {
		this.method = method;
	}

	private static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
}