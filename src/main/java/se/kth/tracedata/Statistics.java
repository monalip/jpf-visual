package se.kth.tracedata;

/**
 * simple structure to hold statistics info created by Reporters/Publishers
 * this is kind of a second tier SearchListener, which does not
 * explicitly have to be registered
 * 
 * <2do> this should get generic and accessible enough to replace all the
 * other statistics collectors, otherwise there is too much redundancy.
 * If users have special requirements, they should subclass Statistics
 * and set jpf.report.statistics.class accordingly
 * 
 * Note that Statistics might be accessed by a background thread
 * reporting JPF progress, hence we have to synchronize
 */
public class Statistics extends ListenerAdapter implements Cloneable {
	// we make these public since we don't want to add a gazillion of
	  // getters for these purely informal numbers
	  
	  public long maxUsed = 0;
	  public long newStates = 0;
	  public long backtracked = 0;
	  public long restored = 0;
	  public int processed = 0;
	  public int constraints = 0;
	  public long visitedStates = 0;
	  public long endStates = 0;
	  public int maxDepth = 0;
	  
	  public int gcCycles = 0;
	  public long insns = 0;
	  public int threadCGs = 0;
	  public int sharedAccessCGs = 0;
	  public int monitorCGs = 0;
	  public int signalCGs = 0;
	  public int threadApiCGs = 0;
	  public int breakTransitionCGs = 0;
	  public int dataCGs = 0;
	  public long nNewObjects = 0;
	  public long nReleasedObjects = 0;
	  public int maxLiveObjects = 0;

	  @Override
	  public Statistics clone() {
	    try {
	      return (Statistics)super.clone();
	    } catch (CloneNotSupportedException e) {
	      return null; // can't happen
	    }
	  }
    
  

}
