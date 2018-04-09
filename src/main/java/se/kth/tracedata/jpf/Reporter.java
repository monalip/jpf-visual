package se.kth.tracedata.jpf;

import java.util.ArrayList;
import java.util.List;

//import gov.nasa.jpf.report.Publisher;
import se.kth.tracedata.jpf.Publisher;
//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.jpf.Path;


public class Reporter implements se.kth.tracedata.Reporter{
	
	gov.nasa.jpf.report.Reporter jpfreporter;
	
	public Reporter(gov.nasa.jpf.report.Reporter jpfreporter) {
		this.jpfreporter = jpfreporter;
		
	}
	protected List<se.kth.tracedata.jpf.Publisher> Publishers = new ArrayList<se.kth.tracedata.jpf.Publisher>();
	public Path getPath (){
	    return new Path(jpfreporter.getPath());
	  }
	
	// as we cannot wrap nas publisher of the list directly we iterate through the loop and 
	//going through each iteration and wrap it to the our package iterater
	public List<Publisher> getPublishers() {
		List<gov.nasa.jpf.report.Publisher> Listpublisher= jpfreporter.getPublishers();
		for(gov.nasa.jpf.report.Publisher p : Listpublisher)
		{
			Publishers.add(new Publisher(p));
		}
		return Publishers;
	    
	  }
	
	
}