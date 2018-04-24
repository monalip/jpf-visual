package se.kth.tracedata.jpf;

import se.kth.tracedata.Instruction;

public abstract class VirtualInvocation implements Instruction{
	gov.nasa.jpf.jvm.bytecode.VirtualInvocation jpfVirtualinvocation;
	
	public VirtualInvocation(gov.nasa.jpf.jvm.bytecode.VirtualInvocation jpfVirtualinvocation)
	{
			
		this.jpfVirtualinvocation = jpfVirtualinvocation;
	}

	
	public String getInvokedMethodName() {
		return jpfVirtualinvocation.getInvokedMethodName();
	}

	
	public String getInvokedMethodClassName() {
		return jpfVirtualinvocation.getInvokedMethodClassName();
	}
}
