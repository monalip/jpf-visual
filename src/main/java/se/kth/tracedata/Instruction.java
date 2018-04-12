package se.kth.tracedata;

//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.jpf.MethodInfo;

public interface Instruction {
	public MethodInfo getMethodInfo();
	  public String getFileLocation();

}
