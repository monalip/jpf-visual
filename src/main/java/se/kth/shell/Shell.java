package se.kth.shell;

import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//import gov.nasa.jpf.shell.ShellManager;
import se.kth.shell.ShellManager;
//import gov.nasa.jpf.shell.ShellPanel;
import se.kth.shell.ShellPanel;
//import gov.nasa.jpf.JPFShell;
import se.kth.tracedata.JPFShell;
//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;
//import gov.nasa.jpf.shell.ShellFrame;
import se.kth.shell.ShellFrame;
import se.kth.tracedata.Config;

/**
 * The <code>Shell</code> class represents a JPF Shell.<br>
 * Shells contain a collection {@link ShellPanel} and {@link ShellCommand}. It is
 * up to subclasses of Shell to determine how they are represented. 
 * <br> By default once all Shells are disposed of, then a
 * System.exit(0) is called. To disable this behavior set {@link gov.nasa.jpf.shell.ShellManager#SYSTEM_EXIT_ON_SHELL_CLOSE}
 * to false.<br>
 *
 * Although this class extends JFrame there is no swing code in here, that is
 * upto the implementor of this class. (See {@link BasicShell} for an example on
 * what this entails. <br>
 * 
 * There is only 1 single {@link gov.nasa.jpf.Config} that exists for all Shell instances,
 * see {@link gov.nasa.jpf.shell.ShellManager} for more information on how
 * shells interact with resources like the {@link gov.nasa.jpf.Config} instance and error handling.
 *
 * @author Sandro Badame 
 */
public abstract class Shell extends JFrame implements JPFShell, ShellFrame{

  /**
   * The default TITLE for a Shell
   */
  public static final String TITLE = "JPF Shell";

  public static final Image DEFAULT_ICON
          = new ImageIcon(Shell.class.getResource("spiral-of-death-small.png"))
                          .getImage();

  protected String titlePrefix;
  
  private static String getTitleSuffix(Config c){
    String suffix = c.getProperty("jpf.app");
    if (suffix != null) {
      suffix = new File(suffix).getName();
    }else{
      suffix = c.getTarget();
    }
    return " - " + suffix;
  }
  /**
   * Sends a request to the shell that this command be updated
   * @param command  the command to be updated
   */
  public abstract void updateShellCommand(ShellCommand command);
  /**
   * Sends a request to the shell that this panel receive focus, it is upto the
   * the shell implementation to decide what that means. Usually it is the
   * panel requesting to be visible to the user.
   */
  public abstract void requestFocus(ShellPanel panel);
  
  /**
   * Create a shell with a title
   * @param title - the title of this shell
   */
 public Shell(String title){
   super(title);
   titlePrefix = title;
 }
 /**
  * Finds a ShellPanel via {@link #getShellPanel(java.lang.Class)} and then removes
  * it using {@link #removeShellPanel(gov.nasa.jpf.shell.ShellPanel) }
  * @param shellPanelClass
  */
 public void removeShellPanel(Class<? extends ShellPanel> shellPanelClass) {
   ShellPanel t = getShellPanel(shellPanelClass);
   if (t != null) {
     removeShellPanel(t);
   }
 }


 private ArrayList<ShellPanel> panels = new ArrayList<ShellPanel>();

 /**
  * Removes this panel from this shell. The panel is not being moved, it is being
  * disposed of. If this shell will contain no panels after this removal, then
  * the shell is disposed of.
  * @param panel
  */
 public void removeShellPanel(ShellPanel panel) {
   panels.remove(panel);
   panelRemoval(panel);
 }

 /**
  * Searches this shell for the first instance of a panel that is a subclass
  * of the shellPanelClass
  * @param shellPanelClass The class to search for
  * @return The first panel that this shell holds that is an instance of
  * shellPanelClass, null if a panel is not found.
  */
 public <T extends ShellPanel> T getShellPanel(Class<T> shellPanelClass) {
   for (ShellPanel panel : panels) {
     if (panel.getClass().isAssignableFrom(shellPanelClass)) {
       return (T) panel;
     }
   }
   return null;
 }
 private void panelRemoval(ShellPanel panel){
	    if (panels.isEmpty()) {
	      dispose();
	    }
	    panel.removedFromShell();
	  }


  

/**
 * Does the dirty work of adding its self to the ShellManager and various
 * Swing related details.
 */
public final void registerShell(){
  if (!ShellManager.getManager().hasShell(this)) {
    ShellManager.getManager().addShell(this);
    //We will handle when to exit the VM in ShellManager
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //Responsible to report shell closing to the ShellManager
    addWindowListener(new ShellWindowListener());

    setIconImage(DEFAULT_ICON);
  }
  
  
}
/**
 * Installs a command that was registered with the ShellManager onto this
 * shell.
 * @param command the command to be installed.
 */
public abstract void installCommand(ShellCommand command);
/**
 * This class serves as the listener for when the Shells are being closed.
 */
private class ShellWindowListener extends WindowAdapter {

  @Override
  public void windowClosing(WindowEvent e) {
    for (ShellPanel panel : panels) {
      //Check if all of the panels are OK with closing
      if (panel.closing() == false) {
        return;
      }
    }
    setVisible(false);
    dispose();
  }

  @Override
  public void windowClosed(WindowEvent e) {
    for (ShellPanel panel : panels) {
      panel.closed();
    }
  }
}
/**
 * adds a panel to this shell
 * @param panel the panel to be registered
 */
public void addShellPanel(ShellPanel panel){
  panels.add(panel);
  panel.setShell(this);
  panel.addedToShell();
}

/**
 * Displays an error message to the user
 */
public void error(String err) {
  JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
}
/**
 * @return a copy of the list of panels in this Shell.
 */
public  List<ShellPanel> getPanels(){
  return new ArrayList(panels);
}
/**
 * Creates another shell for the case of panel tearing. ie) the Shell that will
 * hold the panel that is being isolated.
 * @return a new shell to hold the panel that is being dragged away.
 */
public abstract Shell createChildShell();
public final void silentlyRemovePanel(ShellPanel panel) {
    panels.remove(panel);
    panel.removedFromShell();
  }
public final void silentlyAddPanel(ShellPanel panel) {
    panels.add(panel);
    panel.addedToShell();
  }

}

