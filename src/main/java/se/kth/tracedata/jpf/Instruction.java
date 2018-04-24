package se.kth.tracedata.jpf;



public class Instruction implements se.kth.tracedata.Instruction {
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
	@Override
	// if jpfInstruction instanceof gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction then with the hepl of
	//instruction object we are calling methods getInvokedMethodName() and getInvokedMethodClassName()
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
	public boolean isInstanceofJVMReturnIns() {
		if(jpfInstruction instanceof gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction)
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	@Override
	public boolean isInstanceofLockIns() {
		if(jpfInstruction instanceof gov.nasa.jpf.jvm.bytecode.LockInstruction)
		{
			return true;
		}
		else
		{
		return false;
		}
	}
	@Override
	// jpfInstruction instanceof gov.nasa.jpf.jvm.bytecode.LockInstruction then we can method getLastLockRef with the help of instruction object
	public int getLastLockRef() {
		return ((gov.nasa.jpf.jvm.bytecode.LockInstruction)jpfInstruction).getLastLockRef();
	}
	
	

}
