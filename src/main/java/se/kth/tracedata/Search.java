package se.kth.tracedata;

import java.util.ArrayList;
import java.util.List;

//import gov.nasa.jpf.report.Reporter;
import se.kth.tracedata.Reporter;
//import gov.nasa.jpf.JPF;
import se.kth.tracedata.JPF;
//import gov.nasa.jpf.search.SearchListener;
import se.kth.tracedata.SearchListener;
//import gov.nasa.jpf.util.JPFLogger;
import se.kth.tracedata.JPFLogger;
//import gov.nasa.jpf.util.Misc;
import se.kth.tracedata.Misc;

/**
 * the mother of all search classes. Mostly takes care of listeners, keeping
 * track of state attributes and errors. This class mainly keeps the
 * general search info like depth, configured properties etc.
 */
public abstract class Search {
	 protected static JPFLogger log = JPF.getLogger("se.kth.tracedata.Search");
	 
	 /** this is a special SearchListener that is always notified last, so that
	   * PublisherExtensions can be sure the notification has been processed by all listeners */
	  protected Reporter reporter;

	// these states control the search loop
	  protected boolean done = false;

	  /** search listeners. We keep them in a simple array to avoid
	   creating objects on each notification */
	  protected SearchListener[] listeners = new SearchListener[0];
	
	protected ArrayList<Error> errors = new ArrayList<Error>();
	/** error encountered during last transition, null otherwise */
	  protected Error currentError = null;
	// message explaining the last search constraint hit
	  protected String lastSearchConstraint;

	
	public String getLastSearchConstraint() {
	    return lastSearchConstraint;
	  }
	/**
	   * @return error encountered during *last* transition (null otherwise)
	   */
	  public Error getCurrentError(){
	    return currentError;
	  }
	  public List<Error> getErrors () {
		    return errors;
		  }
	  public void addListener (SearchListener newListener) {
		    log.info("SearchListener added: ", newListener);
		    listeners = Misc.appendElement(listeners, newListener);
		  }
	  /** this can be used by listeners to terminate the search */
	  public void terminate () {
	    done = true;
	  }
	  public void setReporter(Reporter reporter){
		    this.reporter = reporter;
		  }
	 



}
