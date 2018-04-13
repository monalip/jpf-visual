package se.kth.tracedata.jpf;

public class ChoiceGenerator<T> implements se.kth.tracedata.ChoiceGenerator<T> {
	
	gov.nasa.jpf.vm.ChoiceGenerator<?> jpfChoicegen;
	
	public ChoiceGenerator(gov.nasa.jpf.vm.ChoiceGenerator<?> jpfChoicegen) 
	{
		this.jpfChoicegen= jpfChoicegen;
		
	}

	@Override
	public String getId() {
		return jpfChoicegen.getId();
	}

	@Override
	public int getTotalNumberOfChoices() {
		return jpfChoicegen.getTotalNumberOfChoices();
	}

}
