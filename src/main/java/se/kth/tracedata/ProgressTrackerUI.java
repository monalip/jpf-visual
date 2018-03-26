package se.kth.tracedata;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

//import gov.nasa.jpf.report.Statistics;
import se.kth.tracedata.Statistics;
import se.kth.tracedata.JPF;

public class ProgressTrackerUI  implements Runnable {
	protected String application;
	 JPF jpf;
	  Statistics stats;
	  JPFMonitor monitor;
	  long tStart;
	  JLabel[] dataFields;
	
	
	  static final int INTERVAL = 2000; // update every 2sec
	  
	  class JPFMonitor extends Thread {

		    long tLast;
		    
		    public void run() {
		      tLast = System.currentTimeMillis();

		      while (jpf.getStatus() != JPF.Status.DONE){
		        long t = System.currentTimeMillis();
		        long td = t - tLast;
		        if (td > INTERVAL){
		          SwingUtilities.invokeLater(ProgressTrackerUI.this);
		          tLast = t;
		        } else {
		          try {
		            Thread.sleep(INTERVAL - td);
		          } catch (InterruptedException ix){
		            // we don't care
		          }
		        }
		      }
		    }
		  }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public void resetFields(){
	    for (JLabel lbl : dataFields){
	      lbl.setText(null);
	    }
		}
	// note this is called from the thread that runs JPF (not the EventDispatcher)
	  public void attachJPF(JPF jpf){
	    this.jpf = jpf;
	    application = jpf.getConfig().getTarget();
	    stats = jpf.getReporter().getRegisteredStatistics();
	    monitor = new JPFMonitor();
	    
	    monitor.start();
	    tStart = System.currentTimeMillis();
	  }
	

}
