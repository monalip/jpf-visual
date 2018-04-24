package se.kth.tracedata.jpf;

import se.kth.tracedata.Instruction;

//import gov.nasa.jpf.vm.Instruction;

public abstract class LockInstruction  extends Instruction{
	
	gov.nasa.jpf.jvm.bytecode.LockInstruction jpfLockinstruction;
	static gov.nasa.jpf.vm.Instruction jpfInstruction;
	

	 public LockInstruction(gov.nasa.jpf.jvm.bytecode.LockInstruction jpfLockinstruction) {
		 
		 this.jpfLockinstruction = jpfLockinstruction;
	 }
	
	public int getLastLockRef() {
		return jpfLockinstruction.getLastLockRef();
	}

}
