package detectors;

/*
 * Class stores the different pieces of information about a useless control flow or a recursion into a 
 * single object.
 * 
 * @author Declan McBride 2399448
 */

public class Breakpoints {
	
	private String className;
	private String methodName;
	private int startLine;
	private int endLine;
	
	public Breakpoints(String className, String methodName, int startLine, int endLine) {
		this.className = className;
		this.methodName = methodName;
		this.startLine = startLine;
		this.endLine = endLine;
	}

	@Override
	public String toString() {
		return "className=" + className + ",methodName=" + methodName + ",startline=" + startLine
				+ ",endline=" + endLine;
	}
	
	
	
}
