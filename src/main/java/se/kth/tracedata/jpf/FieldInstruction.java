package se.kth.tracedata.jpf;

public class FieldInstruction  implements se.kth.tracedata.FieldInstruction{
	gov.nasa.jpf.vm.bytecode.FieldInstruction jpfFieldinstruction;
	public FieldInstruction(gov.nasa.jpf.vm.bytecode.FieldInstruction jFieldInstruction)
	{
		this.jpfFieldinstruction = jFieldInstruction;
	}
	@Override
	public String getVariableId() {
		return jpfFieldinstruction.getVariableId();
	}

}
