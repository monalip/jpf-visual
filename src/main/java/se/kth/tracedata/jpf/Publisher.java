package se.kth.tracedata.jpf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



/**
 * abstract base for all format specific publishers. Note that this interface
 * also has to work for non-stream based reporting, i.e. within Eclipse
 * (we don't want to re-parse from text reports there)
 */
public class Publisher implements se.kth.tracedata.Publisher  {
	
	static gov.nasa.jpf.Config jpfconfig;
	static gov.nasa.jpf.report.Reporter jpfreporter;
	gov.nasa.jpf.report.Publisher jpfpublisher;
	public List<gov.nasa.jpf.report.Publisher> publishers = new ArrayList<gov.nasa.jpf.report.Publisher>();
	
	
	
public Publisher (gov.nasa.jpf.report.Publisher jpfpublisher) {
	
	    this.jpfpublisher = jpfpublisher; 
	    
	  }
public Publisher(gov.nasa.jpf.Config jpfconfig,gov.nasa.jpf.report.Reporter jpfreporter)
{
	
}

	
/*public Publisher (List<gov.nasa.jpf.report.Publisher> jpfpublisher) {
	  	
	    this.publishers = jpfpublisher; 
	  }*/
		  
	  /**
	   * to be initialized in openChannel
	   * NOTE - not all publishers need to have one
	   */
	  protected PrintWriter out;

	  public PrintWriter getOut () {
	    return out;
	  }

	  
	 
	 

	  

	  //--- open/close streams etc
	  protected void openChannel(){}
	  protected void closeChannel(){}
	  public void publishTopicStart (String topic) {
		    // to be done by subclasses
		  }
	  protected void publishTrace() {}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	  
	  
}
