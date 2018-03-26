package se.kth.shell;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;
//import gov.nasa.jpf.shell.ShellCommandListener;
import se.kth.shell.ShellCommandListener;
//import gov.nasa.jpf.shell.ShellManager;
import se.kth.shell.ShellManager;
//import gov.nasa.jpf.shell.basicshell.images.JPFShell_Images.StatusIcon;
import static se.kth.shell.images.JPFShell_Images.*;
//import gov.nasa.jpf.shell.commands.TestCommand;
import se.kth.shell.TestCommand;
//import gov.nasa.jpf.shell.commands.VerifyCommand;
import se.kth.shell.VerifyCommand;
//import gov.nasa.jpf.shell.listeners.TestCommandListener;
import se.kth.shell.TestCommandListener;

/**
 * A JPanel that lays along the bottom of the BasicShell displaying icons that
 * indicate the status of various ShellCommands (Right now just {@link gov.nasa.jpf.shell.commands.TestCommand}
 * and {@link gov.nasa.jpf.shell.commands.VerifyCommand}).  
 */
public class StatusPanel extends JPanel implements ShellCommandListener, TestCommandListener{

  //Verify
  private Icon verifyStatusOn = getStatusIcon(StatusIcon.VerifyOn, "Currently Verifying...");
  private Icon verifyStatusOff = getStatusIcon(StatusIcon.VerifyOff, "");
  private JLabel verifyStatusLabel = new JLabel(verifyStatusOff);

  //Test
  private Icon testStatusOn = getStatusIcon(StatusIcon.TestOn, "Currently Running the System Under Test...");
  private Icon testStatusOff = getStatusIcon(StatusIcon.TestOff, "");
  private JLabel testStatusLabel = new JLabel(testStatusOff);

  public StatusPanel(){
    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    setBorder(BorderFactory.createLoweredBevelBorder());
  }

  /**
   * Listens for this VerifyCommand and updates this panel's states as it
   * detects changes in the state of the command.
   * @param command the verify command to listen to
   */
  public void addCommand(VerifyCommand command){
    add(verifyStatusLabel);
    ShellManager.getManager().addCommandListener(command, this);
  }

  /**
   * Listens for this TestComman and updates this panel's states as it
   * detects changes in the state of the command.
   * @param command the test command to listen to
   */
  void addCommand(TestCommand command){
    add(testStatusLabel);
    ShellManager.getManager().addCommandListener(command, (TestCommandListener)this);
  }

  /*----------------------- These are for the VerifyCommand ------------------*/
  public void preCommand(ShellCommand command) {
    verifyStatusLabel.setIcon(verifyStatusOn);
  }

  public void postCommand(ShellCommand command) {
    verifyStatusLabel.setIcon(verifyStatusOff);
  }

  /* ------------- These are for the TestCommand -----------------------------*/
  public void applicationStarted(TestCommand command) {
     testStatusLabel.setIcon(testStatusOn);
  }

  public void applicationEnded(TestCommand command) {
     testStatusLabel.setIcon(testStatusOff);
  }
}
