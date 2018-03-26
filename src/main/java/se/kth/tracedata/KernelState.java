package se.kth.tracedata;

//import gov.nasa.jpf.vm.ThreadList;
import se.kth.tracedata.ThreadList;
//import gov.nasa.jpf.vm.Heap;
import se.kth.tracedata.Heap;

public class KernelState {
	  /** The list of the threads */
	  public ThreadList threads;
	  /** The area containing the heap */
	  public Heap heap;
	public Heap getHeap() {
	    return heap;
	  }
	public ThreadList getThreadList() {
	    return threads;
	  }

}
