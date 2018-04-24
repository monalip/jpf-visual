package se.kth.tracedata;

//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.jpf.MethodInfo;

public interface Instruction {
	 
	public  MethodInfo getMethodInfo() ;
	public  String getFileLocation();
	public  String getInvokedMethodName();
	public  String getInvokedMethodClassName();
	public  boolean isInstanceofJVMInvok();
	public  boolean isInstanceofJVMReturnIns();
	public  boolean isInstanceofLockIns();
	public int getLastLockRef();
	public  boolean isInstanceofVirtualInv();
	public  boolean isInstanceofFieldIns();
	public String getVariableId();
	
	
}
