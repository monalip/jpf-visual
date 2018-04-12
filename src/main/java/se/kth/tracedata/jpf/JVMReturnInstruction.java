package se.kth.tracedata.jpf;

public class JVMReturnInstruction implements se.kth.tracedata.JVMReturnInstruction{
	
	gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction jpfJVMreturninstruction;
	
	public JVMReturnInstruction(gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction jpfJVMreturninstruction)
	{
		this.jpfJVMreturninstruction = jpfJVMreturninstruction;
	}
	
	

}
