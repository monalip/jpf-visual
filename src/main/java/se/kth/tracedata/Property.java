package se.kth.tracedata;



import java.io.PrintWriter;

//import gov.nasa.jpf.search.Search;

//import gov.nasa.jpf.vm.VM;
import se.kth.tracedata.VM;  

/**
 * abstraction that is used by Search objects to determine if program
 * properties have been violated (e.g. NoUncaughtExceptions)
 */
public interface Property {

	/**
	   * return true if property is NOT violated
	   */
	  boolean check (Search search, VM vm);

	  String getErrorMessage ();
	  
	  String getExplanation();
	  
	  void reset (); // required for search.multiple_errors
	  
	  Property clone() throws CloneNotSupportedException; // so that we can store multiple errors
	  void printOn (PrintWriter ps);
	}
