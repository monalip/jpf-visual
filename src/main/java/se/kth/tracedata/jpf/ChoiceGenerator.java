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
	 //boolean method is created to check choicegenerator is instace of ThreadChoiceFromSet
	
	@Override
	public boolean isInstaceofThreadChoiceFromSet() {
		
		return (jpfChoicegen instanceof gov.nasa.jpf.vm.choice.ThreadChoiceFromSet);
	}
	@Override
	public ThreadInfo getChoice(int idx) {
		// TODO Auto-generated method stub
		gov.nasa.jpf.vm.ThreadInfo threainfo = ((gov.nasa.jpf.vm.choice.ThreadChoiceFromSet)jpfChoicegen).getChoice(idx);
		return new ThreadInfo(threainfo);
	}


}
