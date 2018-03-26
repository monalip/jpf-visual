package se.kth.tracedata;


public class ClassInfo {
	
	protected static int nClassInfos; // for statistics
	  /**
	   * Name of the class. e.g. "java.lang.String"
	   * NOTE - this is the expanded name for builtin types, e.g. "int", but NOT
	   * for arrays, which are for some reason in Ldot notation, e.g. "[Ljava.lang.String;" or "[I"
	   */
	  protected String name;
	  /** this is only set if the classfile has a SourceFile class attribute */
	  protected String sourceFileName;
	/**
	   * required by InfoObject interface
	   */
	  public ClassInfo getClassInfo() {
	    return this;
	  }
	  /**
	   * Returns the name of the class.  e.g. "java.lang.String".  similar to
	   * java.lang.Class.getName().
	   */
	  public String getName () {
	    return name;
	  }
	  public String getSourceFileName () {
		    return sourceFileName;
		  }
	  
	  public static int getNumberOfLoadedClasses(){
		    return nClassInfos;
		  }
		  


}
