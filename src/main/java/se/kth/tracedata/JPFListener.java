package se.kth.tracedata;

/**
 * this is just a common root type for VMListeners and SearchListeners. No
 * own interface, just a type tag. It's main purpose is to provide some 
 * typechecks during config-based reflection instantiation
 */
public interface JPFListener {
  // see VMListener, SearchListener
}
