package util;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.PlatformUI;

public class UtilPlatform {

   public static void indentAndSave(ICompilationUnit cu) {
      CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
      ISourceRange range;
      try {
         range = cu.getSourceRange();
         TextEdit indent_edit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, //
               cu.getSource(), range.getOffset(), range.getLength(), 0, null);
         cu.applyTextEdit(indent_edit, null);
         cu.reconcile(ICompilationUnit.NO_AST, ICompilationUnit.FORCE_PROBLEM_DETECTION, null, null);
      } catch (JavaModelException e) {
         e.printStackTrace();
      }
      PlatformUI.getWorkbench().saveAllEditors(false);
   }
}
