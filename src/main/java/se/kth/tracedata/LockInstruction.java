package se.kth.tracedata;

import se.kth.tracedata.jpf.Instruction;

public abstract class LockInstruction extends Instruction {
	  int lastLockRef = MJIEnv.NULL;
	  /**
	    * only useful post-execution (in an instructionExecuted() notification)
	    */
	  public int getLastLockRef () {
	    return lastLockRef;
	  }


}

