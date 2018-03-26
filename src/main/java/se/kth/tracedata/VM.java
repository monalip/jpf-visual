package se.kth.tracedata;
import java.io.PrintWriter;
import java.util.List;

//import gov.nasa.jpf.JPF;
import se.kth.tracedata.JPF;
//import gov.nasa.jpf.util.JPFLogger;
import se.kth.tracedata.JPFLogger;
//import gov.nasa.jpf.util.Misc;
import se.kth.tracedata.Misc;
//import gov.nasa.jpf.vm.ElementInfo;
import se.kth.tracedata.ElementInfo;
//import gov.nasa.jpf.vm.Heap;
import se.kth.tracedata.Heap;
//import gov.nasa.jpf.vm.KernelState;
import se.kth.tracedata.KernelState;
import gov.nasa.jpf.vm.StackFrame;
//import gov.nasa.jpf.vm.SystemState;
import se.kth.tracedata.SystemState;
//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;
//import gov.nasa.jpf.vm.ThreadList;
import se.kth.tracedata.ThreadList;
//import gov.nasa.jpf.vm.VMListener;
import se.kth.tracedata.VMListener;
import se.kth.tracedata.jpf.Path;

public abstract class VM {
	 protected SystemState ss;
	 protected static JPFLogger log = JPF.getLogger("vm");
	protected Path path;  /** execution path to current state */
	/**
	   * use that one if you have to store the path for subsequent use
	   *
	   * NOTE: without a prior call to updatePath(), this does NOT contain the
	   * ongoing transition. See getPath() for usage from a VMListener
	   */
	
	/** potential execution listeners. We keep them in a simple array to avoid
	   creating objects on each notification */
	  protected VMListener[] listeners = new VMListener[0];
	  public Path getClonedPath () {
	    return path.clone();
	  }
	  public abstract String getSUTDescription();
	  public Heap getHeap() {
		    return ss.getHeap();
		  }
	  public int getPathLength () {
		    return path.size();
		  }
	  /**
	   * print call stacks of all live threads
	   * this is also used for debugging purposes, so we can't move it to the Reporter system
	   * (it's also using a bit too many internals for that)
	   */
	  public void printLiveThreadStatus (PrintWriter pw) {
	    int nThreads = ss.getThreadCount();
	    ThreadInfo[] threads = getThreadList().getThreads();
	    int n=0;

	    for (int i = 0; i < nThreads; i++) {
	      ThreadInfo ti = threads[i];

	      if (ti.getStackDepth() > 0){
	        n++;
	        //pw.print("Thread: ");
	        //pw.print(tiMain.getName());
	        pw.println(ti.getStateDescription());

	        List<ElementInfo> locks = ti.getLockedObjects();
	        if (!locks.isEmpty()) {
	          pw.print("  owned locks:");
	          boolean first = true;
	          for (ElementInfo e : locks) {
	            if (first) {
	              first = false;
	            } else {
	              pw.print(",");
	            }
	            pw.print(e);
	          }
	          pw.println();
	        }

	        ElementInfo ei = ti.getLockObject();
	        if (ei != null) {
	          if (ti.getState() == ThreadInfo.State.WAITING) {
	            pw.print( "  waiting on: ");
	          } else {
	            pw.print( "  blocked on: ");
	          }
	          pw.println(ei);
	        }

	        pw.println("  call stack:");
	        for (StackFrame frame : ti){
	          if (!frame.isDirectCallFrame()) {
	            pw.print("\tat ");
	            pw.println(frame.getStackTraceInfo());
	          }
	        }

	        pw.println();
	      }
	    }

	    if (n==0) {
	      pw.println("no live threads");
	    }
	  }
	  public ThreadList getThreadList () {
		    return getKernelState().getThreadList();
		  }
	  public KernelState getKernelState () {
		    return ss.getKernelState();
		  }
	  
	  public <T> T getNextListenerOfType(Class<T> type, T prev){
		    return Misc.getNextElementOfType(listeners, type, prev);
		  }
	  public ElementInfo getElementInfo(int objref){
		    return ss.getHeap().get(objref);
		  }
	  public void addListener (VMListener newListener) {
		    log.info("VMListener added: ", newListener);
		    listeners = Misc.appendElement(listeners, newListener);
		  }


	  

}
