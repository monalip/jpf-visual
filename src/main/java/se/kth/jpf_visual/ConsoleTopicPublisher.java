package se.kth.jpf_visual;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.report.Reporter;

/**
 * Redirects {@link gov.nasa.jpf.traceServer.printer.ConsoleTracePrinter
 * ConsoleTracePrinter}'s output to the Shell's trace report panel.
 * 
 * @author Igor Andjelkovic
 * 
 */

public class ConsoleTopicPublisher extends TraceVisualPrinter implements TopicPublisher {

	private LinkedHashMap<String, Topic> topics;
	private StringWriter output;

	private String curTopic;

	public ConsoleTopicPublisher(Config config, Reporter reporter) {
		super(config, reporter);
		topics = new LinkedHashMap<String, Topic>();
	}

	protected void setTopics() {
		setTopicItems("consoleTracePrinter");
	}

	public Map<String, Topic> getResults() {
		return topics;
	}

	protected void openChannel() {
		output = new StringWriter();
		out = new PrintWriter(output);
	}

	protected void closeChannel() {
		try {
			out.close();
			output.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void publishTopicStart(String topic) {
		if (topic != null) {
			StringBuffer buff = output.getBuffer();
			if (buff.length() > 0) {
				topics.put("console " + curTopic, new Topic(buff.toString()));
				System.out.println("TEST topic = " + topic +" curTopic = " + curTopic);
				buff.setLength(0); // reset the output buffer
			}
		}
		curTopic = topic;
	}

	public void publishEpilog() {
		publishTopicStart("");
	}

}