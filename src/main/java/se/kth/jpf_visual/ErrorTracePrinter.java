package se.kth.jpf_visual;

import java.io.PrintWriter;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.report.Publisher;

//import gov.nasa.jpf.report.Publisher;

//import gov.nasa.jpf.Config;

//import se.kth.tracedata.Reporter;
//import se.kth.tracedata.Reporter;
//import se.kth.tracedata.jpf.Config;
//import se.kth.tracedata.jpf.Publisher;

//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.Path;
//import se.kth.tracedata.jpf.Path;
import gov.nasa.jpf.report.Reporter;

public class ErrorTracePrinter extends Publisher {

	se.kth.tracedata.jpf.Path path;

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
		path = new se.kth.tracedata.jpf.Path(reporter.getPath());
	}

	public se.kth.tracedata.jpf.Path getPath() {
		return path.clone();
	}

}
