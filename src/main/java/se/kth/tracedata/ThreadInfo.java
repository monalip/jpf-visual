package se.kth.tracedata;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.StackFrame;
//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;
//import gov.nasa.jpf.vm.ElementInfo;
import se.kth.tracedata.ElementInfo;
//import gov.nasa.jpf.vm.Heap;
import se.kth.tracedata.Heap;

//import gov.nasa.jpf.vm.ThreadData; // add clone method to the treaddata
import se.kth.tracedata.ThreadData;

public class ThreadInfo  implements Iterable<StackFrame>, Comparable<ThreadInfo> {
	/**
	   * !! this is also volatile -> has to be reset after backtrack
	   * the reference of the object if this thread is blocked or waiting for
	   */
	  int lockRef = MJIEnv.NULL;
	
	//--- our internal thread states
	  public enum State {
	    NEW,  // means created but not yet started
	    RUNNING,
	    BLOCKED,  // waiting to acquire a lock
	    UNBLOCKED,  // was BLOCKED but can acquire the lock now
	    WAITING,  // waiting to be notified
	    TIMEOUT_WAITING,
	    NOTIFIED,  // was WAITING and got notified, but is still blocked
	    INTERRUPTED,  // was WAITING and got interrupted
	    TIMEDOUT,  // was TIMEOUT_WAITING and timed out
	    TERMINATED,
	    SLEEPING
	  };
	  /**
	   * !! this is a volatile object, i.e. it has to be re-computed explicitly
	   * !! after each backtrack (we don't want to duplicate state storage)
	   * list of lock objects currently held by this thread.
	   * unfortunately, we cannot organize this as a stack, since it might get
	   * restored (from the heap) in random order
	   */
	  int[] lockedObjectReferences;
	  protected int objRef; // the java.lang.Thread object reference
	  // the current stack depth (number of frames)
	  protected int stackDepth;
	
	 // search global id, which is the basis for canonical order of threads
	  protected int id;
	  
	  // state managed data that is copy-on-first-write
	  protected ThreadData threadData;
	  /**
	   * the VM we are running on. Bad backlink, but then again, we can't really
	   * test a ThreadInfo outside a VM context anyways.
	   * <2do> If we keep 'list' as a field, 'vm' might be moved over there
	   * (all threads in the list share the same VM)
	   */
	  VM vm;
	 
	  /**
	   * path local unique id for live threads. This is what we use for the
	   * public java.lang.Thread.getId() that can be called from SUT code. It is
	   * NOT what we use for our canonical root set
	   */
	  public int getId () {
	    return id;
	  }
	  public Object clone() {
		    try {
		      // threadData and top StackFrame are copy-on-write, so we should not have to clone them
		      // lockedObjects are state-volatile and restored explicitly after a backtrack
		      return super.clone();

		    } catch (CloneNotSupportedException cnsx) {
		      return null;
		    }
		  }
	  public String getName () {
		    return threadData.name;
		  }
	  public ElementInfo getElementInfo (int objRef) {
		    Heap heap = vm.getHeap();
		    return heap.get(objRef);
		  }
	  public String getStateName () {
		    return threadData.getState().name();
		  }
	  
	  public int getStackDepth() {
		    return stackDepth;
		  }
	  
	  public String getStateDescription () {
		    StringBuilder sb = new StringBuilder("thread ");
		    sb.append(getThreadObjectClassInfo().getName());
		    sb.append(":{id:");
		    sb.append(id);
		    sb.append(',');
		    sb.append(threadData.getFieldValues());
		    sb.append('}');
		    
		    return sb.toString();
		  }
	  
	  public ClassInfo getThreadObjectClassInfo() {
		    return getThreadObject().getClassInfo();
		  }
	  public ElementInfo getThreadObject(){
		    return getElementInfo(objRef);
		  }
	  
	// avoid use in performance critical code
	  public List<ElementInfo> getLockedObjects () {
	    List<ElementInfo> lockedObjects = new LinkedList<ElementInfo>();
	    Heap heap = vm.getHeap();
	    
	    for (int i=0; i<lockedObjectReferences.length; i++) {
	      ElementInfo ei = heap.get(lockedObjectReferences[i]);
	      lockedObjects.add(ei);
	    }
	    
	    return lockedObjects;
	  }
	  /**
	   * Comparison for sorting based on index.
	   */
	  @Override
	  public int compareTo (ThreadInfo that) {
	    return this.id - that.id;
	  }
	@Override
	public Iterator<StackFrame> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	public ElementInfo getLockObject () {
	    if (lockRef == MJIEnv.NULL) {
	      return null;
	    } else {
	      return vm.getElementInfo(lockRef);
	    }
	  }
	/**
	   * Returns the current status of the thread.
	   */
	  public State getState () {
	    return threadData.state;
	  }
	  

	 
	

		  
	  

}
