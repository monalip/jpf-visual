package se.kth.tracedata;

/**
 * create an instance of type E out of a T instance
 */

public interface ElementCreator<T,E> {
	  E create (T o);
	}
