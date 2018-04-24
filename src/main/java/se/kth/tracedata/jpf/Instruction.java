package se.kth.tracedata.jpf;



public class Instruction extends se.kth.tracedata.Instruction {
	 gov.nasa.jpf.vm.Instruction jpfInstruction;
	
	public Instruction(gov.nasa.jpf.vm.Instruction jpfInstruction)
	{
		this.jpfInstruction = jpfInstruction;
	}
	@Override
	public MethodInfo getMethodInfo() {
		return new MethodInfo(jpfInstruction.getMethodInfo());
	}
	@Override
	public String getFileLocation() {
		return jpfInstruction.getFileLocation();
	}
	
	@Override
	public String getInvokedMethodName() {
		
		String methodName = (( gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction) jpfInstruction).getInvokedMethodName();
		return methodName;
		
		//return jpfJVMinvokeinstruct.getInvokedMethodName();
	}
	@Override
	public String getInvokedMethodClassName() {
		String clsName = ((gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction) jpfInstruction).getInvokedMethodClassName();
		//String clsName = ((JVMInvokeInstruction) ins).getInvokedMethodClassName();
		return clsName;
	}
	@Override
	public boolean isInstanceofJVMInvok() {
		if(jpfInstruction instanceof gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction)
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	
	

}
