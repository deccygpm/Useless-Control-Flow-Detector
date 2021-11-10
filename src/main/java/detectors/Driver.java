package detectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import detectors.UselessControlFlowDetector.DoCollector;
import detectors.UselessControlFlowDetector.ForCollector;
import detectors.UselessControlFlowDetector.ForEachCollector;
import detectors.UselessControlFlowDetector.IfCollector;
import detectors.UselessControlFlowDetector.MethodCollector;
import detectors.UselessControlFlowDetector.SwitchCollector;
import detectors.UselessControlFlowDetector.WhileCollector;

/*
 * This class contains the main method.
 * Takes a file path as argument, parses that file and runs the various 
 * useless control flow detectors and the recursion detector on that file. 
 * Prints results to console.
 * 
 * @author Declan McBride 2399448
 * 
 */
public class Driver {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		try {
			String FILE_PATH = args[0];
		
		//Parse the file with JavaParser
		CompilationUnit cu = JavaParser.parse(new File(FILE_PATH));
		
		//Initialise ArrayList to store useless control flows.
		List<Breakpoints> uselessControlFlows = new ArrayList<>();
		
		//Create various detectors and store results in uselessControlFlows.
		IfCollector ifCollector = new IfCollector();
		ifCollector.visit(cu, uselessControlFlows);
		ForCollector forCollector = new ForCollector();
		forCollector.visit(cu, uselessControlFlows);
		ForEachCollector forEachCollector = new ForEachCollector();
		forEachCollector.visit(cu, uselessControlFlows);
		WhileCollector whileCollector = new WhileCollector();
		whileCollector.visit(cu, uselessControlFlows);
		SwitchCollector switchCollector = new SwitchCollector();
		switchCollector.visit(cu, uselessControlFlows);
		DoCollector doCollector = new DoCollector();
		doCollector.visit(cu, uselessControlFlows);
		MethodCollector methodCollector = new MethodCollector();
		methodCollector.visit(cu, uselessControlFlows);
		
		//Print useless control flows.
		System.out.println("Useless Control Flows:");
		for (Breakpoints b: uselessControlFlows) {
			System.out.println(b.toString());
		}
		
		//Initialise ArrayList to store found recursions.
		List<Breakpoints> recursions = new ArrayList<>();
		
		//Generate recursion detector and store results in recursions.
		RecursionDetector recursionDetector = new RecursionDetector();
		recursionDetector.visit(cu, recursions);
		
		//Print found recursions. 
		System.out.println("\nRecursions:");
		for(Breakpoints b: recursions) {
			System.out.println(b.toString());
		}
		
		} catch (FileNotFoundException e) {
			System.out.println("This file could not be found. Try again.");
		} 	
	}	
}
