package se.kth.tracedata;

//import gov.nasa.jpf.vm.MethodInfo;
import se.kth.tracedata.MethodInfo;
//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;
import se.kth.tracedata.jpf.Instruction;

/**
 * we use that to access Instruction methods from xInstruction interfaces
 * 
 * NOTE - this has to be kept in sync with Instruction
 */
public interface InstructionInterface extends Attributable {

	/**
	   * this is for cases where we need the Instruction type. Try to use InstructionInterface in clients
	   */
	  Instruction asInstruction();
	  int getByteCode();
	  boolean isFirstInstruction();
	  boolean isBackJump();
	  boolean isExtendedInstruction();
	  Instruction getNext();
	  int getInstructionIndex();
	  int getPosition();
	  MethodInfo getMethodInfo();
	  int getLength();
	  Instruction getPrev();
	  boolean isCompleted(ThreadInfo ti);
	  String getSourceLine();
	  String getSourceLocation();
	  Instruction execute(ThreadInfo ti);
	  String toPostExecString();
	  String getMnemonic();
	  int getLineNumber();
	  String getFileLocation();
	  String getFilePos();
	  Instruction getNext (ThreadInfo ti);

	  
	  
	  //.. and probably a lot still missing

}
