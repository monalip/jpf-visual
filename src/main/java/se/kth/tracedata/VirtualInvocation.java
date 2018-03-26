package se.kth.tracedata;

import se.kth.tracedata.jpf.Instruction;

public class VirtualInvocation  extends JVMInvokeInstruction{
	 /* Those are all straight from the class file.
	   * Note that we can't directly resolve to MethodInfo objects because
	   * the corresponding class might not be loaded yet (has to be done
	   * on execution)
	   */
	  protected String cname;
	  protected String mname;
	  public String getInvokedMethodClassName() {
		    return cname;
		  }
	  public String getInvokedMethodName () {
		    return mname;
		  }


}
