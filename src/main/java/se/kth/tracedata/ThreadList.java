package se.kth.tracedata;

//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;

public class ThreadList {
	// ThreadInfos for all created but not terminated threads
	  protected ThreadInfo[] threads;

	/**
	   * Returns the array of threads.
	   */
	  public ThreadInfo[] getThreads() {
	    return threads.clone();
	  }
	  /**
	   * Returns the length of the list.
	   */
	  public int length () {
	    return threads.length;
	  }
}
