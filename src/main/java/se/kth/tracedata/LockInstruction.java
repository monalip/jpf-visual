package se.kth.tracedata;

//import gov.nasa.jpf.vm.Instruction;

public abstract class LockInstruction extends Instruction
{
	/**
	    * only useful post-execution (in an instructionExecuted() notification)
	    */
	  public abstract int getLastLockRef () ;

}
