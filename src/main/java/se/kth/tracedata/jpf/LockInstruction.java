package se.kth.tracedata.jpf;

public class LockInstruction implements se.kth.tracedata.LockInstruction{
	
	 gov.nasa.jpf.jvm.bytecode.LockInstruction jpfLockinstruction;
	 public LockInstruction( gov.nasa.jpf.jvm.bytecode.LockInstruction jpInstruction) {
		 this.jpfLockinstruction = jpfLockinstruction;
	 }
	@Override
	public int getLastLockRef() {
		return jpfLockinstruction.getLastLockRef();
	}

}
