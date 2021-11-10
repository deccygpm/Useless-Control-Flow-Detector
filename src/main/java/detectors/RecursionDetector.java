package detectors;

import java.util.List;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/*
 * This class overrides visit method in VoidVisitorAdapter to detect recursion in a java file.
 * 
 * @author Declan McBride 2399448
 * 
 */
public class RecursionDetector extends VoidVisitorAdapter<List<Breakpoints>> {
	
	@Override
	public void visit(MethodDeclaration md, List<Breakpoints> breakpoints) {
		super.visit(md, breakpoints);
		//Find all methods in a java file
		for(MethodDeclaration m:md.findAll(MethodDeclaration.class)) {
			//Find all method calls within found methods 
			for(MethodCallExpr c : m.findAll(MethodCallExpr.class)) {
				//Get method name and called method name
				String methodname = m.getNameAsString();
				String callname = c.getNameAsString();
				//If the method call is the same as the method we are currently using generate a Breakpoint
				if(methodname.equals(callname)) {
					String cn = c.findAncestor(ClassOrInterfaceDeclaration.class).get().getNameAsString();
					String mn = c.findAncestor(MethodDeclaration.class).get().getNameAsString();
					int startLine = lineCleaner(c.getBegin().get().toString());					
					int endLine = lineCleaner(c.getEnd().get().toString());		
					Breakpoints b = new Breakpoints(cn, mn, startLine, endLine);
					breakpoints.add(b);
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
