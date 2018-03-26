package se.kth.shell;

//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;

/**
 * This interface is used to listen for when shell commands are executed
 * If the {@link gov.nasa.jpf.shell.ShellCommand#prepare()} method is false
 * then preCommand and postCommand are never executed. Otherwise the these
 * methods wrap around the execution of the command. In order to gain more
 * insight about what stage the command is at at each point, or how have code
 * that needs to be executed at more "intricate" and ShellCommand implementation
 * specific points, check to see if there is a subclass of ShellCommandListener
 * that serves your needs.<br>
 *
 * For details on how to create more intricate ShellCommandListeners for 
 * complicated commands see {@link gov.nasa.jpf.shell.commands.TestCommandListener} or
 * {@link gov.nasa.jpf.shell.commands.VerifyCommandListener}.
 */
public interface ShellCommandListener<E extends ShellCommand> {


  /**
   * Executed directly before a command the command's 
   * {@link gov.nasa.jpf.shell.ShellCommand#execute() } method is executed.
   * @param command the command to be executed
   */
  public void preCommand(E command);

  /**
   * Executed directly after command's
   * {@link gov.nasa.jpf.shell.ShellCommand#execute() } method is executed.
   * @param command the command being executed.
   */
  public void postCommand(E command);
}
