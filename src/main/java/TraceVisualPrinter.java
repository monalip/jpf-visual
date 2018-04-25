
/*
 * Copyright (C) 2014, United States Government, as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All rights reserved.
 *
 * The Java Pathfinder core (jpf-core) platform is licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import gov.nasa.jpf.Config;
//import gov.nasa.jpf.Error;
import se.kth.tracedata.Error;
//import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.report.Publisher;
//import se.kth.tracedata.
import gov.nasa.jpf.report.Reporter;
//import se.kth.tracedata.Reporter;
import gov.nasa.jpf.report.Statistics;
//import gov.nasa.jpf.util.Left;
import se.kth.tracedata.Left;
import se.kth.tracedata.ChoiceGenerator;
//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;
//import gov.nasa.jpf.vm.ClassLoaderInfo;
import se.kth.tracedata.ClassLoaderInfo;
//import gov.nasa.jpf.vm.Instruction;
import se.kth.tracedata.Instruction;
//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.MethodInfo;
//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.Path;
//import gov.nasa.jpf.vm.Step;
import se.kth.tracedata.Step;
//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;
//import gov.nasa.jpf.vm.Transition;
import se.kth.tracedata.Transition;
import gov.nasa.jpf.vm.VM;


public class TraceVisualPrinter extends Publisher {

	// output destinations
	String fileName;
	FileOutputStream fos;
	private se.kth.tracedata.jpf.Path path;

	String port;

	// the various degrees of information for program traces
	protected boolean showCG;
	protected boolean showSteps;
	protected boolean showLocation;
	protected boolean showSource;
	protected boolean showMethod;
	protected boolean showCode;

	public TraceVisualPrinter(Config conf, Reporter reporter) {
		super(conf, reporter);

		// options controlling the output destination
		fileName = conf.getString("report.console.file");
		port = conf.getString("report.console.port");

		// options controlling what info should be included in a trace
		showCG = conf.getBoolean("report.console.show_cg", true);
		showSteps = conf.getBoolean("report.console.show_steps", true);
		showLocation = conf.getBoolean("report.console.show_location", true);
		showSource = conf.getBoolean("report.console.show_source", true);
		showMethod = conf.getBoolean("report.console.show_method", false);
		showCode = conf.getBoolean("report.console.show_code", false);
	}

	@Override
	protected void openChannel() {

		if (fileName != null) {
			try {
				fos = new FileOutputStream(fileName);
				out = new PrintWriter(fos);
			} catch (FileNotFoundException x) {
				// fall back to System.out
			}
		} else if (port != null) {
			// <2do>
		}

		if (out == null) {
			out = new PrintWriter(System.out, true);
		}
	}

	@Override
	protected void closeChannel() {
		if (fos != null) {
			out.close();
		}
	}

	@Override
	public void publishTopicStart(String topic) {
		out.println();
		out.print("====================================================== ");
		out.println(topic);
	}

	@Override
	public void publishTopicEnd(String topic) {
		// nothing here
	}

	@Override
	public void publishStart() {
		super.publishStart();

		if (startItems.length > 0) { // only report if we have output for this
										// phase
			publishTopicStart("search started: " + formatDTG(reporter.getStartDate()));
		}
	}

	@Override
	public void publishFinished() {
		super.publishFinished();

		if (finishedItems.length > 0) { // only report if we have output for
										// this phase
			publishTopicStart("search finished: " + formatDTG(reporter.getFinishedDate()));
		}
	}

	@Override
	protected void publishJPF() {
		out.println(reporter.getJPFBanner());
		out.println();
	}

	@Override
	protected void publishDTG() {
		out.println("started: " + reporter.getStartDate());
	}

	@Override
	protected void publishUser() {
		out.println("user: " + reporter.getUser());
	}

	@Override
	protected void publishJPFConfig() {
		publishTopicStart("JPF configuration");

		TreeMap<Object, Object> map = conf.asOrderedMap();
		Set<Map.Entry<Object, Object>> eSet = map.entrySet();

		for (Object src : conf.getSources()) {
			out.print("property source: ");
			out.println(conf.getSourceName(src));
		}

		out.println("properties:");
		for (Map.Entry<Object, Object> e : eSet) {
			out.println("  " + e.getKey() + "=" + e.getValue());
		}
	}

	@Override
	protected void publishPlatform() {
		publishTopicStart("platform");
		out.println("hostname: " + reporter.getHostName());
		out.println("arch: " + reporter.getArch());
		out.println("os: " + reporter.getOS());
		out.println("java: " + reporter.getJava());
	}

	@Override
	protected void publishSuT() {
		publishTopicStart("system under test");
		out.println(reporter.getSuT());
	}

	@Override
	protected void publishError() {
		Error e = new se.kth.tracedata.jpf.Error(reporter.getCurrentError());

		publishTopicStart("error " + e.getId());
		out.println(e.getDescription());

		String s = e.getDetails();
		if (s != null) {
			out.println(s);
		}

	}

	@Override
	protected void publishConstraint() {
		String constraint = reporter.getLastSearchConstraint();
		publishTopicStart("search constraint");
		out.println(constraint); // not much info here yet
	}

	@Override
	protected void publishResult() {
		List<gov.nasa.jpf.Error> jpferrors = reporter.getErrors();
		//convert list of nas a errors to se.kth.tracedata type errors
		List<Error> errors=  new ArrayList<Error>();
		for(gov.nasa.jpf.Error err : jpferrors)
		{
			errors.add(new se.kth.tracedata.jpf.Error(err));
		}

		publishTopicStart("results");

		if (errors.isEmpty()) {
			out.println("no errors detected");
		} else {
			for (Error e : errors) {
				out.print("error #");
				out.print(e.getId());
				out.print(": ");
				out.print(e.getDescription());

				String s = e.getDetails();
				if (s != null) {
					s = s.replace('\n', ' ');
					s = s.replace('\t', ' ');
					s = s.replace('\r', ' ');
					out.print(" \"");
					if (s.length() > 50) {
						out.print(s.substring(0, 50));
						out.print("...");
					} else {
						out.print(s);
					}
					out.print('"');
				}

				out.println();
			}
		}
	}

	/**
	 * this is done as part of the property violation reporting, i.e. we have an
	 * error
	 */

	// this is the main method we use to experiment
	@Override
	protected void publishTrace() {
		//Type mismatch: cannot convert from gov.nasa.jpf.vm.Path to se.kth.tracedata.Path error is resolved using Path adapter
		this.path = new se.kth.tracedata.jpf.Path(reporter.getPath());
		int i = 0;

		if (path.size() == 0) {
			return; // nothing to publish
		}

		publishTopicStart("trace " + reporter.getCurrentErrorId());

		for (se.kth.tracedata.jpf.Transition t : path) {
			out.print("------------------------------------------------------ ");
			out.println("transition #" + i++ + " thread: " + t.getThreadIndex());

			if (showCG) {
				out.println(t.getChoiceGenerator());
				//if (t.getChoiceGenerator() instanceof ThreadChoiceFromSet) {
				// t.getChoiceGenerator() instanceof ThreadChoiceFromSet this condition is checked using isInstaceofThreadChoiceFromSet() method of choicegenerator
				ChoiceGenerator<?> cg = t.getChoiceGenerator();
				if (cg.isInstaceofThreadChoiceFromSet()) {

					//for (ThreadInfo ti : ((ThreadChoiceFromSet) t.getChoiceGenerator()).getChoices()) {
					//getChoices method is directly created inside the choicegenerator so no need of casting
					for (ThreadInfo ti : cg.getChoices()) {
						out.println("	ti: " + ti);
					}

				}
			}

			if (showSteps) {
				String lastLine = null;
				int nNoSrc = 0;

				for (Step s : t) {
					String line = s.getLineString();

					if (showSource) {
						if (line != null) {
							if (!line.equals(lastLine)) {
								if (nNoSrc > 0) {
									out.println("      [[[" + nNoSrc + " insn w/o sources]");
								}

								out.print("  ");
								if (showLocation) {
									out.print(Left.format(s.getLocationString(), 30));
									out.print(" : ");
								}
								out.println(line.trim());
								nNoSrc = 0;

							}

						} else { // no source
							nNoSrc++;
						}

						lastLine = line;
					}

					if (line != null) {

						/* more information of the trace */
						if (true) {
							Instruction insn = s.getInstruction();
							//if (insn instanceof JVMInvokeInstruction) {
							//if condition is checked using isInstanceofJVMInvok() method
							
								if (insn.isInstanceofJVMInvok())
								{
									///
									/*out.println("JVMInvokeInstruction: "
											+ ((JVMInvokeInstruction) insn).getInvokedMethodClassName() + "."
											+ ((JVMInvokeInstruction) insn).getInvokedMethodName().replaceAll("\\(.*$",
													""));*/
									
									// above methods are defined in instrutor class so no need of casting
									out.println("JVMInvokeInstruction: "
											+ insn.getInvokedMethodClassName() + "."
											+ insn.getInvokedMethodName().replaceAll("\\(.*$",
													""));
								}

							if (true) {
								MethodInfo mi = insn.getMethodInfo();
								out.println(" mi uniqueName: " + mi.getUniqueName());

								ClassInfo mci = mi.getClassInfo();

								if (mi.isSynchronized()) {
									out.print("mi synchronized : " + mi.isSynchronized());
								}

								out.print("mci:    ");
								if (mci != null) {
									out.println(" className: " + mci.getName());
								}
							}

						}
					}

				}
			}
		}
	}

	@Override
	protected void publishOutput() {
		se.kth.tracedata.jpf.Path path = new se.kth.tracedata.jpf.Path(reporter.getPath());

		if (path.size() == 0) {
			return; // nothing to publish
		}

		publishTopicStart("output " + reporter.getCurrentErrorId());

		if (path.hasOutput()) {
			for (Transition t : path) {
				String s = t.getOutput();
				if (s != null) {
					out.print(s);
				}
			}
		} else {
			out.println("no output");
		}
	}

	@Override
	protected void publishSnapshot() {
		VM vm = reporter.getVM();

		// not so nice - we have to delegate this since it's using a lot of
		// internals, and is also
		// used in debugging
		publishTopicStart("snapshot " + reporter.getCurrentErrorId());

		if (vm.getPathLength() > 0) {
			vm.printLiveThreadStatus(out);
		} else {
			out.println("initial program state");
		}
	}

	public static final String STATISTICS_TOPIC = "statistics";

	// this is useful if somebody wants to monitor progress from a specialized
	// ConsolePublisher
	public synchronized void printStatistics(PrintWriter pw) {
		publishTopicStart(STATISTICS_TOPIC);
		printStatistics(pw, reporter);
	}

	// this can be used outside a publisher, to show the same info
	public static void printStatistics(PrintWriter pw, Reporter reporter) {
		Statistics stat = reporter.getStatistics();

		pw.println("elapsed time:       " + formatHMS(reporter.getElapsedTime()));
		pw.println("states:             new=" + stat.newStates + ",visited=" + stat.visitedStates + ",backtracked="
				+ stat.backtracked + ",end=" + stat.endStates);
		pw.println("search:             maxDepth=" + stat.maxDepth + ",constraints=" + stat.constraints);
		pw.println("choice generators:  thread=" + stat.threadCGs + " (signal=" + stat.signalCGs + ",lock="
				+ stat.monitorCGs + ",sharedRef=" + stat.sharedAccessCGs + ",threadApi=" + stat.threadApiCGs
				+ ",reschedule=" + stat.breakTransitionCGs + "), data=" + stat.dataCGs);
		pw.println("heap:               " + "new=" + stat.nNewObjects + ",released=" + stat.nReleasedObjects
				+ ",maxLive=" + stat.maxLiveObjects + ",gcCycles=" + stat.gcCycles);
		pw.println("instructions:       " + stat.insns);
		pw.println("max memory:         " + (stat.maxUsed >> 20) + "MB");

		pw.println("loaded code:        classes=" + se.kth.tracedata.jpf.ClassLoaderInfo.getNumberOfLoadedClasses() + ",methods="
				+ se.kth.tracedata.jpf.MethodInfo.getNumberOfLoadedMethods());
	}

	@Override
	public void publishStatistics() {
		printStatistics(out);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "consoleTracePrinter";
	}

}
