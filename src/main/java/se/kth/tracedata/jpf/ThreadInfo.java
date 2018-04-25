package se.kth.tracedata.jpf;

//import gov.nasa.jpf.vm.ElementInfo;
import se.kth.tracedata.jpf.ElementInfo;

public class ThreadInfo implements se.kth.tracedata.ThreadInfo{
	gov.nasa.jpf.vm.ThreadInfo  jpfThreadinfo;
	  public ThreadInfo(gov.nasa.jpf.vm.ThreadInfo  jpfThreadinfo) {
		 this.jpfThreadinfo = jpfThreadinfo;
		 
		  }
	@Override
	public int getId() {
		return jpfThreadinfo.getId();
	}
	@Override
	public ElementInfo getElementInfo(int objRef) {
		return new ElementInfo(jpfThreadinfo.getElementInfo(objRef));
	}
	@Override
	public String getStateName() {
		return jpfThreadinfo.getStateName();
	}
	@Override
	public String getName() {
		return jpfThreadinfo.getName();
	}

}
