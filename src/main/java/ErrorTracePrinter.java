import java.io.PrintWriter;

//import gov.nasa.jpf.report.Publisher;

//import gov.nasa.jpf.Config;

//import se.kth.tracedata.Reporter;
import se.kth.tracedata.Reporter;
//import se.kth.tracedata.jpf.Config;
import se.kth.tracedata.jpf.Publisher;

//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.Path;
//import se.kth.tracedata.jpf.Path;

public class ErrorTracePrinter  extends Publisher{

	protected ErrorTracePrinter() {
		super();
		// TODO Auto-generated constructor stub
	}


	Path path;
	Reporter reporter;
	

	/*public ErrorTracePrinter(Config conf, Reporter reporter) {
		super(conf,reporter);
	}*/

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
		
	}

	
	public Path getPath() {
		path =   reporter.getPath();
		
		if(path != null)
		{
			return  path.clone();
		}
		return null;
	}

}
