package kth.se.Interfaces.jpf;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

public interface InstructionJpfInterface {
	public MethodInfo getMethodInfo();
	public String toString();
	public String getFileLocation();
	public Instruction getPrev();

}
