package se.kth.tracedata;

//import gov.nasa.jpf.Config;
import se.kth.tracedata.Config;

/**
 * listener for gov.nasa.jpf.Config changes. Implementors
 * can register themselves upon initialization, to react to
 * downstream changes even if they cache or process Config
 * settings for increased performance.
 * 
 * the notification is per-key
 */
public interface ConfigChangeListener {

  /**
   * a JPF property was changed during runtime (e.g. by using the Verify API
   * or encountering annotations)
   */
  void propertyChanged (Config conf, String key, String oldValue, String newValue);
  
  /**
   *  this can be used to let a config listener remove itself, which is
   *  required if the same Config object is used for several JPF runs
   */
  void jpfRunTerminated (Config conf);
}
