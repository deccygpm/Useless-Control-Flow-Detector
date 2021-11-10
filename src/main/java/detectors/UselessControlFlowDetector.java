package detectors;

import java.util.List;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.LiteralExpr;

/**
 * This class detects useless control flows in Java code using JavaParser by providing Visitors to check what happens following a control flow
 * statement. 
 * 
 * @author Declan McBride - 2399448
 * 
 * @issues There is a lot of repetition in this code, particularly for gathering information to create new Breakpoints. This repetition
 * could have perhaps been refactored into a method to make the code DRYer, but unfortunately time did not permit this. Where repetition occurs
 * it is explained once in MethodCollector class. 
 */

public class UselessControlFlowDetector {
	
	/*
	 * This class detects empty methods. While not necessarily a control flow, an empty method is included in the Calculator.java file we were 
	 * provided for testing. For that reason I have included this method. 
	 */
	public static class MethodCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(MethodDeclaration md, List<Breakpoints> breakpoints) {
			super.visit(md, breakpoints);
			List<Node> kids = md.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt) {
					if (n.getChildNodes().isEmpty()) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString(); //Get the class name
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString(); // Get the method name
						int startLine = lineCleaner(md.getBegin().get().toString()); //Get the line number for the start of this block of code			
						int endLine = lineCleaner(md.getEnd().get().toString()); //Get the line number for the end of this block of code			
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine); //Create new Breakpoint
						breakpoints.add(b);//Add new Breakpoint to list of useless control flows. 
					}
				}
			}
		}
	}
	
	/*
	 * This class checks if-else statements for useless control flow.
	 */
	public static class IfCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(IfStmt is, List<Breakpoints> breakpoints) {
			super.visit(is, breakpoints);
			List<Node> kids = is.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt){
					//If an if statement has no children, or the only child is a comment it is considered useless control flow. 
					if (n.getChildNodes().isEmpty() || (n.getChildNodes().size() == 1 && n.getChildNodes().get(0) instanceof Comment)) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
						int startLine = lineCleaner(is.getBegin().get().toString());					
						int endLine = lineCleaner(is.getEnd().get().toString());					
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
						breakpoints.add(b);
					}
				}
			}
		}
	}
	
	/*
	 * This class checks for loops for useless control flow. 
	 */
	public static class ForCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(ForStmt fs, List<Breakpoints> breakpoints) {
			super.visit(fs, breakpoints);
			List<Node> kids = fs.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt) {
					//If a for loop has no children it is useless control flow. 
					if (n.getChildNodes().isEmpty()) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
						int startLine = lineCleaner(fs.getBegin().get().toString());					
						int endLine = lineCleaner(fs.getEnd().get().toString());					
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
						breakpoints.add(b);
					}
				}
			}
		}
	}
	
	/* 
	 * This class checks for each loops for useless control flow.
	 */
	public static class ForEachCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(ForEachStmt fes, List<Breakpoints> breakpoints) {
			super.visit(fes, breakpoints);
			List<Node> kids = fes.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt) {
					//If a for statement has no children it is a useless control flow
					if (n.getChildNodes().isEmpty()) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
						int startLine = lineCleaner(fes.getBegin().get().toString());					
						int endLine = lineCleaner(fes.getEnd().get().toString());					
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
						breakpoints.add(b);
					}
				}
			}
		}
	}
	
	/*
	 * This class checks while loops for useless control flow.
	 */
	public static class WhileCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(WhileStmt ws, List<Breakpoints> breakpoints) {
			super.visit(ws, breakpoints);
			List<Node> kids = ws.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt) {
					//If a while loop is empty it is a useless control flow.
					if (n.getChildNodes().isEmpty()) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
						int startLine = lineCleaner(ws.getBegin().get().toString());					
						int endLine = lineCleaner(ws.getEnd().get().toString());					
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
						breakpoints.add(b);
					}
				}
			}
		}
	}
	
	/* 
	 * This class checks swtich statements for useless control flow. 
	 */
	public static class SwitchCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(SwitchStmt ss, List<Breakpoints> breakpoints) {
			super.visit(ss, breakpoints);
			List<Node> kids = ss.getChildNodes();
			//Checks each part of the swithc statement for useless control flows
			for(Node n: kids) {
				if (n instanceof SwitchEntry) {
					//If the only child of the entry is a LiteralExpr then this is a useless control flow
					if(n.getChildNodes().size() == 1 && n.getChildNodes().get(0) instanceof LiteralExpr) {
							String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
							String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
							//Decision taken to provide the lines of the useless statement within a larger switch statement. 
							//Alternate approach declared the entire switch statement useless if any singular part of it was useless.
							//This seemed like overkill.
							int startLine = lineCleaner(n.getBegin().get().toString());					
							int endLine = lineCleaner(n.getEnd().get().toString());					
							Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
							breakpoints.add(b);
					}
				}
			}
		}
	}
	
	/*
	 * This class checks do while loops for useless control.
	 */
	public static class DoCollector extends VoidVisitorAdapter<List<Breakpoints>> {
		
		@Override
		public void visit(DoStmt ds, List<Breakpoints> breakpoints) {
			super.visit(ds, breakpoints);
			List<Node> kids = ds.getChildNodes();
			for(Node n: kids) {
				if (n instanceof BlockStmt) {
					//If a do statement has no children it is useless control flow
					if (n.getChildNodes().isEmpty()) {
						String cn = n.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
						String mn = n.findAncestor(MethodDeclaration.class).get().getNameAsString();
						int startLine = lineCleaner(ds.getBegin().get().toString());					
						int endLine = lineCleaner(ds.getEnd().get().toString());					
						Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
						breakpoints.add(b);
					}
				}
			}	
		}
	}
	
	
	/**
	 * A method to strip line number strings of all letters and return an integer based on the numeric digits that were in the string
	 * @param line  The line number string to be stripped
	 * @return The line number as an integer
	 */
	public static int lineCleaner(String line) {
		String[] lines = line.split(",");
		String ln = lines[0].replaceAll("[^0-9]","");
		int lineno = Integer.parseInt(ln);
		return lineno;
	}
}
	