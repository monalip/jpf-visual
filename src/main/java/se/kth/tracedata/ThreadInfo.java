package se.kth.tracedata;

//import gov.nasa.jpf.vm.ElementInfo;
import se.kth.tracedata.jpf.ElementInfo;

public interface ThreadInfo {
	public int getId ();
	public ElementInfo getElementInfo (int objRef);
	public String getStateName () ;
	public String getName ();

}
