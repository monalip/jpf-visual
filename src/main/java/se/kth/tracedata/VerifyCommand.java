package se.kth.tracedata;

import gov.nasa.jpf.shell.ShellCommand;

public class VerifyCommand extends ShellCommand{

	  private JPF jpf;
	  private boolean error_occured = false;


	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	public JPF getJPF(){
	    if (jpf == null)
	      throw new IllegalStateException("Cannot reference JPF before prepare()");
	    return jpf;
	  }
	public boolean errorOccured() {
		return error_occured;
	}
}
