package se.kth.tracedata;

import java.awt.print.Printable;
import java.io.PrintWriter;
import java.util.Iterator;


//import gov.nasa.jpf.vm.Transition;
import se.kth.tracedata.Transition;
//import kth.se.jpf.Interface.PathInterfaceImpl;

public interface PathInterface extends Cloneable,Iterable<Transition>,Printable{
	

	
	//clone check
	public PathInterface clone();
	
	  
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
