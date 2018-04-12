package se.kth.tracedata.jpf;

public class Instruction implements se.kth.tracedata.Instruction{
	gov.nasa.jpf.vm.Instruction jpfInstruction;
	public Instruction(gov.nasa.jpf.vm.Instruction jpfInstruction)
	{
		this.jpfInstruction = jpfInstruction;
	}
	@Override
	public MethodInfo getMethodInfo() {
		return new MethodInfo(jpfInstruction.getMethodInfo());
	}
	@Override
	public String getFileLocation() {
		return jpfInstruction.getFileLocation();
	}

}
