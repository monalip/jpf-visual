package se.kth.tracedata.jpf;

public class VirtualInvocation implements se.kth.tracedata.VirtualInvocation{
	gov.nasa.jpf.jvm.bytecode.VirtualInvocation jpfVirtualinvocation;
	
	public VirtualInvocation(gov.nasa.jpf.jvm.bytecode.VirtualInvocation jpfVirtualinvocation)
	{
		
		this.jpfVirtualinvocation = jpfVirtualinvocation;
	}

	@Override
	public String getInvokedMethodName() {
		return jpfVirtualinvocation.getInvokedMethodName();
	}

	@Override
	public String getInvokedMethodClassName() {
		return jpfVirtualinvocation.getInvokedMethodClassName();
	}
}
