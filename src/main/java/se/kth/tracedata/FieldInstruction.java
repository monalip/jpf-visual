package se.kth.tracedata;

import se.kth.tracedata.jpf.Instruction;

public class FieldInstruction extends Instruction {
	
	protected String fname;
	  protected String ftype;
	  protected String className;
	  protected String varId;
	 public String getVariableId () {
		    if (varId == null) {
		      varId = className + '.' + fname;
		    }
		    return varId;
		  }

}
