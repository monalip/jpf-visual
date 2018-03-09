package kth.se.Adapters.jpf;

import gov.nasa.jpf.vm.Instruction;
import kth.se.Interfaces.StepInterface;
import gov.nasa.jpf.vm.Step; 

public class StepInterfaceImp  implements StepInterface{
	Step step;

	@Override
	public Instruction getInstruction() {
		// TODO Auto-generated method stub
		return (step.getInstruction());
	}

	@Override
	public String getLocationString() {
		// TODO Auto-generated method stub
		return (step.getLocationString());
	}

	@Override
	public String getLineString() {
		// TODO Auto-generated method stub
		return (step.getLineString());
	}

}
