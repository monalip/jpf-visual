package se.kth.tracedata.jpf;

import se.kth.tracedata.JVMReturnInstruction;

public class JVMInvokeInstruction implements se.kth.tracedata.JVMInvokeInstruction{
	
	gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction jpfJVMinvokeinstruct;
	
	public JVMInvokeInstruction(gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction jpfJVMinvokeinstruct)
	{
		this.jpfJVMinvokeinstruct = jpfJVMinvokeinstruct;
	}

	@Override
	public String getInvokedMethodName() {
		return jpfJVMinvokeinstruct.getInvokedMethodName();
	}

	@Override
	public String getInvokedMethodClassName() {
		return jpfJVMinvokeinstruct.getInvokedMethodClassName();
	}

}
