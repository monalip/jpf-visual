package se.kth.shell;

//import gov.nasa.jpf.shell.ShellCommandListener;
import se.kth.shell.ShellCommandListener;
//import gov.nasa.jpf.shell.commands.VerifyCommand;
import se.kth.shell.VerifyCommand;

/**
 * Command listener for the {@link gov.nasa.jpf.shell.commands.VerifyCommand}
 * class. The {@link #preCommand(gov.nasa.jpf.shell.ShellCommand)} executes 
 * before a JPF instance is created. {@link #afterJPFInit(gov.nasa.jpf.JPF) }
 * is run after JPF instance is created but before the {@link gov.nasa.jpf.JPF#run()}
 * method is run. The {@link #postCommand(gov.nasa.jpf.shell.ShellCommand)}
 * method is run after the jpf search comes to end for whatever reason.
 */
public interface VerifyCommandListener extends 
	ShellCommandListener<VerifyCommand>{

  /**
   * Called after the JPF instance is created but, before it is run. This is
   * when publishers and listeners can be added to JPF.
   * @param jpf
   */
  public void afterJPFInit(VerifyCommand command);

	public void exceptionDuringVerify(Exception ex);

}
