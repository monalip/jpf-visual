package se.kth.tracedata;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

//import gov.nasa.jpf.report.Statistics;
import se.kth.tracedata.Statistics;
//import gov.nasa.jpf.report.Statistics;
//import gov.nasa.jpf.JPF;
import se.kth.tracedata.JPF;
//import gov.nasa.jpf.vm.VM;
import se.kth.tracedata.VM;
import se.kth.tracedata.jpf.Path;
import se.kth.tracedata.Search;




/**
* this is our default report generator, which is heavily configurable
* via our standard properties. Note this gets instantiated and
* registered automatically via JPF.addListeners(), so you don't
* have to add it explicitly
*/
public class Reporter extends SearchListenerAdapter{
	
	  protected Date started, finished;
	public static Logger log = JPF.getLogger("report");
	protected Config conf;
	  protected JPF jpf;
	  protected VM vm;
	  protected Search search;
	  protected Statistics stat; // the object that collects statistics
	  
	  protected List<Publisher> publishers = new ArrayList<Publisher>();
	  
	  public Path getPath (){
		    return vm.getClonedPath();
		  }
	  
	//--- various getters
	  
	  public Date getStartDate() {
	    return started;
	  }

	  public Date getFinishedDate () {
	    return finished;
	  }
	  
	  public String getJPFBanner () {
		    StringBuilder sb = new StringBuilder();
		    
		    sb.append("JavaPathfinder core system v");
		    sb.append(JPF.VERSION);
		    
		    String rev = getRevision();
		    if (rev != null){
		      sb.append(" (rev ");
		      sb.append(rev);
		      sb.append(')');
		    }
		    
		    sb.append(" - (C) 2005-2014 United States Government. All rights reserved.");
		    
		    if (conf.getBoolean("report.show_repository", false)) {
		      String repInfo =  getRepositoryInfo();
		      if (repInfo != null) {
		        sb.append( repInfo);
		      }
		    }
		    
		    return sb.toString();
		  }
	  protected String getRevision() {
		    try {
		      InputStream is = JPF.class.getResourceAsStream(".version");
		      if (is != null){
		        int len = is.available();
		        byte[] data = new byte[len];
		        is.read(data);
		        is.close();
		        return new String(data).trim();
		        
		      } else {
		        return null;
		      }
		      
		    } catch (Throwable t){
		      return null;
		    }
		  }
	  
	  protected String getRepositoryInfo() {
		    try {
		      InputStream is = JPF.class.getResourceAsStream("build.properties");
		      if (is != null){
		        Properties revInfo = new Properties();
		        revInfo.load(is);

		        StringBuffer sb = new StringBuffer();
		        String date = revInfo.getProperty("date");
		        String author = revInfo.getProperty("author");
		        String rev = revInfo.getProperty("rev");
		        String machine = revInfo.getProperty("hostname");
		        String loc = revInfo.getProperty("location");
		        String upstream = revInfo.getProperty("upstream");

		        return String.format("%s %s %s %s %s", date,author,rev,machine,loc);
		      }
		    } catch (IOException iox) {
		      return null;
		    }

		    return null;
		  }
	  public String getUser() {
		    return System.getProperty("user.name");
		  }
	  
	  public String getHostName () {
		    try {
		      InetAddress in = InetAddress.getLocalHost();
		      String hostName = in.getHostName();
		      return hostName;
		    } catch (Throwable t) {
		      return "localhost";
		    }
		  }
	  
	  public String getArch () {
		    String arch = System.getProperty("os.arch");
		    Runtime rt = Runtime.getRuntime();
		    String type = arch + "/" + rt.availableProcessors();

		    return type;
		  }

		  public String getOS () {
		    String name = System.getProperty("os.name");
		    String version = System.getProperty("os.version");
		    return name + "/" + version;
		  }
		  public String getJava (){
			    String vendor = System.getProperty("java.vendor");
			    String version = System.getProperty("java.version");
			    return vendor + "/" + version;
			  }
		  public String getSuT() {
			    return vm.getSUTDescription();
			  }
		  
		  public String getLastSearchConstraint () {
			    return search.getLastSearchConstraint();
			  }
		  public String getCurrentErrorId () {
			    Error e = getCurrentError();
			    if (e != null) {
			      return "#" + e.getId();
			    } else {
			      return "";
			    }
			  }
		  public Error getCurrentError () {
			    return search.getCurrentError();
			  }
		  public VM getVM() {
			    return vm;
			  }
		  public List<Error> getErrors () {
			    return search.getErrors();
			  }
		  /**
		   * in ms
		   */
		  public long getElapsedTime () {
		    Date d = (finished != null) ? finished : new Date();
		    long t = d.getTime() - started.getTime();
		    return t;
		  }
		  public Statistics getStatistics() {
			    return stat;
			  }
		  
		  /*public Statistics getRegisteredStatistics(){
			    
			    if (stat == null){ // none yet, initialize
			      // first, check if somebody registered one explicitly
			      stat = vm.getNextListenerOfType(Statistics.class, null);
			      if (stat == null){
			        stat = conf.getInstance("report.statistics.class@stat", Statistics.class);
			        if (stat == null) {
			          stat = new Statistics();
			        }
			        jpf.addListener(stat);
			      }
			    }
			    
			    return stat;
			  }*/
		  public List<Publisher> getPublishers() {
			    return publishers;
			  }
		  public Statistics getRegisteredStatistics(){
			    
			    if (stat == null){ // none yet, initialize
			      // first, check if somebody registered one explicitly
			      stat = vm.getNextListenerOfType(Statistics.class, null);
			      if (stat == null){
			        stat = conf.getInstance("report.statistics.class@stat", Statistics.class);
			        if (stat == null) {
			          stat = new Statistics();
			        }
			        jpf.addListener(stat);
			      }
			    }
			    
			    return stat;
			  }
			  







}
