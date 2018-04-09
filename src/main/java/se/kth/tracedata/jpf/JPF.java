package se.kth.tracedata.jpf;

import se.kth.tracedata.jpf.Reporter;

public class JPF {
	gov.nasa.jpf.JPF jpf ;
	public JPF (gov.nasa.jpf.JPF jpf) {
	  	
	    this.jpf = jpf; 
	  }
	 public Reporter getReporter () {
		    return new Reporter(jpf.getReporter());
		  }

}
