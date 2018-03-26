package se.kth.tracedata;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * classloader that is used by Config to instantiate from JPF configured
 * paths. This is a standard parent-first loader to avoid multiple class
 * instances when using our Run*.jar tools
 *
 * The main reason for having our own classloader is dynamically configured resource
 * and library lookup
 */
public class JPFClassLoader extends URLClassLoader {

  String[] nativeLibs;


  static {
    //ClassLoader.registerAsParallelCapable(); // for jdk7
  }
  
  public JPFClassLoader (URL[] urls){
    super(urls);
  }

  public JPFClassLoader (URL[] urls, String[] libs, ClassLoader parent){
    super(urls, parent);

    nativeLibs = libs;
  }

  @Override
  protected String findLibrary (String libBaseName){

    if (nativeLibs != null){
      String libName = File.separator + System.mapLibraryName(libBaseName);

      for (String libPath : nativeLibs) {
        if (libPath.endsWith(libName)) {
          return libPath;
        }
      }
    }

    return null; // means VM uses java.library.path to look it up
  }

  /**
   * we make it public since we add paths dynamically during JPF init
   * 
   * Note this is ignored according to the javadocs if the provided url is already in the classpath.
   * We do rely on this feature since me might add jpf.jar several times during bootstrap
   */
  @Override
  public void addURL (URL url){
    if (url != null){
      super.addURL(url);
    }
  }
  
  public void setNativeLibs (String[] libs){
    nativeLibs = libs;
  }
}
