package se.kth.tracedata;

public interface LockInstruction {
	/**
	    * only useful post-execution (in an instructionExecuted() notification)
	    */
	  public int getLastLockRef () ;

}
