package se.kth.tracedata;

//import gov.nasa.jpf.JPFListener;
import se.kth.tracedata.JPFListener;
import gov.nasa.jpf.search.Search;

/**
 * interface to register for notification by the Search object.
 * Observer role in same-name pattern
 */
public interface SearchListener extends JPFListener {
  
  /**
   * got the next state
   * Note - this will be notified before any potential propertyViolated, in which
   * case the currentError will be already set
   */
  void stateAdvanced (Search search);
  
  /**
   * state is fully explored
   */
  void stateProcessed (Search search);
  
  /**
   * state was backtracked one step
   */
  void stateBacktracked (Search search);

  /**
   * some state is not going to appear in any path anymore
   */
  void statePurged (Search search);

  /**
   * somebody stored the state
   */
  void stateStored (Search search);
  
  /**
   * a previously generated state was restored
   * (can be on a completely different path)
   */
  void stateRestored (Search search);
  
  /**
   * there was a probe request, e.g. from a periodical timer
   * note this is called synchronously from within the JPF execution loop
   * (after instruction execution)
   */
  void searchProbed (Search search);
  
  /**
   * JPF encountered a property violation.
   * Note - this is always preceeded by a stateAdvanced
   */
  void propertyViolated (Search search);
  
  /**
   * we get this after we enter the search loop, but BEFORE the first forward
   */
  void searchStarted (Search search);
  
  /**
   * there was some contraint hit in the search, we back out
   * could have been turned into a property, but usually is an attribute of
   * the search, not the application
   */
  void searchConstraintHit (Search search);
  
  /**
   * we're done, either with or without a preceeding error
   */
  void searchFinished (Search search);
}

