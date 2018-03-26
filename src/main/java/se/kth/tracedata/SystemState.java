package se.kth.tracedata;

//import gov.nasa.jpf.vm.KernelState;
import se.kth.tracedata.KernelState;
//import gov.nasa.jpf.vm.Heap;
import se.kth.tracedata.Heap;

public class SystemState {
	/** current execution state of the VM (stored separately by VM) */
	  public KernelState ks;
	public Heap getHeap() {
	    return ks.getHeap();
	  }
	 public int getThreadCount () {
		    return ks.threads.length();
		  }
	 public KernelState getKernelState() {
		    return ks;
		  }

}
