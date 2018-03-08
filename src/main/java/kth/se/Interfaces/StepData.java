package kth.se.Interfaces;
import java.util.WeakHashMap;


import gov.nasa.jpf.util.Source;
import gov.nasa.jpf.vm.Instruction;
public class StepData {
	 private static WeakHashMap<StepData, String> s_comments = new WeakHashMap<StepData, String>();  // Not every Step gets a comment.  So save memory and put comments in a global comment HashMap.  Make this a WeakHashMap so that old Step objects can be GCed.

	  private final Instruction insn;
	  StepData next;
	  public StepData (Instruction insn) {
		    if (insn == null)
		      throw new IllegalArgumentException("insn == null");

		    this.insn = insn;
		  }
}
