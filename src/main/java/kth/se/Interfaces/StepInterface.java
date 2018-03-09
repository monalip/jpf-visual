package kth.se.Interfaces;
import gov.nasa.jpf.vm.Instruction;

public interface StepInterface {
		public Instruction getInstruction();
		public String getLocationString();
		public String getLineString();

}
