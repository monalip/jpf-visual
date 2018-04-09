import java.io.PrintWriter;


//import se.kth.tracedata.Reporter;
import se.kth.tracedata.Reporter;
import se.kth.tracedata.jpf.Publisher;
//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.Path;
//import se.kth.tracedata.jpf.Path;

public class ErrorTracePrinter  extends Publisher{

	Path path;
	Reporter reporter;
	

	/*public ErrorTracePrinter(Config conf, Reporter reporter) {
		super(conf, reporter);
	}*/

	
	public String getName() {
		// TODO Auto-generated method stub
		return "errorTracePrinter";
	}

	
	protected void openChannel() {
		if (out == null) {
			out = new PrintWriter(System.out, true);
		}
	}

	protected void closeChannel() {
		out.close();
	}

	public void publishTopicStart(String topic) {
		out.println();
		out.print("====================================================== ");
		out.println(topic);
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
