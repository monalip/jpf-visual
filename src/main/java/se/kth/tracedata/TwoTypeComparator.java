package se.kth.tracedata;

/**
 * like a normal comparator, but comparing heterogenous objects
 */
public interface TwoTypeComparator<T1,T2> {
  int compare (T1 o1, T2 o2);
}