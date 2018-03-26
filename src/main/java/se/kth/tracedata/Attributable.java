package se.kth.tracedata;



public interface Attributable {

	  boolean hasAttr ();
	  boolean hasAttr (Class<?> attrType);
	  Object getAttr();
	  void setAttr (Object a);
	  void addAttr (Object a);
	  void removeAttr (Object a);
	  void replaceAttr (Object oldAttr, Object newAttr);
	  <T> T getAttr (Class<T> attrType);
	  <T> T getNextAttr (Class<T> attrType, Object prev);
	 
	  
	}
