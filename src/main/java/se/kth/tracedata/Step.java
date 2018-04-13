package se.kth.tracedata;

import gov.nasa.jpf.vm.Instruction;
//import se.kth.tracedata.jpf.Instruction;

public interface Step {
	public String getLineString () ;
	 public String getLocationString();
	 public Instruction getInstruction();

}
