package se.kth.tracedata.jpf;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Iterable;


import gov.nasa.jpf.vm.ChoiceGenerator;
import se.kth.tracedata.ThreadInfo;
import se.kth.tracedata.jpf.Step;;

public class Transition implements se.kth.tracedata.Transition,Iterable<Step>{
	
	private Step   first, last;
	gov.nasa.jpf.vm.Transition jpfTransition;
	public Transition(gov.nasa.jpf.vm.Transition jpfTransition) {
		this.jpfTransition =jpfTransition;
		
	}
	Iterator<gov.nasa.jpf.vm.Step> jpfiterator = jpfTransition.iterator();
	public Transition(Iterator<gov.nasa.jpf.vm.Step> jpfiterator)
	{
		this.jpfiterator = jpfiterator;
	}
	//ArrayList<Step> mylist = new ArrayList<Step>();
	Iterator<Step> mylist;
	
	
	
	@Override
	public int getThreadIndex() {
		return jpfTransition.getThreadIndex();
	}
	
	
	
	@Override
	public ThreadInfo getThreadInfo() {
		return new se.kth.tracedata.jpf.ThreadInfo(jpfTransition.getThreadInfo());
	}
	@Override
	public ChoiceGenerator<?> getChoiceGenerator() {
		return jpfTransition.getChoiceGenerator();
	}
	@Override
	public Step getStep(int index) {
		return new Step(jpfTransition.getStep(index));
	}
	@Override
	public int getStepCount() {
		return jpfTransition.getStepCount();
	}


	@Override
	public Iterator<Step> iterator() {
		Iterator<Step> stepIter= new Iterator<Step>() {
			
			@Override
			public Step next() {
				
				return new Step(jpfiterator.next());
			}
			
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
		
		/*Iterator<Step> step;
		while(stepIterator.hasNext())
		{
			//step.next() = new Step(jpfiterator.next());
		}*/
		return stepIter;
	}
	
	

}
