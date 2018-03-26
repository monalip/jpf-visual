package se.kth.tracedata.jpf;




//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.MethodInfo;
//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;
import se.kth.tracedata.InstructionInterface;


/**
 * common root of all JPF bytecode instruction classes 
 * 
 */
public abstract class Instruction implements Cloneable, InstructionInterface{
	
	protected int insnIndex;        // code[] index of instruction
	  protected int position;     // accumulated bytecode position (prev pos + prev bc-length)
	  protected MethodInfo mi;    // the method this insn belongs to

	  // property/mode specific attributes
	  protected Object attr;
	  @Override
		public Instruction asInstruction() {
			// TODO Auto-generated method stub
			return this;
		}
	  
	// to allow a classname and methodname context for each instruction
	  public void setContext(String className, String methodName, int lineNumber,
	          int offset) {
	  }
	  /**
	   * is this the first instruction in a method
	   */
	  @Override
	  public boolean isFirstInstruction() {
		    return (insnIndex == 0);
		  }
	  /**
	   * answer if this is a potential loop closing jump
	   */
	  @Override
		public boolean isBackJump() {
			// TODO Auto-generated method stub
			return false;
		}
	  /**
	   * is this instruction part of a monitorenter code pattern 
	   */
	  public boolean isMonitorEnterPrologue(){
		    return false;
		  }
	  /**
	   * is this one of our own, artificial insns?
	   */

		@Override
		public boolean isExtendedInstruction() {
			// TODO Auto-generated method stub
			return false;
		}
	  
		@Override
		public MethodInfo getMethodInfo() {
			// TODO Auto-generated method stub
			return mi;
		}
		/**
		   * that's used for explicit construction of MethodInfos (synthetic methods)
		   */
		public void setMethodInfo(MethodInfo mi) {
		    this.mi = mi;
		  }
		/**
		   * this returns the instruction at the following code insnIndex within the same
		   * method, which might or might not be the next one to enter (branches, overlay calls etc.).
		   */
		 
		 @Override
			public Instruction getNext() {
				// TODO Auto-generated method stub
			 return mi.getInstruction(insnIndex + 1);
					
			}
		 @Override
			public int getInstructionIndex() {
				// TODO Auto-generated method stub
				return insnIndex;
			}
		 @Override
			public int getPosition() {
				// TODO Auto-generated method stub
				return position;
			}
		 public void setLocation(int insnIdx, int pos) {
			    insnIndex = insnIdx;
			    position = pos;
			  }
		 /**
		   * return the length in bytes of this instruction.
		   * override if this is not 1
		   */
		 @Override
			public int getLength() {
				// TODO Auto-generated method stub
				return 1;
			}
		 @Override
			public Instruction getPrev() {
			 if (insnIndex > 0) {
			      return mi.getInstruction(insnIndex - 1);
			    } else {
			      return null;
			    }
			}
		 /**
		   * this is for listeners that process instructionExecuted(), but need to
		   * determine if there was a CG registration, an overlayed direct call
		   * (like clinit) etc.
		   * The easy case is the instruction not having been executed yet, in
		   * which case ti.getNextPC() == null
		   * There are two cases for re-execution: either nextPC was set to the
		   * same insn (which is what CG creators usually use), or somebody just
		   * pushed another stackframe that executes something which will return to the
		   * same insn (that is what automatic <clinit> calls and the like do - we call
		   * it overlays)
		   */

			@Override
			public boolean isCompleted(ThreadInfo ti) {
				// TODO Auto-generated method stub
				return false;

			}
			@Override
			  public String toString() {
			    return getMnemonic();
			  }

		
	@Override
	public boolean hasAttr() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAttr(Class<?> attrType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getAttr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttr(Object a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttr(Object a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttr(Object a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceAttr(Object oldAttr, Object newAttr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getAttr(Class<T> attrType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getNextAttr(Class<T> attrType, Object prev) {
		// TODO Auto-generated method stub
		return null;
	}

	

	

	@Override
	public int getByteCode() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getSourceLine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSourceLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instruction execute(ThreadInfo ti) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toPostExecString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMnemonic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLineNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFilePos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instruction getNext(ThreadInfo ti) {
		// TODO Auto-generated method stub
		return null;
	}

 
 
}
