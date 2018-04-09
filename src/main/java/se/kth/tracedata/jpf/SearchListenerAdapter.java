package se.kth.tracedata.jpf;

import gov.nasa.jpf.search.Search;
//import gov.nasa.jpf.search.SearchListener;
import se.kth.tracedata.SearchListener;

/**
 * a no-action SearchListener which we can use to override only the
 * notifications we are interested in
 */
public class SearchListenerAdapter implements SearchListener {

  @Override
  public void stateAdvanced(Search search) {}

  @Override
  public void stateProcessed(Search search) {}

  @Override
  public void stateBacktracked(Search search) {}

  @Override
  public void statePurged(Search search) {}

  @Override
  public void stateStored(Search search) {}

  @Override
  public void stateRestored(Search search) {}

  @Override
  public void searchProbed(Search search) {}
  
  @Override
  public void propertyViolated(Search search) {}

  @Override
  public void searchStarted(Search search) {}

  @Override
  public void searchConstraintHit(Search search) {}

  @Override
  public void searchFinished(Search search) {}

}
