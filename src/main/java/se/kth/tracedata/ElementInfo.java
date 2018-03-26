package se.kth.tracedata;

//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;

public abstract class ElementInfo implements Cloneable {
	 //--- instance fields

	  protected ClassInfo       ci;
	 public ClassInfo getClassInfo() {
		    return ci;
		  }

}
