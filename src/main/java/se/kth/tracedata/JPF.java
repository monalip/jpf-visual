package se.kth.tracedata;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//import gov.nasa.jpf.ConfigChangeListener;
import se.kth.tracedata.ConfigChangeListener;
//import gov.nasa.jpf.report.Statistics;
import se.kth.tracedata.Statistics;
//import gov.nasa.jpf.util.Misc;
import se.kth.tracedata.Misc;
//import gov.nasa.jpf.JPFConfigException;
import se.kth.tracedata.JPFConfigException;
//import gov.nasa.jpf.report.Reporter;
import se.kth.tracedata.Reporter;
//import gov.nasa.jpf.util.JPFLogger;
import se.kth.tracedata.JPFLogger;
//import gov.nasa.jpf.util.LogManager;
import se.kth.tracedata.LogManager;
//import gov.nasa.jpf.Config;
import se.kth.tracedata.Config;
//import gov.nasa.jpf.JPFListener;
import se.kth.tracedata.JPFListener;
//import gov.nasa.jpf.search.Search;
import se.kth.tracedata.Search;
//import gov.nasa.jpf.search.SearchListener;
import se.kth.tracedata.SearchListener;
//import gov.nasa.jpf.vm.VM;
import se.kth.tracedata.VM;
//import gov.nasa.jpf.vm.VMListener;
import se.kth.tracedata.VMListener;





//import gov.nasa.jpf.util.LogManager; we donot need it

public class JPF implements Runnable{
	  /** we use this as safety margin, to be released upon OutOfMemoryErrors */
	  byte[] memoryReserve;

	  
	 public enum Status { NEW, RUNNING, DONE };
	 Status status = Status.NEW;
	  /** Reference to the virtual machine used by the search */
	  VM vm;
	  /** The search policy used to explore the state space */
	  Search search;
	  /** a list of listeners that get automatically added from VM, Search or Reporter initialization */
	  List<VMListener> pendingVMListeners;
	  List<SearchListener> pendingSearchListeners;

	
	  /** this is the backbone of all JPF configuration */
	  Config config;
	/** the report generator */
	  Reporter reporter;

	 /* use this one to get a Logger that is initialized via our Config mechanism. Note that
	   * our own Loggers do NOT pass
	   */
	  //public static JPFLogger getLogger (String name) {  JPFLogger internaly extending Logger class hence we are calling the logger class directly 
	//without nasa dependecy
	 /**
	   * use this one to get a Logger that is initialized via our Config mechanism. Note that
	   * our own Loggers do NOT pass
	   */
	  public static JPFLogger getLogger (String name) {
	    return LogManager.getLogger( name);
	  }
		  public Reporter getReporter () {
			    return reporter;
			  }
		  public Config getConfig() {
			    return config;
			  }
		  public Status getStatus() {
			    return status;
			  }
		  
		  /**
		   * return the search object. This can be null if the initialization has failed
		   */
		  public Search getSearch() {
		    return search;
		  }
		  /**
		   * return a Config object that holds the JPF options. This first
		   * loads the properties from a (potentially configured) properties file, and
		   * then overlays all command line arguments that are key/value pairs
		   */
		  public static Config createConfig (String[] args) {
		    return new Config(args);
		  }
		  
		  protected void addPendingVMListener (VMListener l){
			    if (pendingVMListeners == null){
			      pendingVMListeners = new ArrayList<VMListener>();
			    }
			    pendingVMListeners.add(l);
			  }
		  protected void addPendingSearchListener (SearchListener l){
			    if (pendingSearchListeners == null){
			      pendingSearchListeners = new ArrayList<SearchListener>();
			    }
			    pendingSearchListeners.add(l);
			  }
		  /**
		   * return the search object. This can be null if the initialization has failed
		   */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		/**
		   * create new JPF object. Note this is always guaranteed to return, but the
		   * Search and/or VM object instantiation might have failed (i.e. the JPF
		   * object might not be really usable). If you directly use getSearch() / getVM(),
		   * check for null
		   */
		  public JPF(Config conf) {
		    config = conf;

		    setNativeClassPath(config); // before we do anything else

		    if (logger == null) { // maybe somebody created a JPF object explicitly
		      logger = initLogging(config);
		    }

		    initialize();
		  }
		  static void setNativeClassPath(Config config) {
			    if (!config.hasSetClassLoader()) {
			      config.initClassLoader( JPF.class.getClassLoader());
			    }
			  }
		  private static Logger initLogging(Config conf) {
			    LogManager.init(conf);
			    return getLogger("se.kth.tracedata");
			  }
		  private void initialize() {
			    VERSION = config.getString("jpf.version", VERSION);
			    memoryReserve = new byte[config.getInt("jpf.memory_reserve", 64 * 1024)]; // in bytes
			    
			    try {
			      
			      Class<?>[] vmArgTypes = { JPF.class, Config.class };
			      Object[] vmArgs = { this, config };
			      vm = config.getEssentialInstance("vm.class", VM.class, vmArgTypes, vmArgs);

			      Class<?>[] searchArgTypes = { Config.class, VM.class };
			      Object[] searchArgs = { config, vm };
			      search = config.getEssentialInstance("search.class", Search.class,
			                                                searchArgTypes, searchArgs);

			      // although the Reporter will always be notified last, this has to be set
			      // first so that it can register utility listeners like Statistics that
			      // can be used by configured listeners
			      Class<?>[] reporterArgTypes = { Config.class, JPF.class };
			      Object[] reporterArgs = { config, this };
			      reporter = config.getInstance("report.class", Reporter.class, reporterArgTypes, reporterArgs);
			      if (reporter != null){
			    	  search.setReporter(reporter);
			      }
			      
			      addListeners();
			      
			      config.addChangeListener(new ConfigListener());
			      
			    } catch (JPFConfigException cx) {
			      logger.severe(cx.toString());
			      //cx.getCause().printStackTrace();      
			      throw new ExitException(false, cx);
			    }
			  }
		  /**
		   * this is called after vm, search and reporter got instantiated
		   */
		  void addListeners () {
		    Class<?>[] argTypes = { Config.class, JPF.class };
		    Object[] args = { config, this };

		    // first listeners that were automatically added from VM, Search and Reporter initialization
		    if (pendingVMListeners != null){
		      for (VMListener l : pendingVMListeners) {
		       vm.addListener(l);
		      }      
		      pendingVMListeners = null;
		    }
		    
		    if (pendingSearchListeners != null){
		      for (SearchListener l : pendingSearchListeners) {
		       search.addListener(l);
		      }
		      pendingSearchListeners = null;
		    }
		    
		    // and finally everything that's user configured
		    List<JPFListener> listeners = config.getInstances("listener", JPFListener.class, argTypes, args);
		    if (listeners != null) {
		      for (JPFListener l : listeners) {
		        addListener(l);
		      }
		    }
		  }
		  public void addListener (JPFListener l) {    
			    // the VM is instantiated first
			    if (l instanceof VMListener) {
			      if (vm != null) {
			        vm.addListener( (VMListener) l);
			        
			      } else { // no VM yet, we are in VM initialization
			        addPendingVMListener((VMListener)l);
			      }
			    }
			    
			    if (l instanceof SearchListener) {
			      if (search != null) {
			        search.addListener( (SearchListener) l);
			        
			      } else { // no search yet, we are in Search initialization
			        addPendingSearchListener((SearchListener)l);
			      }
			    }
			  }
		  public static String VERSION = "8.0"; // the major version number

		  static Logger logger     = null; // initially

		 

		  class ConfigListener implements ConfigChangeListener {
		    
		    /**
		     * check for new listeners that are dynamically configured
		     */
		    @Override
		    public void propertyChanged(Config config, String key, String oldValue, String newValue) {
		      if ("listener".equals(key)) {
		        if (oldValue == null)
		          oldValue = "";
		        
		        String[] nv = config.asStringArray(newValue);
		        String[] ov = config.asStringArray(oldValue);
		        String[] newListeners = Misc.getAddedElements(ov, nv);
		        Class<?>[] argTypes = { Config.class, JPF.class };          // Many listeners have 2 parameter constructors
		        Object[] args = {config, JPF.this };
		        
		        if (newListeners != null) {
		          for (String clsName : newListeners) {
		            try {
		              JPFListener newListener = config.getInstance("listener", clsName, JPFListener.class, argTypes, args);
		              addListener(newListener);
		              logger.info("config changed, added listener " + clsName);

		            } catch (JPFConfigException cfx) {
		              logger.warning("listener change failed: " + cfx.getMessage());
		            }
		          }
		        }
		      }
		    }
		    public void addListener (JPFListener l) {    
		        // the VM is instantiated first
		        if (l instanceof VMListener) {
		          if (vm != null) {
		            vm.addListener( (VMListener) l);
		            
		          } else { // no VM yet, we are in VM initialization
		            addPendingVMListener((VMListener)l);
		          }
		        }
		        
		        if (l instanceof SearchListener) {
		          if (search != null) {
		            search.addListener( (SearchListener) l);
		            
		          } else { // no search yet, we are in Search initialization
		            addPendingSearchListener((SearchListener)l);
		          }
		        }
		      }

			@Override
			public void jpfRunTerminated(Config conf) {
				// TODO Auto-generated method stub
				
			}
		    

		  


		  

		  }
		  /**
		   * private helper class for local termination of JPF (without killing the
		   * whole Java process via System.exit).
		   * While this is basically a bad non-local goto exception, it seems to be the
		   * least of evils given the current JPF structure, and the need to terminate
		   * w/o exiting the whole Java process. If we just do a System.exit(), we couldn't
		   * use JPF in an embedded context
		   */
		  @SuppressWarnings("serial")
		  public static class ExitException extends RuntimeException {
		    boolean report = true;
		    
		    ExitException() {}
		    
		    ExitException (boolean report, Throwable cause){
		      super(cause);
		      
		      this.report = report;
		    }
		    
		    ExitException(String msg) {
		      super(msg);
		    }
		    
		    public boolean shouldReport() {
		      return report;
		    }
		  }
		

}
