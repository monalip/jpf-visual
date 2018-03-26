package se.kth.tracedata;


//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;

public class ChoiceGenerator<T> {
	 // want the id to be visible to subclasses outside package
	  protected String id;
	  protected ThreadInfo[] values;
	public String getId() {
	    return id;
	  }
	  public int getTotalNumberOfChoices () {
		    return values.length;
		  }
	  public ChoiceGenerator<T> clone() throws CloneNotSupportedException {
		    return (ChoiceGenerator<T>)super.clone();
		  }


}
