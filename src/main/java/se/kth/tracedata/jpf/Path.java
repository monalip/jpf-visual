package se.kth.tracedata.jpf;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

//import gov.nasa.jpf.vm.Transition;
import se.kth.tracedata.jpf.Transition;


public class Path implements se.kth.tracedata.Path  {
	gov.nasa.jpf.vm.Path jpfPath;
	  
	public Path(gov.nasa.jpf.vm.Path jpfPath)
	  {
		  this.jpfPath = jpfPath;
	  }
	  
	  private Path() {} // for cloning
	  
	  public Path (String app) {
	    jpfPath = new Path(app);
	  }
	  
	  @Override
	  public Path clone() {
	    return new Path(jpfPath.clone());
	    // new adapter instance with cloned JPF path
	  }
	  
	  public String getApplication () {
	    return jpfPath.application;
	  }

	  public Transition getLast() {
	    return new Transition(jpfPath.getLast());
	    // create new transition adapter with JPF data inside
	  }

	  public void add (Transition t) {
	    jpfPath.add(t.jpfTransition);
	  }

	  public boolean isEmpty() {
	    return jpfPath.isEmpty();
	  }
	  
	  public int size() {
	    return jpfPath.size();
	  }

	  public boolean hasOutput () {
	    return jpfPath.hasOutput();
	  }
	  
	  public void printOutputOn (PrintWriter pw) {
	    jpfPath.printOutputOn(pw);
	  }
	  
	  public void removeLast () {
	    jpfPath.removeLast();
	  }
	  
	  public Transition get (int pos) {
	    return new Transition(jpfPath.get(pos));
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
