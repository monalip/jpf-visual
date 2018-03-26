package se.kth.tracedata;

import java.util.Iterator;
import java.util.Map;

//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;


public class ClassLoaderInfo implements Iterable<ClassInfo>, Comparable<ClassLoaderInfo>, Cloneable {
	/**
	   * Map from class file URLs to first ClassInfo that was read from it. This search
	   * global map is used to make sure we only read class files once
	   */
	  protected static Map<String,ClassInfo> loadedClasses;
	public static int getNumberOfLoadedClasses (){
		
	    return loadedClasses.size();
	  }

	@Override
	public int compareTo(ClassLoaderInfo o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<ClassInfo> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
