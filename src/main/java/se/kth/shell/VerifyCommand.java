package se.kth.shell;

//import gov.nasa.jpf.JPF;
import se.kth.tracedata.JPF;
//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;

/**
 * The command responsible for starting and running JPF and also for canceling
 * it.
 */
public class VerifyCommand extends ShellCommand{
	
	 private JPF jpf;
	/**
	   * only works after {@link #prepare()} is called.
	   * @return the instance of JPF that is being used to run the verify. 
	   *	     Mostly meant to be used by listeners.
	   */
		private boolean error_occured = false;
	  public JPF getJPF(){
	    if (jpf == null)
	      throw new IllegalStateException("Cannot reference JPF before prepare()");
	    return jpf;
	  }
	  public boolean errorOccured() {
			return error_occured;
		}
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
