package se.kth.tracedata.jpf;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import gov.nasa.jpf.vm.Transition;
//import se.kth.tracedata.jpf.Transition;


public class Path implements se.kth.tracedata.Path{
	
	 gov.nasa.jpf.vm.Path jpfpath;
	 String             application;  
	  private LinkedList<Transition> stack;
	  
	  private Path() {} // for cloning
	  
	  public Path(Path path2) {
		  
	  } // for cloning
	  
	 
	  public Path (gov.nasa.jpf.vm.Path jpfpath) {
		  	
		    this.jpfpath = jpfpath; 
		  }
	  public Path (String app) {
		    jpfpath = new gov.nasa.jpf.vm.Path(app);
		  }
	  
	public Path clone() {
		  	    
		    return new Path(jpfpath.clone());
	  }
	  public String getApplication () {
	    return(jpfpath.getApplication());
	  }

	  public Transition getLast () {
		  return(jpfpath.getLast());
	  }

	  public void add (Transition t) {
	    stack.add(t);
	  }

	  
	 

	  public boolean isEmpty() {
	   return jpfpath.isEmpty();
	  }
	  
	  public int size () {
	    return jpfpath.size();
	  }

	  public boolean hasOutput () {
	    return jpfpath.hasOutput();
	  }
	  
	  public void printOutputOn (PrintWriter pw) {
	    jpfpath.printOn(pw);
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
		  jpfpath.removeLast();
	  }
	  
	  
	  public Transition get (int pos) {
		    return stack.get(pos);
		  }

	public Iterator<Transition> iterator() {
		return stack.iterator();
	}

	

	public Iterator<Transition> descendingIterator() {
		return stack.descendingIterator();
	}

	


	
	}