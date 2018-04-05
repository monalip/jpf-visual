package se.kth.tracedata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

//import gov.nasa.jpf.ConfigChangeListener;
import se.kth.tracedata.ConfigChangeListener;
//import gov.nasa.jpf.JPFClassLoader;
import se.kth.tracedata.JPFClassLoader;
//import gov.nasa.jpf.util.FileUtils;
import se.kth.tracedata.FileUtils;
import se.kth.tracedata.JPF.ConfigListener;
//import gov.nasa.jpf.util.JPFSiteUtils;
import se.kth.tracedata.JPFSiteUtils;
//import gov.nasa.jpf.JPFException;
import se.kth.tracedata.JPFException;
//import gov.nasa.jpf.JPFConfigException;
import se.kth.tracedata.JPFConfigException;



@SuppressWarnings("serial")
public class Config extends Properties {
	 ArrayList<ConfigChangeListener> changeListeners;
	
	  // Properties are simple Hashmaps, but we want to maintain the order of entries
	  LinkedList<String> entrySequence = new LinkedList<String>();
	 // do we want to log the config init
	  public static boolean log = false;
	  public static final String LIST_SEPARATOR = ",";
	
	static final String[] EMPTY_STRING_ARRAY = new String[0];
	  static final String MAX = "MAX";
	  static final char[] DELIMS = { ',', ';' };
	  public static final String TRUE = "true";
	  public static final String FALSE = "false";
	  // where did we initialize from
	  ArrayList<Object> sources = new ArrayList<Object>();
	  public static final Class<?>[] CONFIG_ARGTYPES = { Config.class }; 
	  public final Object[] CONFIG_ARGS = { this };
	  // an [optional] hashmap to keep objects we want to be singletons
	  HashMap<String,Object> singletons;
	  
	  // it seems bad design to keep ClassLoader management in a glorified Properties object,
	  // but a lot of what Config does is to resolve configured types, for which we need
	  // control over the loader that is used for resolution
	  ClassLoader loader = Config.class.getClassLoader();
	  public static final Class<?>[] NO_ARGTYPES = new Class<?>[0];
	  public static final Object[] NO_ARGS = new Object[0];
	  
	  
	public String getString(String key) {
	    return getProperty(key);
	  }
	/**
	   * our own version of split, which handles "`" quoting, and breaks on non-quoted
	   * ',' and ';' chars. We need this so that we can use ';' separated lists in
	   * JPF property files, but still can use quoted ';' if we absolutely have to
	   * specify Java signatures. On the other hand, we can't quote with '\' because
	   * that would make Windows paths even more terrible.
	   * regexes are bad at quoting, and this is more efficient anyways
	   */
	  protected String[] split (String input){
	    return split(input, DELIMS);
	  }
	  protected String[] split (String input, char[] delim){
		    int n = input.length();
		    ArrayList<String> elements = new ArrayList<String>();
		    boolean quote = false;

		    char[] buf = new char[128];
		    int k=0;

		    for (int i=0; i<n; i++){
		      char c = input.charAt(i);

		      if (!quote) {
		        if (isDelim(delim,c)){ // element separator
		          elements.add( new String(buf, 0, k));
		          k = 0;
		          continue;
		        } else if (c=='`') {
		          quote = true;
		          continue;
		        }
		      }

		      if (k >= buf.length){
		        char[] newBuf = new char[buf.length+128];
		        System.arraycopy(buf, 0, newBuf, 0, k);
		        buf = newBuf;
		      }
		      buf[k++] = c;
		      quote = false;
		    }

		    if (k>0){
		      elements.add( new String(buf, 0, k));
		    }

		    return elements.toArray(new String[elements.size()]);
		  }
	  private boolean isDelim(char[] delim, char c){
		    for (int i=0; i<delim.length; i++){
		      if (c == delim[i]){
		        return true;
		      }
		    }
		    return false;
		  }

	public String[] getStringArray(String key, String[] def){
	    String v = getProperty(key);
	    if (v != null && (v.length() > 0)) {
	      return split(v);
	    } else {
	      return def;
	    }
	  }
	public boolean getBoolean(String key, boolean def) {
	    String v = getProperty(key);
	    if (v != null) {
	      return (v == TRUE);
	    } else {
	      return def;
	    }
	  }
	public TreeMap<Object,Object> asOrderedMap() {
	    TreeMap<Object,Object> map = new TreeMap<Object,Object>();
	    map.putAll(this);
	    return map;
	  }
	public List<Object> getSources() {
	    return sources;
	  }
	
	public String getSourceName (Object src){
	    if (src instanceof File){
	      return ((File)src).getAbsolutePath();
	    } else if (src instanceof URL){
	      return ((URL)src).toString();
	    } else {
	      return src.toString();
	    }
	  }
	
	public String getTarget(){
	    return getString("target");
	  }
	 public <T> ArrayList<T> getInstances(String key, Class<T> type) throws JPFConfigException {

		    Class<?>[] argTypes = { Config.class };
		    Object[] args = { this };

		    return getInstances(key,type,argTypes,args);
		  }
	 public <T> ArrayList<T> getInstances(String key, Class<T> type, Class<?>[]argTypes, Object[] args)
             throws JPFConfigException {
			Class<?>[] c = getClasses(key);
			
			if (c != null) {
			String[] ids = getIds(key);
			
			ArrayList<T> a = new ArrayList<T>(c.length);
			
			for (int i = 0; i < c.length; i++) {
			String id = (ids != null) ? ids[i] : null;
			T listener = getInstance(key, c[i], type, argTypes, args, id);
			if (listener != null) {
			a.add( listener);
			} else {
			// should report here
			}
			}
			
			return a;
			
			} else {
			// should report here
			}
			
			return null;
			}
	 public Class<?>[] getClasses(String key) throws JPFConfigException {
		    String[] v = getStringArray(key);
		    if (v != null) {
		      int n = v.length;
		      Class<?>[] a = new Class[n];
		      for (int i = 0; i < n; i++) {
		        String clsName = expandClassName(v[i]);
		        if (clsName != null && clsName.length() > 0){
		          try {
		            clsName = stripId(clsName);
		            a[i] = loader.loadClass(clsName);
		          } catch (ClassNotFoundException cnfx) {
		            throw new JPFConfigException("class not found " + v[i]);
		          } catch (ExceptionInInitializerError ix) {
		            throw new JPFConfigException("class initialization of " + v[i] + " failed: " + ix, ix);
		          }
		        }
		      }

		      return a;
		    }

		    return null;
		  }
	 public String[] getStringArray(String key) {
		    String v = getProperty(key);
		    if (v != null && (v.length() > 0)) {
		      return split(v);
		    }

		    return null;
		  }

		    String expandClassName (String clsName) {
		        if (clsName != null && clsName.length() > 0 && clsName.charAt(0) == '.') {
		          return  clsName;
		        } else {
		          return clsName;
		        }
		      }
		    
		 // <2do> - that's kind of kludged together, not very efficient
		    String[] getIds (String key) {
		      String v = getProperty(key);

		      if (v != null) {
		        int i = v.indexOf('@');
		        if (i >= 0) { // Ok, we have ids
		          String[] a = split(v);
		          String[] ids = new String[a.length];
		          for (i = 0; i<a.length; i++) {
		            ids[i] = getId(a[i]);
		          }
		          return ids;
		        }
		      }

		      return null;
		    }
		    
		    public <T> T getInstance(String key, Class<T> type, String defClsName) throws JPFConfigException {
		        Class<?>[] argTypes = CONFIG_ARGTYPES;
		        Object[] args = CONFIG_ARGS;

		        Class<?> cls = getClass(key);
		        String id = getIdPart(key);

		        if (cls == null) {
		          try {
		            cls = loader.loadClass(defClsName);
		          } catch (ClassNotFoundException cfx) {
		            throw new JPFConfigException("class not found " + defClsName);
		          } catch (ExceptionInInitializerError ix) {
		            throw new JPFConfigException("class initialization of " + defClsName + " failed: " + ix, ix);
		          }
		        }
		        
		        return getInstance(key, cls, type, argTypes, args, id);
		      }
		    public <T> T getInstance(String key, Class<T> type, Class<?>[] argTypes,
                    Object[] args) throws JPFConfigException {
			Class<?> cls = getClass(key);
			String id = getIdPart(key);
			
			if (cls != null) {
			return getInstance(key, cls, type, argTypes, args, id);
			} else {
			return null;
			}

		    }
		    
		    
		    /**
		     * return an [optional] id part of a property value (all that follows the first '@')
		     */
		    String getIdPart (String key) {
		      String v = getProperty(key);
		      if ((v != null) && (v.length() > 0)) {
		        int i = v.indexOf('@');
		        if (i >= 0){
		          return v.substring(i+1);
		        }
		      }

		      return null;
		    }
		    public Class<?> getClass(String key) throws JPFConfigException {
		        return asClass( getProperty(key));
		      }
		      
		    public Class<?> asClass (String v) throws JPFConfigException {
		        if ((v != null) && (v.length() > 0)) {
		          v = stripId(v);
		          v = expandClassName(v);
		          try {
		            return loader.loadClass(v);
		          } catch (ClassNotFoundException cfx) {
		            throw new JPFConfigException("class not found " + v + " by classloader: " + loader);
		          } catch (ExceptionInInitializerError ix) {
		            throw new JPFConfigException("class initialization of " + v + " failed: " + ix,
		                ix);
		          }
		        }

		        return null;    
		      }
		    String stripId (String v) {
		        int i = v.indexOf('@');
		        if (i >= 0) {
		          return v.substring(0,i);
		        } else {
		          return v;
		        }
		      }
		    String getId (String v){
		        int i = v.indexOf('@');
		        if (i >= 0) {
		          return v.substring(i+1);
		        } else {
		          return null;
		        }
		      }
		    
		    /**
		     * this is our private instantiation workhorse - try to instantiate an object of
		     * class 'cls' by using the following ordered set of ctors 1. <cls>(
		     * <argTypes>) 2. <cls>(Config) 3. <cls>() if all of that fails, or there was
		     * a 'type' provided the instantiated object does not comply with, return null
		     */
		    <T> T getInstance(String key, Class<?> cls, Class<T> type, Class<?>[] argTypes,
		                       Object[] args, String id) throws JPFConfigException {
		      Object o = null;
		      Constructor<?> ctor = null;

		      if (cls == null) {
		        return null;
		      }

		      if (id != null) { // check first if we already have this one instantiated as a singleton
		        if (singletons == null) {
		          singletons = new HashMap<String,Object>();
		        } else {
		          o = type.cast(singletons.get(id));
		        }
		      }

		      while (o == null) {
		        try {
		          ctor = cls.getConstructor(argTypes);
		          o = ctor.newInstance(args);
		        } catch (NoSuchMethodException nmx) {
		           
		          if ((argTypes.length > 1) || ((argTypes.length == 1) && (argTypes[0] != Config.class))) {
		            // fallback 1: try a single Config param
		            argTypes = CONFIG_ARGTYPES;
		            args = CONFIG_ARGS;

		          } else if (argTypes.length > 0) {
		            // fallback 2: try the default ctor
		            argTypes = NO_ARGTYPES;
		            args = NO_ARGS;

		          } else {
		            // Ok, there is no suitable ctor, bail out
		            throw new JPFConfigException(key, cls, "no suitable ctor found");
		          }
		        } catch (IllegalAccessException iacc) {
		          throw new JPFConfigException(key, cls, "\n> ctor not accessible: "
		              + getMethodSignature(ctor));
		        } catch (IllegalArgumentException iarg) {
		          throw new JPFConfigException(key, cls, "\n> illegal constructor arguments: "
		              + getMethodSignature(ctor));
		        } catch (InvocationTargetException ix) {
		          Throwable tx = ix.getTargetException();
		          if (tx instanceof JPFConfigException) {
		            throw new JPFConfigException(tx.getMessage() + "\n> used within \"" + key
		                + "\" instantiation of " + cls);
		          } else {
		            throw new JPFConfigException(key, cls, "\n> exception in "
		                + getMethodSignature(ctor) + ":\n>> " + tx, tx);
		          }
		        } catch (InstantiationException ivt) {
		          throw new JPFConfigException(key, cls,
		              "\n> abstract class cannot be instantiated");
		        } catch (ExceptionInInitializerError eie) {
		          throw new JPFConfigException(key, cls, "\n> static initialization failed:\n>> "
		              + eie.getException(), eie.getException());
		        }
		      }

		      // check type
		      if (!type.isInstance(o)) {
		        throw new JPFConfigException(key, cls, "\n> instance not of type: "
		            + type.getName());
		      }

		      if (id != null) { // add to singletons (in case it's not already in there)
		        singletons.put(id, o);
		      }

		      return type.cast(o); // safe according to above
		    }

		    public String getMethodSignature(Constructor<?> ctor) {
		      StringBuilder sb = new StringBuilder(ctor.getName());
		      sb.append('(');
		      Class<?>[] argTypes = ctor.getParameterTypes();
		      for (int i = 0; i < argTypes.length; i++) {
		        if (i > 0) {
		          sb.append(',');
		        }
		        sb.append(argTypes[i].getName());
		      }
		      sb.append(')');
		      return sb.toString();
		    }
		    public int getInt(String key) {
		        return getInt(key, 0);
		      }
		    public int getInt(String key, int defValue) {
		        String v = getProperty(key);
		        if (v != null) {
		          if (MAX.equals(v)){
		            return Integer.MAX_VALUE;
		          } else {
		            try {
		              return Integer.parseInt(v);
		            } catch (NumberFormatException nfx) {
		              throw new JPFConfigException("illegal int element in '" + key + "' = \"" + v + '"');
		            }
		          }
		        }

		        return defValue;
		      }
		    /**
		     * this one is used to instantiate objects from a list of keys that share
		     * the same prefix, e.g.
		     * 
		     *  shell.panels = config,site
		     *  shell.panels.site = .shell.panels.SitePanel
		     *  shell.panels.config = .shell.panels.ConfigPanel
		     *  ...
		     * 
		     * note that we specify default class names, not classes, so that the classes
		     * get loaded through our own loader at call time (they might not be visible
		     * to our caller)
		     */
		    public <T> T[] getGroupInstances (String keyPrefix, String keyPostfix, Class<T> type, 
		            String... defaultClsNames) throws JPFConfigException {
		      
		      String[] ids = getCompactTrimmedStringArray(keyPrefix);
		      
		      if (ids.length > 0){
		        keyPrefix = keyPrefix + '.';
		        T[] arr = (T[]) Array.newInstance(type, ids.length);
		        
		        for(int i = 0; i < ids.length; i++){
		          String key = keyPrefix + ids[i];
		          if (keyPostfix != null){
		            key = key + keyPostfix;
		          }
		          arr[i] = getEssentialInstance(key, type);
		        }
		        
		        return arr;
		        
		      } else {
		        T[] arr = (T[]) Array.newInstance(type, defaultClsNames.length);
		                
		        for (int i=0; i<arr.length; i++){
		          arr[i] = getInstance((String)null, defaultClsNames[i], type);
		          if (arr[i] == null){
		            exception("cannot instantiate default type " + defaultClsNames[i]);
		          }
		        }
		        
		        return arr;
		      }
		    }
		    public String[] getCompactTrimmedStringArray (String key){
		        String[] a = getStringArray(key);

		        if (a != null) {
		          for (int i = 0; i < a.length; i++) {
		            String s = a[i];
		            if (s != null && s.length() > 0) {
		              a[i] = s.trim();
		            }
		          }

		          return removeEmptyStrings(a);

		        } else {
		          return EMPTY_STRING_ARRAY;
		        }
		      }
		    public <T> T getEssentialInstance(String key, Class<T> type) throws JPFConfigException {
		        Class<?>[] argTypes = { Config.class };
		        Object[] args = { this };
		        return getEssentialInstance(key, type, argTypes, args);
		      }
		    
		    public static String[] removeEmptyStrings (String[] a){
		        if (a != null) {
		          int n = 0;
		          for (int i=0; i<a.length; i++){
		            if (a[i].length() > 0){
		              n++;
		            }
		          }

		          if (n < a.length){ // we have empty strings in the split
		            String[] r = new String[n];
		            for (int i=0, j=0; i<a.length; i++){
		              if (a[i].length() > 0){
		                r[j++] = a[i];
		                if (j == n){
		                  break;
		                }
		              }
		            }
		            return r;

		          } else {
		            return a;
		          }
		        }

		        return null;
		      }
		    
		    public JPFConfigException exception (String msg) {
		        String context = getString("config");
		        if (context != null){
		          msg = "error in " + context + " : " + msg;
		        }

		        return new JPFConfigException(msg);
		      }

		      public void throwException(String msg) {
		        throw new JPFConfigException(msg);
		      }
		      
		      public <T> T getInstance (String id, String clsName, Class<T> type) throws JPFConfigException {
		    	    Class<?>[] argTypes = CONFIG_ARGTYPES;
		    	    Object[] args = CONFIG_ARGS;

		    	    Class<?> cls = asClass(clsName);
		    	    
		    	    if (cls != null) {
		    	      return getInstance(id, cls, type, argTypes, args, id);
		    	    } else {
		    	      return null;
		    	    }
		    	  }
		      
		      public <T> T getEssentialInstance(String key, Class<T> type, Class<?>[] argTypes, Object[] args) throws JPFConfigException {
		    	    Class<?> cls = getEssentialClass(key);
		    	    String id = getIdPart(key);

		    	    return getInstance(key, cls, type, argTypes, args, id);
		    	  }
		      
		      public Class<?> getEssentialClass(String key) throws JPFConfigException {
		    	    Class<?> cls = getClass(key);
		    	    if (cls == null) {
		    	      throw new JPFConfigException("no classname entry for: \"" + key + "\"");
		    	    }

		    	    return cls;
		    	  }
		      public <T> T getInstance(String key, Class<T> type) throws JPFConfigException {
		    	    Class<?>[] argTypes = CONFIG_ARGTYPES;
		    	    Object[] args = CONFIG_ARGS;

		    	    return getInstance(key, type, argTypes, args);
		    	  }
		      public String getString(String key, String defValue) {
		    	    String s = getProperty(key);
		    	    if (s != null) {
		    	      return s;
		    	    } else {
		    	      return defValue;
		    	    }
		    	  }
		      // the original command line args that were passed into the constructor
		      String[] args;
		      
		      // non-property/option command line args (starting from the first arg that is not prepened by '-','+')
		      String[] freeArgs;
		      
		      /**
		       * the standard Config constructor that processes the whole properties stack
		       */
		      public Config (String[] cmdLineArgs)  {
		        args = cmdLineArgs;
		        String[] a = cmdLineArgs.clone(); // we might nullify some of them

		        // we need the app properties (*.jpf) pathname upfront because it might define 'site'
		        String appProperties = getAppPropertiesLocation(a);

		        //--- the site properties
		        String siteProperties = getSitePropertiesLocation( a, appProperties);
		        if (siteProperties != null){
		          loadProperties( siteProperties);
		        }

		        //--- get the project properties from current dir + site configured extensions
		        loadProjectProperties();

		        //--- the application properties
		        if (appProperties != null){
		          loadProperties( appProperties);
		        }

		        //--- at last, the (rest of the) command line properties
		        loadArgs(a);

		        // note that global path collection now happens from initClassLoader(), to
		        // accommodate for deferred project initialization when explicitly setting Config entries

		        //printEntries();
		      }
		      
		      protected boolean loadProperties (String fileName) {
		    	    if (fileName != null && fileName.length() > 0) {
		    	      FileInputStream is = null;
		    	      try {
		    	        File f = new File(fileName);
		    	        if (f.isFile()) {
		    	          log("loading property file: " + fileName);

		    	          setConfigPathProperties(f.getAbsolutePath());
		    	          sources.add(f);
		    	          is = new FileInputStream(f);
		    	          load(is);
		    	          return true;
		    	        } else {
		    	          throw exception("property file does not exist: " + f.getAbsolutePath());
		    	        }
		    	      } catch (MissingRequiredKeyException rkx){
		    	        // Hmpff - control exception
		    	        log("missing required key: " + rkx.getMessage() + ", skipping: " + fileName);
		    	      } catch (IOException iex) {
		    	        throw exception("error reading properties: " + fileName);
		    	      } finally {
		    	        if (is != null){
		    	          try {
		    	            is.close();
		    	          } catch (IOException iox1){
		    	            log("error closing input stream for file: " + fileName);
		    	          }
		    	        }
		    	      }
		    	    }

		    	    return false;
		    	  }
		      
		      String getAppPropertiesLocation(String[] args){
		    	    String path = null;

		    	    path = getPathArg(args, "app");
		    	    if (path == null){
		    	      // see if the first free arg is a *.jpf
		    	      path = getAppArg(args);
		    	    }
		    	    
		    	    put("jpf.app", path);

		    	    return path;
		    	  }
		      
		      String getSitePropertiesLocation(String[] args, String appPropPath){
		    	    String path = getPathArg(args, "site");

		    	    if (path == null){
		    	      // look into the app properties
		    	      // NOTE: we might want to drop this in the future because it constitutes
		    	      // a cyclic properties file dependency
		    	      if (appPropPath != null){
		    	        path = JPFSiteUtils.getMatchFromFile(appPropPath,"site");
		    	      }

		    	      if (path == null) {
		    	        File siteProps = JPFSiteUtils.getStandardSiteProperties();
		    	        if (siteProps != null){
		    	          path = siteProps.getAbsolutePath();
		    	        }
		    	      }
		    	    }
		    	    
		    	    put("jpf.site", path);

		    	    return path;
		    	  }

		      /*
		       * note that matching args are expanded and stored here, to avoid any
		       * discrepancy with value expansions (which are order-dependent)
		       */
		      protected String getPathArg (String[] args, String key){
		        int keyLen = key.length();

		        for (int i=0; i<args.length; i++){
		          String a = args[i];
		          if (a != null){
		            int len = a.length();
		            if (len > keyLen + 2){
		              if (a.charAt(0) == '+' && a.charAt(keyLen+1) == '='){
		                if (a.substring(1, keyLen+1).equals(key)){
		                  String val = expandString(key, a.substring(keyLen+2));
		                  args[i] = null; // processed
		                  return val;
		                }
		              }
		            }
		          }
		        }

		        return null;
		      }
		      
		      
		      // our internal expander
		      // Note that we need to know the key this came from, to handle recursive expansion
		      protected String expandString (String key, String s) {
		        int i, j = 0;
		        if (s == null || s.length() == 0) {
		          return s;
		        }

		        while ((i = s.indexOf("${", j)) >= 0) {
		          if ((j = s.indexOf('}', i)) > 0) {
		            String k = s.substring(i + 2, j);
		            String v;
		            
		            if ((key != null) && key.equals(k)) {
		              // that's expanding itself -> use what is there
		              v = getProperty(key);
		            } else {
		              // refers to another key, which is already expanded, so this
		              // can't get recursive (we expand during entry storage)
		              v = getProperty(k);
		            }
		            
		            if (v == null) { // if we don't have it, fall back to system properties
		              v = System.getProperty(k);
		            }
		            
		            if (v != null) {
		              s = s.substring(0, i) + v + s.substring(j + 1, s.length());
		              j = i + v.length();
		            } else {
		              s = s.substring(0, i) + s.substring(j + 1, s.length());
		              j = i;
		            }
		          }
		        }

		        return s;    
		      }
		      
		      /*
		       * if the first freeArg is a JPF application property filename, use this
		       * as targetArg and set the "jpf.app" property accordingly
		       */
		      protected String getAppArg (String[] args){

		        for (int i=0; i<args.length; i++){
		          String a = args[i];
		          if (a != null && a.length() > 0){
		            switch (a.charAt(0)) {
		              case '+': continue;
		              case '-': continue;
		              default:
		                if (a.endsWith(".jpf")){
		                  String val = expandString("jpf.app", a);
		                  args[i] = null; // processed
		                  return val;
		                }
		            }
		          }
		        }

		        return null;
		      }
		      
		      /**
		       * this holds the policy defining in which order we process directories
		       * containing JPF projects (i.e. jpf.properties files)
		       */
		      protected void loadProjectProperties () {
		        // this is the list of directories holding jpf.properties files that
		        // have to be processed in order of entry (increasing priority)
		        LinkedList<File> jpfDirs = new LinkedList<File>();

		        // deduce the JPF projects in use (at least jpf-core) from the CL which
		        // defined this class
		        addJPFdirsFromClasspath(jpfDirs);

		        // add all the site configured extension dirs (but NOT jpf-core)
		        addJPFdirsFromSiteExtensions(jpfDirs);

		        // add the current dir, which has highest priority (this might bump up
		        // a previous entry by reodering it - which includes jpf-core)
		        addCurrentJPFdir(jpfDirs);

		        // now load all the jpf.property files we found in these dirs
		        // (later loads can override previous settings)
		        for (File dir : jpfDirs){
		          loadProperties(new File(dir,"jpf.properties").getAbsolutePath());
		        }
		      }
		      /*
		       * argument syntax:
		       *          {'+'<key>['='<val>'] | '-'<driver-arg>} {<free-arg>}
		       *
		       * (1) null cmdLineArgs are ignored
		       * (2) all config cmdLineArgs start with '+'
		       * (3) if '=' is ommitted, a 'true' value is assumed
		       * (4) if <val> is ommitted, a 'null' value is assumed
		       * (5) no spaces around '='
		       * (6) all '-' driver-cmdLineArgs are ignored
		       */

		      protected void loadArgs (String[] cmdLineArgs) {

		        for (int i=0; i<cmdLineArgs.length; i++){
		          String a = cmdLineArgs[i];

		          if (a != null && a.length() > 0){
		            switch (a.charAt(0)){
		              case '+': // Config arg
		                processArg(a.substring(1));
		                break;

		              case '-': // driver arg, ignore
		                continue;

		              default:  // free (non property/option) cmdLineArgs to follow

		                int n = cmdLineArgs.length - i;
		                freeArgs = new String[n];
		                System.arraycopy(cmdLineArgs, i, freeArgs, 0, n);

		                return;
		            }
		          }
		        }
		      }
		      protected void setConfigPathProperties (String fileName){
		    	    put("config", fileName);
		    	    int i = fileName.lastIndexOf(File.separatorChar);
		    	    if (i>=0){
		    	      put("config_path", fileName.substring(0,i));
		    	    } else {
		    	      put("config_path", ".");
		    	    }
		    	  }
		      
		      public void log (String msg){
		    	    if (log){ // very simplisitc, but we might do more in the future
		    	      System.out.println(msg);
		    	    }
		    	  }
		   // bad - a control exception
		      static class MissingRequiredKeyException extends RuntimeException {
		        MissingRequiredKeyException(String details){
		          super(details);
		        }
		      }
		      protected void addJPFdirsFromClasspath(List<File> jpfDirs) {
		    	    String cp = System.getProperty("java.class.path");
		    	    String[] cpEntries = cp.split(File.pathSeparator);

		    	    for (String p : cpEntries) {
		    	      File f = new File(p);
		    	      File dir = f.isFile() ? getParentFile(f) : f;

		    	      addJPFdirs(jpfDirs, dir);
		    	    }
		    	  }
		      protected void addJPFdirsFromSiteExtensions (List<File> jpfDirs){
		    	    String[] extensions = getCompactStringArray("extensions");
		    	    if (extensions != null){
		    	      for (String pn : extensions){
		    	        addJPFdirs( jpfDirs, new File(pn));
		    	      }
		    	    }
		    	  }
		      
		      /**
		       * add the current dir to the list of JPF components.
		       * Note: this includes the core, so that we maintain the general
		       * principle that the enclosing project takes precedence (imagine the opposite:
		       * if we want to test a certain feature that is overridden by another extension
		       * we don't know about)
		       */
		      protected void addCurrentJPFdir(List<File> jpfDirs){
		        File dir = new File(System.getProperty("user.dir"));
		        while (dir != null) {
		          File jpfProp = new File(dir, "jpf.properties");
		          if (jpfProp.isFile()) {
		            registerJPFdir(jpfDirs, dir);
		            return;
		          }
		          dir = getParentFile(dir);
		        }
		      }
		      /*
		       * this does not include the '+' prefix, just the 
		       *     <key>[=[<value>]]
		       */
		      protected void processArg (String a) {

		        int idx = a.indexOf("=");

		        if (idx == 0){
		          throw new JPFConfigException("illegal option: " + a);
		        }

		        if (idx > 0) {
		          String key = a.substring(0, idx).trim();
		          String val = a.substring(idx + 1).trim();

		          if (val.length() == 0){
		            val = null;
		          }

		          setProperty(key, val);

		        } else {
		          setProperty(a.trim(), "true");
		        }

		      }
		      
		      protected void addJPFdirs (List<File> jpfDirs, File dir){
		    	    while (dir != null) {
		    	      File jpfProp = new File(dir, "jpf.properties");
		    	      if (jpfProp.isFile()) {
		    	        registerJPFdir(jpfDirs, dir);
		    	        return;       // we probably don't want recursion here
		    	      }
		    	      dir = getParentFile(dir);
		    	    }
		    	  }
		      
		      static File root = new File(File.separator);
		      protected File getParentFile(File f){
		    	    if (f == root){
		    	      return null;
		    	    } else {
		    	      File parent = f.getParentFile();
		    	      if (parent == null){
		    	        parent = new File(f.getAbsolutePath());

		    	        if (parent.getName().equals(root.getName())) {
		    	          return root;
		    	        } else {
		    	          return parent;
		    	        }
		    	      } else {
		    	        return parent;
		    	      }
		    	    }
		    	  }
		      
		      public String[] getCompactStringArray(String key){
		    	    return removeEmptyStrings(getStringArray(key));
		    	  }
		      
		      /**
		       * the obvious part is that it only adds to the list if the file is absent
		       * the not-so-obvious part is that it re-orders already present files
		       * to maintain the priority
		       */
		      protected boolean registerJPFdir(List<File> list, File dir){
		        try {
		          dir = dir.getCanonicalFile();

		          for (File e : list) {
		            if (e.equals(dir)) {
		              list.remove(e);
		              list.add(e);
		              return false;
		            }
		          }
		        } catch (IOException iox) {
		          throw new JPFConfigException("illegal path spec: " + dir);
		        }
		        
		        list.add(dir);
		        return true;
		      }
		      

		      public boolean hasSetClassLoader (){
		    	    return Config.class.getClassLoader() != loader;
		    	  }
		      public JPFClassLoader initClassLoader (ClassLoader parent) {
		    	    ArrayList<String> list = new ArrayList<String>();

		    	    // we prefer to call this here automatically instead of allowing
		    	    // explicit collectGlobalPath() calls because (a) this could not preserve
		    	    // initial path settings, and (b) setting it *after* the JPFClassLoader got
		    	    // installed won't work (would have to add URLs explicitly, or would have
		    	    // to create a new JPFClassLoader, which then conflicts with classes already
		    	    // defined by the previous one)
		    	    collectGlobalPaths();
		    	    if (log){
		    	      log("collected native_classpath=" + get("native_classpath"));
		    	      log("collected native_libraries=" + get("native_libraries"));
		    	    }


		    	    String[] cp = getCompactStringArray("native_classpath");
		    	    cp = FileUtils.expandWildcards(cp);
		    	    for (String e : cp) {
		    	      list.add(e);
		    	    }
		    	    URL[] urls = FileUtils.getURLs(list);

		    	    String[] nativeLibs = getCompactStringArray("native_libraries");

		    	    JPFClassLoader cl;
		    	    if (parent instanceof JPFClassLoader){ // no need to create a new one, just initialize
		    	      cl = (JPFClassLoader)parent;
		    	      for (URL url : urls){
		    	        cl.addURL(url);
		    	      }
		    	      cl.setNativeLibs(nativeLibs);
		    	      
		    	    } else {    
		    	      cl = new JPFClassLoader( urls, nativeLibs, parent);
		    	    }
		    	    
		    	    loader = cl;
		    	    return cl;
		    	  }
		      /**
		       * collect all the <project>.{native_classpath,classpath,sourcepath,peer_packages,native_libraries}
		       * and append them to the global settings
		       *
		       * NOTE - this is now called from within initClassLoader, which should only happen once and
		       * is the first time we really need the global paths.
		       *
		       * <2do> this is Ok for native_classpath and native_libraries, but we should probably do
		       * classpath, sourcepath and peer_packages separately (they can be collected later)
		       */
		      public void collectGlobalPaths() {
		            
		        // note - this is in the order of entry, i.e. reflects priorities
		        // we have to process this in reverse order so that later entries are prioritized
		        String[] keys = getEntrySequence();

		        String nativeLibKey = "." + System.getProperty("os.name") +
		                '.' + System.getProperty("os.arch") + ".native_libraries";

		        for (int i = keys.length-1; i>=0; i--){
		          String k = keys[i];
		          if (k.endsWith(".native_classpath")){
		            appendPath("native_classpath", k);
		            
		          } else if (k.endsWith(".classpath")){
		            appendPath("classpath", k);
		            
		          } else if (k.endsWith(".sourcepath")){        
		            appendPath("sourcepath", k);
		            
		          } else if (k.endsWith("peer_packages")){
		            append("peer_packages", getString(k), ",");
		            
		          } else if (k.endsWith(nativeLibKey)){
		            appendPath("native_libraries", k);
		          }
		        }
		      }
		      
		      static Pattern absPath = Pattern.compile("(?:[a-zA-Z]:)?[/\\\\].*");
		      void appendPath (String pathKey, String key){
		    	    String projName = key.substring(0, key.indexOf('.'));
		    	    String pathPrefix = null;

		    	    if (projName.isEmpty()){
		    	      pathPrefix = new File(".").getAbsolutePath();
		    	    } else {
		    	      pathPrefix = getString(projName);
		    	    }

		    	    if (pathPrefix != null){
		    	      pathPrefix += '/';

		    	      String[] elements = getCompactStringArray(key);
		    	      if (elements != null){
		    	        for (String e : elements) {
		    	          if (e != null && e.length()>0){

		    	            // if this entry is not an absolute path, or doesn't start with
		    	            // the project path, prepend the project path
		    	            if (!(absPath.matcher(e).matches()) && !e.startsWith(pathPrefix)) {
		    	              e = pathPrefix + e;
		    	            }

		    	            append(pathKey, e);
		    	          }
		    	        }
		    	      }

		    	    } else {
		    	      //throw new JPFConfigException("no project path for " + key);
		    	    }
		    	  }
		      protected String append (String key, String value) {
		    	    return append(key, value, LIST_SEPARATOR); // append with our standard list separator
		    	  }
		      
		    //------------------------------ public methods - the Config API


		      public String[] getEntrySequence () {
		        // whoever gets this might add/append/remove items, so we have to
		        // avoid ConcurrentModificationExceptions
		        return entrySequence.toArray(new String[entrySequence.size()]);
		      }

		      public String append (String key, String value, String separator) {
		        String oldValue = getProperty(key);
		        value = normalize( expandString(key, value));

		        append0(key, oldValue, oldValue, value, separator);

		        return oldValue;
		      }
		      /**
		       * replace string constants with global static objects
		       */
		      protected String normalize (String v) {
		        if (v == null){
		          return null; // ? maybe TRUE - check default loading of "key" or "key="
		        }

		        // trim leading and trailing blanks (at least Java 1.4.2 does not take care of trailing blanks)
		        v = v.trim();
		        
		        // true/false
		        if ("true".equalsIgnoreCase(v)
		            || "yes".equalsIgnoreCase(v)
		            || "on".equalsIgnoreCase(v)) {
		          v = TRUE;
		        } else if ("false".equalsIgnoreCase(v)
		            || "no".equalsIgnoreCase(v)
		            || "off".equalsIgnoreCase(v)) {
		          v = FALSE;
		        }

		        // nil/null
		        if ("nil".equalsIgnoreCase(v) || "null".equalsIgnoreCase(v)){
		          v = null;
		        }
		        
		        return v;
		      }
		      
		      private void append0 (String key, String oldValue, String a, String b, String separator){
		    	    String newValue;

		    	    if (a != null){
		    	      if (b != null) {
		    	        StringBuilder sb = new StringBuilder(a);
		    	        if (separator != null) {
		    	          sb.append(separator);
		    	        }
		    	        sb.append(b);
		    	        newValue = sb.toString();

		    	      } else { // b==null : nothing to append
		    	        if (oldValue == a){ // using reference compare is intentional here
		    	          return; // no change
		    	        } else {
		    	          newValue = a;
		    	        }
		    	      }

		    	    } else { // a==null : nothing to append to
		    	      if (oldValue == b || b == null){  // using reference compare is intentional here
		    	        return; // no change
		    	      } else {
		    	        newValue = b;
		    	      }
		    	    }

		    	    // if we get here, we have a newValue that differs from oldValue
		    	    put0(key, newValue);
		    	    notifyPropertyChangeListeners(key, oldValue, newValue);
		    	  }
		      private Object put0 (String k, Object v){
		    	    entrySequence.add(k);
		    	    return super.put(k, v);
		    	  }
		      protected void notifyPropertyChangeListeners (String key, String oldValue, String newValue) {
		    	    if (changeListeners != null) {
		    	      for (ConfigChangeListener l : changeListeners) {
		    	        l.propertyChanged(this, key, oldValue, newValue);
		    	      }
		    	    }    
		    	  }



		      public String[] asStringArray (String s){
		    	    return split(s);
		    	  }


		      public <T> T getInstance (String id, String clsName, Class<T> type, Class<?>[] argTypes, Object[] args) throws JPFConfigException {
		    	    Class<?> cls = asClass(clsName);
		    	    
		    	    if (cls != null) {
		    	      return getInstance(id, cls, type, argTypes, args, id);
		    	    } else {
		    	      return null;
		    	    }
		    	  }
		      public void addChangeListener (ConfigChangeListener l) {
		    	    if (changeListeners == null) {
		    	      changeListeners = new ArrayList<ConfigChangeListener>();
		    	      changeListeners.add(l);
		    	    } else {
		    	      if (!changeListeners.contains(l)) {
		    	        changeListeners.add(l);
		    	      }
		    	    }
		    	  }

		      public String[] getTargetArgs(){
		    	    String[] a = getStringArray("target.args");
		    	    if (a == null){
		    	      return new String[0];
		    	    } else {
		    	      return a;
		    	    }
		    	  }


		    
		    
		    





			
	  
	
	

}
