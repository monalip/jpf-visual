package kth.se.jpf.Interface;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import gov.nasa.jpf.vm.Transition;


public class PathInterfaceImpl implements PathInterface{
	String             application;  
	  private LinkedList<Transition> stack;
	  
	  private PathInterfaceImpl() {} // for cloning
	  
	  @Override
		public PathInterfaceImpl clone() {
		  PathInterfaceImpl clone = new PathInterfaceImpl();
		  clone.application = application;
		// we need to deep copy the stack to preserve CG and ThreadInfo state
		  LinkedList<Transition> clonedStack = new LinkedList<Transition>();
		  for(Transition t : stack)
		  {
			  clonedStack.add((Transition) t.clone());
		  }
		  clone.stack = clonedStack;
		  
			// TODO Auto-generated method stub
			return clone;
		}

	@Override
	public Iterator<Transition> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printOn(PrintWriter ps) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public String getApplication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transition getLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Transition t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transition get(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return stack.size();
	}

	@Override
	public boolean hasOutput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void printOutputOn(PrintWriter pw) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLast() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Transition> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
