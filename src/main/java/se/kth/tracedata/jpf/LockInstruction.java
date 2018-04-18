package se.kth.tracedata.jpf;

//import gov.nasa.jpf.vm.Instruction;
import se.kth.tracedata.jpf.Instruction;

public class LockInstruction extends Instruction implements se.kth.tracedata.LockInstruction{
	
	gov.nasa.jpf.jvm.bytecode.LockInstruction jpfLockinstruction;
	
	static gov.nasa.jpf.vm.Instruction jpfInsrt;
	 public LockInstruction(gov.nasa.jpf.jvm.bytecode.LockInstruction jpfLockinstruction) {
		 super(jpfInsrt);
		 this.jpfLockinstruction = jpfLockinstruction;
	 }
	@Override
	public int getLastLockRef() {
		return jpfLockinstruction.getLastLockRef();
	}

}
