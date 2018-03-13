package kth.se.jpf.Interface;

import java.io.PrintWriter;
import java.util.Iterator;

import gov.nasa.jpf.util.Printable;
import gov.nasa.jpf.vm.Transition;

public interface PathInterface extends Cloneable,Iterable<Transition>,Printable{
	//clone check
		public PathInterfaceImpl clone();
		
		  
		  public String getApplication () ;
		  public Transition getLast ();

		  public void add (Transition t) ;

		  public Transition get (int pos) ;

		  public boolean isEmpty() ;
		  
		  public int size () ;

		  public boolean hasOutput ();
		  
		  public void printOutputOn (PrintWriter pw);
		
		  //public void printOn (PrintWriter pw);

		  public void removeLast () ;
		 
		 // public Iterator<Transition> iterator () ;
		  
		  public Iterator<Transition> descendingIterator() ;
		

}
