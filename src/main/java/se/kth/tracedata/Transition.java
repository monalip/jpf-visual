package se.kth.tracedata;

import gov.nasa.jpf.vm.ChoiceGenerator;
//import gov.nasa.jpf.vm.Step;
import se.kth.tracedata.Step;
//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;

public interface Transition {
	public int getThreadIndex ();
	public ThreadInfo getThreadInfo();
	public ChoiceGenerator<?> getChoiceGenerator();
	// don't use this for step iteration - this is very inefficient
	  public Step getStep (int index) ;
	  public int getStepCount ();

}
