package kth.se.Adapters.jpf;

import gov.nasa.jpf.vm.MethodInfo;
import kth.se.Interfaces.jpf.InstructionJpfInterface;


import gov.nasa.jpf.vm.Instruction; 

public class InstructionJpfImp implements InstructionJpfInterface{
	private Instruction ins;
	
	public InstructionJpfImp(Instruction insj){
		this.ins = insj;
	}
	

	@Override
	public MethodInfo getMethodInfo() {
		// TODO Auto-generated method stub
		return (ins.getMethodInfo());
	}
	@Override
	public String toString()
	{
		return (ins.toString());
	}
	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return (ins.getFileLocation());
	}
	@Override
	public Instruction getPrev() {
		// TODO Auto-generated method stub
		return (ins.getPrev());
	}
	
	

}
