import java.io.PrintWriter;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.report.Reporter;


import gov.nasa.jpf.vm.Path;
//import se.kth.tracedata.Path;
//import kth.se.jpf.Interface.PathInterface;

public class ErrorTracePrinter extends Publisher {

	

	Path path;

	
	public ErrorTracePrinter(Config conf, Reporter reporter) {
		super(conf, reporter);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "errorTracePrinter";
	}

	@Override
	protected void openChannel() {
		if (out == null) {
			out = new PrintWriter(System.out, true);
		}
	}

	@Override
	protected void closeChannel() {
		out.close();
	}

	@Override
	public void publishTopicStart(String topic) {
		out.println();
		out.print("====================================================== ");
		out.println(topic);
	}

	@Override
	protected void publishTrace() {
		path = reporter.getPath();
		 //path = path.clone(); 
		//getpath() of the reported is returning the cone of the application hence we are directly 
		//calling it from the pathinterface clone method 
	}

	public Path getPath() {
		return path.clone();
	}

}
