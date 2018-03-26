package se.kth.tracedata;

//import gov.nasa.jpf.vm.ClassInfo;
import se.kth.tracedata.ClassInfo;
//import gov.nasa.jpf.vm.ElementInfo;
import se.kth.tracedata.ElementInfo;

//import gov.nasa.jpf.vm.ThreadInfo;
import se.kth.tracedata.ThreadInfo;

/**
 * this is our implementation independent model of the heap
 */
public interface Heap extends Iterable<ElementInfo> {

  //--- this is the common heap client API

  ElementInfo get (int objref);
  ElementInfo getModifiable (int objref);

  void gc();

  boolean isOutOfMemory();

  void setOutOfMemory(boolean isOutOfMemory);

  //--- the allocator primitives
  ElementInfo newArray (String elementType, int nElements, ThreadInfo ti);
  ElementInfo newObject (ClassInfo ci, ThreadInfo ti);
  
  ElementInfo newSystemArray (String elementType, int nElements, ThreadInfo ti, int anchor);
  ElementInfo newSystemObject (ClassInfo ci, ThreadInfo ti, int anchor);

  //--- convenience allocators that avoid constructor calls
  // (those are mostly used for their reference values since they already have initialized fields,
  // but to keep it consistent we use ElementInfo return types)
  ElementInfo newString (String str, ThreadInfo ti);
  ElementInfo newSystemString (String str, ThreadInfo ti, int anchor);
  
  ElementInfo newInternString (String str, ThreadInfo ti);
  
  ElementInfo newSystemThrowable (ClassInfo ci, String details, int[] stackSnapshot, int causeRef,
                          ThreadInfo ti, int anchor);
  
  Iterable<ElementInfo> liveObjects();

  int size();

  //--- system internal interface


  //void updateReachability( boolean isSharedOwner, int oldRef, int newRef);

  void markThreadRoot (int objref, int tid);

  void markStaticRoot (int objRef);

  // these update per-object counters - object will be gc'ed if it goes to zero
  void registerPinDown (int objRef);
  void releasePinDown (int objRef);

  void unmarkAll();

  void cleanUpDanglingReferences();

  boolean isAlive (ElementInfo ei);

  void registerWeakReference (ElementInfo ei);

  // to be called from ElementInfo.markRecursive(), to avoid exposure of
  // mark implementation
  void queueMark (int objref);

  boolean hasChanged();


  // <2do> this will go away
  void markChanged(int objref);

  void resetVolatiles();

  void restoreVolatiles();

  void checkConsistency (boolean isStateStore);


}
