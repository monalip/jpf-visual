package se.kth.shell;

//import gov.nasa.jpf.shell.ShellCommandListener;
import se.kth.shell.ShellCommandListener;
//import gov.nasa.jpf.shell.commands.TestCommand;
import se.kth.shell.TestCommand;

/**
 * Command listener for the {@link gov.nasa.jpf.shell.TestCommand} class. The
 * {@link preCommand(gov.nasa.jpf.shell.ShellCommand)}
 * and {@link gov.nasa.jpf.shell.ShellCommandListener#postCommand(gov.nasa.jpf.shell.ShellCommand)}
 * methods are executed before and after the command is launched whether the
 * command is going to start or kill the System Under Test (SuT).<br>
 * To be sure that code is only executed directly after the SuT has started use
 * the {@link #applicationStarted(gov.nasa.jpf.shell.commands.TestCommand)} 
 * method. To be sure that code is only executed once a SuT has ended use the
 * {@link #applicationEnded(gov.nasa.jpf.shell.commands.TestCommand)} method.
 * <br>
 * The life cycle of the TestCommand is as follows:
 *  <ol>
 *    <li>User clicks on the TestCommand button on the shell or uses some
 *        other means to execute the
 * {@link gov.nasa.jpf.shell.ShellManager#fireCommand(gov.nasa.jpf.shell.ShellCommand)}
 *        method. Note: This could happen either when the SuT is already
 *	  running (In which case the command will kill the SuT process) or
 *        when the SuT is not running (In which case the command will start
 *        a SuT process.)
 *    </li>
 *    <li>{@link gov.nasa.jpf.shell.ShellCommandListener#preCommand(gov.nasa.jpf.shell.ShellCommand)} is executed.</li>
 *    <li>Either the SuT is started or killed.<br> If it is started then
 *        {@link gov.nasa.jpf.shell.TestCommandListener#applicationStarted(gov.nasa.jpf.shell.TestCommand) }
 *        is executed directly after the process is started.<br>
 *	  Otherwise, if the SuT process is being killed then the process is
 *        killed and the {@link gov.nasa.jpf.shell.TestCommandListener#applicationEnded(gov.nasa.jpf.shell.TestCommand) }
 *        method is executed.
 *     </li>
 *    <li>{@link gov.nasa.jpf.shell.ShellCommandListener#postCommand(gov.nasa.jpf.shell.ShellCommand)} is executed.</li>
 *  </ol>
 */
public interface TestCommandListener extends ShellCommandListener{

  /**
   * Called whenever the SuT has been started.
   * @param command The command responsible for starting the SuT
   */
  void applicationStarted(TestCommand command);

  /**
   * Called whenever the SuT has terminated.
   * @param command The command responsible for terminating the SuT
   */
  void applicationEnded(TestCommand command);
}
