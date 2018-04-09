package se.kth.tracedata;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

import gov.nasa.jpf.vm.Transition;

//import gov.nasa.jpf.vm.Transition;

public interface Path extends Cloneable,Iterable<Transition>{
	
			
			//clone check
			public  Path clone();
			
			  
			  public String getApplication () ;
			 // public Transition getLast ();

			 // public void add (Transition t) ;

			  //public Transition get (int pos) ;

			  public boolean isEmpty() ;
			  
			  public int size () ;

			  public boolean hasOutput ();
			  
			  public void printOutputOn (PrintWriter pw);
			
			  public void printOn (PrintWriter pw);

			  public void removeLast () ;
			 
			 public Iterator<Transition> iterator () ;
			  
			  public Iterator<Transition> descendingIterator() ;
			
			  public Transition get (int pos);

	}