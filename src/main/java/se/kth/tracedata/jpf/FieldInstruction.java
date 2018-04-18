package se.kth.tracedata.jpf;

//import gov.nasa.jpf.vm.Instruction;
import se.kth.tracedata.jpf.Instruction;

public class FieldInstruction  extends Instruction implements se.kth.tracedata.FieldInstruction{
	gov.nasa.jpf.vm.bytecode.FieldInstruction jpfFieldinstruction;
	gov.nasa.jpf.vm.Instruction jpfInstruction;
	
	public FieldInstruction(gov.nasa.jpf.vm.bytecode.FieldInstruction jFieldInstruction)
	{
		super(jFieldInstruction);
		this.jpfFieldinstruction = jFieldInstruction;
	}
	@Override
	public String getVariableId() {
		return jpfFieldinstruction.getVariableId();
	}

}
