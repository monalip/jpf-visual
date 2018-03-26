package se.kth.shell;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JPanel;

//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;
//import gov.nasa.jpf.shell.ShellPanel;
import se.kth.shell.ShellPanel;
//import gov.nasa.jpf.shell.basicshell.BasicShell;
import se.kth.shell.BasicShell;

/**
 * This is the shell used as a child to the BasicShell it can hold tabs just
 * like BasicShell but does not have a list a of commands across the top.
 * This class is used for when a tab is dragged away from the main BasicShell.
 */
public class BasicChildShell extends BasicShell{

  public BasicChildShell(BasicShell masterShell) {
    super("JPF Child Shell");
    this.masterShell = masterShell;
     this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        List<ShellPanel> panels = getPanels();
        for (int i = panels.size() - 1; i >= 0; i--) {
          removeShellPanel(panels.get(i));
        }
      }
    });
  }

  /**
   * Only the main Shell contains a list of commands to be executed.
   * @param command does nothing, don't even bother.
   */
  @Override
  public void installCommand(final ShellCommand command){}

  /**
   * No need for a tool bar on child shells
   */
  @Override
  protected JPanel createToolBar(){return new JPanel();}



}
