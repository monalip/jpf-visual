package se.kth.tracedata.jpf;

import se.kth.tracedata.Instruction;

public abstract class FieldInstruction implements  Instruction {
	gov.nasa.jpf.vm.bytecode.FieldInstruction jpfFieldinstruction;
	
	
	public FieldInstruction(gov.nasa.jpf.vm.bytecode.FieldInstruction jFieldInstruction)
	{
			
		this.jpfFieldinstruction = jFieldInstruction;
	}
	
	public String getVariableId() {
		return jpfFieldinstruction.getVariableId();
	}

	
	public MethodInfo getMethodInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}