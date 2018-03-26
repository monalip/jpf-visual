package se.kth.tracedata.jpf;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import se.kth.tracedata.PathInterface;
//import gov.nasa.jpf.vm.Transition;
import se.kth.tracedata.Transition;


public class Path implements PathInterface  {
	String  application;  
	  private LinkedList<Transition> stack;
	 Path path;
	  
	  public Path(Path path2)
	  {
		  this.path = path2;
	  }
	  
	  private Path() {} // for cloning
	  
	  public Path (String app) {
	    application = app;
	    stack = new LinkedList<Transition>();
	  }
	  
	  @Override
	  public Path clone() {
	    Path clone = new Path();
	   // clone.application = application;
	    
	    // we need to deep copy the stack to preserve CG and ThreadInfo state
	    LinkedList<Transition> clonedStack = new LinkedList<Transition>();
	    for (Transition t : stack){
	      clonedStack.add( (Transition)t.clone());
	    }
	   // clone.stack = clonedStack;
	    
	    return clone;
	  }
	  
	  public String getApplication () {
	    return application;
	  }

	  public Transition getLast () {
	    if (stack.isEmpty()) {
	      return null;
	    } else {
	      return stack.getLast();
	    }
	  }

	  public void add (Transition t) {
	    stack.add(t);
	  }

	  
	 

	  public boolean isEmpty() {
	    return (stack.size() == 0);
	  }
	  
	  public int size () {
	    return stack.size();
	  }

	  public boolean hasOutput () {
	    for (Transition t : stack) {
	      if (t.getOutput() != null) {
	        return true;
	      }
	    }
	    
	    return false;
	  }
	  
	  public void printOutputOn (PrintWriter pw) {
	    for (Transition t : stack) {
	      String output = t.getOutput();
	      if (t != null) {
	        pw.print(output);
	      }
	    }
	  }
	  
	 
	  public void printOn (PrintWriter pw) {
	/**** <2do> this is going away
	    int    length = size;
	    Transition entry;

	    for (int index = 0; index < length; index++) {
	      pw.print("Transition #");
	      pw.print(index);
	      
	      if ((entry = get(index)) != null) {
	        pw.print(' ');

	        entry.printOn(pw);
	      }
	    }
	***/
	  }

	  public void removeLast () {
	    stack.removeLast();
	  }
	  
	  
	  public Transition get (int pos) {
		    return stack.get(pos);
		  }

	@Override
	public Iterator<Transition> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	



	

	@Override
	public Iterator<Transition> descendingIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}

	

	

	

	

	

	
	}
