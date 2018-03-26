package se.kth.tracedata;



public class ThreadData {
	
	  /** the scheduler priority of this thread */
	  int priority;

	 /**
	   * Current state of the thread.
	   */
	  ThreadInfo.State state;
	  /** is this a daemon thread */
	  boolean isDaemon;
	  /**
	   * The lock counter when the object got into a wait. This value
	   * is used to restore the object lock count once this thread
	   * gets notified
	   */
	  int lockCount;
	  
	  /**
	   * The suspend count of the thread. See ThreadInfo.suspend() for a discussion
	   * of how faithful this is (it is an over approximation)
	   */
	  int suspendCount;
	/**
	   * the name of this thread
	   * (only temporarily unset, between NEW and INVOKESPECIAL)
	   */
	  String name = "?";
	  public ThreadData clone () {
		    ThreadData t = new ThreadData();
		    t.name = name;
		    return t;

	  }
	  public ThreadInfo.State getState() { return state; }
	  public String getFieldValues () {
		    StringBuilder sb = new StringBuilder("name:");

		    sb.append(name);
		    sb.append(",status:");
		    sb.append(state.name());
		    sb.append(",priority:");
		    sb.append(priority);
		    sb.append(",isDaemon:");
		    sb.append(isDaemon);
		    sb.append(",lockCount:");
		    sb.append(lockCount);
		    sb.append(",suspendCount:");
		    sb.append(suspendCount);

		    return sb.toString();
		  }
}
