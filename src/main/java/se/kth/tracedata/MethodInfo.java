package se.kth.tracedata;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;
import se.kth.tracedata.jpf.Instruction;


public class MethodInfo {

	static final int INIT_MTH_SIZE = 4096;
	  
	 /** Instructions associated with the method */
	  protected Instruction[] code;
	  /** the standard Java modifier attributes */
	  protected int modifiers;
	  /** Class the method belongs to */
	  protected ClassInfo ci;
	  /**
	   * this is a lazy evaluated mangled name consisting of the name and
	   * arg type signature
	   */
	  protected String uniqueName;
	  /** Table used for line numbers 
	   * this assigns a line number to every instruction index, instead of 
	   * using an array of ranges. Assuming we have 2-3 insns per line on average,
	   * this should still require less memory than a reference array with associated
	   * range objects, and allows faster access of instruction line numbers, which
	   * we might need for location specs
	   */
	  protected int[] lineNumbers;
	   
	protected static final ArrayList<MethodInfo> mthTable = new ArrayList<MethodInfo>(INIT_MTH_SIZE);
	
	public static MethodInfo getMethodInfo (int globalId){
	    if (globalId >=0 && globalId <mthTable.size()){
	      return mthTable.get(globalId);
	    } else {
	      return null;
	    }
	  }
	public Instruction getInstruction (int i) {
	    if (code == null) {
	      return null;
	    }

	    if ((i < 0) || (i >= code.length)) {
	      return null;
	    }

	    return code[i];
	  }
	/**
	   * Returns true if the method is synchronized.
	   */
	  public boolean isSynchronized () {
	    return ((modifiers & Modifier.SYNCHRONIZED) != 0);
	  }
	  /**
	   * Returns the class the method belongs to.
	   */
	  public ClassInfo getClassInfo () {
	    return ci;
	  }
	  public String getUniqueName () {
		    return uniqueName;
		  }

	  public String getSourceFileName () {
		    if (ci != null) {
		      return ci.getSourceFileName();
		    } else {
		      return "[VM]";
		    }
		  }
	  /**
	   * Returns the line number for a given position.
	   */
	  public int getLineNumber (Instruction pc) {
	    if (lineNumbers == null) {
	      if (pc == null)
	        return -1;
	      else
	        return pc.getPosition();
	    }

	    if (pc != null) {
	      int idx = pc.getInstructionIndex();
	      if (idx < 0) idx = 0;
	      return lineNumbers[idx];
	    } else {
	      return -1;
	    }
	  }
	  public String getClassName () {
		    if (ci != null) {
		      return ci.getName();
		    } else {
		      return "[VM]";
		    }
		  }
	  public static int getNumberOfLoadedMethods () {
		    return mthTable.size();
		  }
	  
	  /**
	   * Returns the full classname (if any) + name + signature.
	   */
	  public String getFullName () {
	    if (ci != null) {
	      return ci.getName() + '.' + getUniqueName();
	    } else {
	      return getUniqueName();
	    }
	  }


}
