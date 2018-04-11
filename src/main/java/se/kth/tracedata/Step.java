package se.kth.tracedata;

import gov.nasa.jpf.vm.Instruction;

public interface Step {
	public String getLineString () ;
	 public String getLocationString();
	 public Instruction getInstruction();

}
