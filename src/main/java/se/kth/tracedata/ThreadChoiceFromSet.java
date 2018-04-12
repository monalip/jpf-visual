package se.kth.tracedata;

//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.jpf.ThreadInfo;

public interface ThreadChoiceFromSet {
	 public ThreadInfo getChoice (int idx);

}
