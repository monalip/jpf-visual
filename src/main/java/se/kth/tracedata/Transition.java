package se.kth.tracedata;

import java.util.Iterator;

//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;
//import gov.nasa.jpf.vm.Transition;
//import gov.nasa.jpf.vm.ChoiceGenerator;
import se.kth.tracedata.ChoiceGenerator;

//import gov.nasa.jpf.vm.Transition;


//import gov.nasa.jpf.vm.Step;

//import gov.nasa.jpf.vm.Step;
import se.kth.tracedata.Step;

public class Transition implements Iterable<Step>, Cloneable {
	  private Step   first, last;
	  Step next;
	  int nSteps;
	  ThreadInfo ti;
	  String         output;
	  ChoiceGenerator<?> cg;


	@Override
	public Iterator<Step> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Step getStep (int index) {
	    Step s = first;
	    for (int i=0; s != null && i < index; i++) s = s.next;
	    return s;
	  }
	public int getStepCount () {
	    return nSteps;
	  }
	public int getThreadIndex () {
	    return ti.getId();
	  }
	public ThreadInfo getThreadInfo() {
	    return ti;
	  }
	public String getOutput () {
	    return output;
	  }
	public ChoiceGenerator<?> getChoiceGenerator() {
	    return cg;
	  }
	public Object clone() {
	    try {
	      Transition t = (Transition)super.clone();
	      
	      // the deep copy references
	      t.cg = cg.clone();
	      t.ti = (ThreadInfo)ti.clone();
	      
	      return t;
	      
	    } catch (CloneNotSupportedException cnsx){
	      return null; // cannot happen
	    } 
	  }


	

}