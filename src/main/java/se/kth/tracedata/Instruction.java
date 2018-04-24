package se.kth.tracedata;

//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.jpf.MethodInfo;

public abstract class Instruction {
	 
	public abstract MethodInfo getMethodInfo() ;
	public abstract String getFileLocation();
	public abstract String getInvokedMethodName();
	public abstract String getInvokedMethodClassName();
	public abstract boolean isInstanceofJVMInvok();
	public abstract boolean isInstanceofJVMReturnIns();
	
}
