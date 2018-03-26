package se.kth.tracedata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

//import gov.nasa.jpf.JPF;
import se.kth.tracedata.JPF;



public class Source {
	 static List<SourceRoot> sourceRoots;
	  static Hashtable<String,Source> sources = new Hashtable<String,Source>();
	  static Source noSource = new Source(null, null);
	  static Logger logger = JPF.getLogger("se.kth.tracedata.Source");
	//--- the Source instance data itself
	  protected SourceRoot root;
	  protected String     fname;
	  protected String[]   lines;

	  static abstract class SourceRoot { // common base
		    abstract InputStream getInputStream (String fname);
		  }

	  protected Source (SourceRoot root, String fname) {
	    this.root = root;
	    this.fname = fname;
	  }
	public static Source getSource (String relPathName) {
	    if (relPathName == null){
	      return null;
	    }
	    
	    Source s = sources.get(relPathName);
	    if (s == noSource) {
	       return null;
	    }

	    if (s == null) {
	      for (SourceRoot root : sourceRoots) {
	        InputStream is = root.getInputStream(relPathName);
	        if (is != null) {
	          try {
	          s = new Source(root,relPathName);
	          s.loadLines(is);
	          is.close();

	          sources.put(relPathName, s);
	          return s;
	          } catch (IOException iox) {
	            logger.warning("error reading " + relPathName + " from" + root);
	            return null;
	          }
	        }
	      }
	    } else {
	      return s;
	    }

	    sources.put(relPathName, noSource);
	    return null;
	  }
	protected void loadLines (InputStream is) throws IOException {
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));

	    ArrayList<String> l = new ArrayList<String>();
	    for (String line = in.readLine(); line != null; line = in.readLine()) {
	      l.add(line);
	    }
	    in.close();

	    if (l.size() > 0) {
	      lines = l.toArray(new String[l.size()]);
	    }
	  }
	/**
	   * this is our sole purpose in life - answer line strings
	   * line index is 1-based
	   */
	  public String getLine (int i) {
	    if ((lines == null) || (i <= 0) || (i > lines.length)) {
	      return null;
	    } else {
	      return lines[i-1];
	    }
	  }



}
