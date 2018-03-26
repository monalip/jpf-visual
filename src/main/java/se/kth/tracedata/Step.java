package se.kth.tracedata;


//import gov.nasa.jpf.util.Source;
//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.MethodInfo;
import se.kth.tracedata.jpf.Instruction;

public class Step {
	Step next;
	Instruction i;
	private final Instruction insn = i;
	public Instruction getInstruction() {
	    return insn;
	  }
	public String getLineString () {
	    MethodInfo mi = insn.getMethodInfo();
	    if (mi != null) {
	      Source source = Source.getSource(mi.getSourceFileName());
	      if (source != null) {
	        int line = mi.getLineNumber(insn);
	        if (line > 0) {
	          return source.getLine(line);
	        }
	      }
	    }

	    return null;
	  }
	public String getLocationString() {
	    MethodInfo mi = insn.getMethodInfo();
	    if (mi != null) {
	      return mi.getSourceFileName() + ':' + mi.getLineNumber(insn);
	    }

	    return "?:?";
	  }
	public Step getNext() {
	    return next;
	  }

}
