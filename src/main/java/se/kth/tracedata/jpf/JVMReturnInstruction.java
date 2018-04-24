package se.kth.tracedata.jpf;

import se.kth.tracedata.Instruction;

public abstract class JVMReturnInstruction extends Instruction{
	
	gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction jpfJVMreturninstruction;
	static gov.nasa.jpf.vm.Instruction jpfInstruction;
	
	public JVMReturnInstruction(gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction jpfJVMreturninstruction)
	{
		
		this.jpfJVMreturninstruction = jpfJVMreturninstruction;
	}
	
	

}
