package se.kth.shell;

import java.awt.Component;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//import gov.nasa.jpf.shell.util.LinkDestination;
import se.kth.shell.LinkDestination;
//import gov.nasa.jpf.shell.Shell;
import se.kth.shell.Shell;
//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;
//import gov.nasa.jpf.shell.ShellCommandListener;
import se.kth.shell.ShellCommandListener;
import se.kth.tracedata.Config;
public class ShellManager {
	
	//Stream to outside program
	  private PrintWriter ideOut = null;
	  //Holds a list of all commands that are currently registered
	  private ArrayList<ShellCommand> commands = new ArrayList<ShellCommand>();
	//Holds this config for this set of shells.
	  private Config config;
		private String[] startingArgs;

	  //Logging
	  private static final Logger shellLog = Logger.getLogger("se.kth.shell");
	  static {
	    //We don't want the error being reported twice in the ErrorPanel
	    shellLog.setUseParentHandlers(false);
	    shellLog.setLevel(Level.INFO);
	  }
	//Holds a mapping of ShellCommandClasses to their sclisteners.
	  private HashMap<Class<? extends ShellCommand>, List<ShellCommandListener>> 
		  classlisteners = new HashMap<Class<? extends ShellCommand>,
		                               List<ShellCommandListener>>();
	//Holds the singleton instance of ShellManager
	  private static ShellManager manager;
	//Holds all of the known shells
	  private ArrayList<WeakReference<Shell>> shells =
		  new ArrayList<WeakReference<Shell>>();

	
	/**
	   * @throws IllegalStateException if no ShellManager has been created
	   * @return the single <code>ShellMonitor</code> instance
	   */
	  public static ShellManager getManager(){
	    if (manager == null){
	      Thread.dumpStack();
	      throw new IllegalStateException("No ShellManager exists");
	    }
	    return manager;
	  }
	  
	 
	  /**
	   * Adds the listener to all commands that are a subclass of the <code>commandClass</code>
	   * @param commandClass - the ShellCommand class to listen for
	   * @param listener - The listener to be executed when the ShellCommand executes
	   */
	  public void addCommandListener(Class<? extends ShellCommand> commandClass,
	                                 ShellCommandListener listener ){
	    if (!classlisteners.containsKey(commandClass)) {
	      classlisteners.put(commandClass, new ArrayList<ShellCommandListener>());
	    }
	    classlisteners.get(commandClass).add(listener);
	  }


	  /**
	   * Notifies all shells that a command wishes to be updated. This should be
	   * used to notify Shells to update how they display commands because the
	   * icon, tool-tip or name of the command changed.
	   * @param command the command that needs to be updated
	   */
	  public void updateCommand(ShellCommand command){
	    for (WeakReference<Shell> r : shells) {
	      Shell shell = r.get();
	      if (shell != null){
	        shell.updateShellCommand(command);
	      }
	    }
	  }
	  /**
	   * This method that should be called to launch/fire/execute a command. It first
	   * calls {@link gov.nasa.jpf.shell.ShellCommand#prepare()} to first determine
	   * whether to continue and notify the sclisteners. If prepare() is true then
	   * {@link gov.nasa.jpf.shell.ShellCommandListener#preCommand(ShellCommand)}
	   * is executed for all sclisteners to this command. The command is then executed via
	   * {@link gov.nasa.jpf.shell.ShellCommand#execute()}. All the sclisteners are then
	   * once again notified about the completion of the command through
	   * {@link gov.nasa.jpf.shell.ShellCommandListener#postCommand(ShellCommand)}
	   *
	   * @param command the command who's sclisteners and execute method will be fired.
	   */
	  public void fireCommand(ShellCommand command){
			if (command.prepare()){
	      List<ShellCommandListener> sclisteners = getCommandListeners(command);
	      for (ShellCommandListener scl : sclisteners) { 
	        try{
	          scl.preCommand(command);
	        }catch(Exception e){
	          getLogger().log(Level.SEVERE, "Error in preCommand", e);
	        }
	      }
				command.execute();
	      for (ShellCommandListener scl : sclisteners) {
	        try{
	          scl.postCommand(command);
	        }catch(Exception e){
	          getLogger().log(Level.SEVERE, "Error in postCommand", e);
	        }
	      }
			}
	  }
	  public List<ShellCommandListener> getCommandListeners(ShellCommand c){
		    List<ShellCommandListener> list = new ArrayList<ShellCommandListener>();
		    for (Class ls : classlisteners.keySet()) {
		      if (ls.isAssignableFrom(c.getClass())) {
		        list.addAll(classlisteners.get(ls));
		      }
		    }
		    return list;
		  }
	  public static Logger getLogger(){
		    return shellLog;
		  }
	  
	  /**
	   * Adds a listener for the command
	   * @param command the command to listen for
	   * @param listener the listener to register for the command
	   */
	  public void addCommandListener(ShellCommand command,
	                                 ShellCommandListener listener){
	    addCommandListener(command.getClass(), listener);
	  }

	  
	  public Config getConfig(){
	    return config;
	  }
	  public boolean hasShell(Shell aThis) {
		    for (WeakReference<Shell> shell : shells) {
		      if (shell.get() == aThis) {
		        return true;
		      }
		    }
		    return false;
		  }
	  /**
	   * Adds a Shell to this ShellManager. Once a Shell is added all commands that
	   * have been added to the manager are installed onto the shell.
	   * @param shell the shell to be added, does nothing if the shell has already
	   *	    been added to the manager
	   */
	  public void addShell(Shell shell){
	    //Check if this shell is already added
	    for (WeakReference<Shell> r : shells) {
	      if(shell.equals(r.get())){
	        return;
	      }
	    }

	    shells.add(new WeakReference(shell));

	    for (ShellCommand command : commands) {
	      if (command != null) {
	        shell.installCommand(command);
	      }
	    }
	  }
	  /**
		 * @return all of the command sclisteners of a certain type
		 */
		public <E extends ShellCommandListener> List<E> getCommandListeners(Class<? extends ShellCommand> commandType, Class<E> listenerType){
			ArrayList<E> arrayList = new ArrayList<E>();

	    for (Class<? extends ShellCommand> class1 : classlisteners.keySet()) {
	      if (class1.isAssignableFrom(commandType)) {
	        for (ShellCommandListener shellCommandListener : classlisteners.get(class1)) {
	          if (listenerType.isAssignableFrom(shellCommandListener.getClass())) {
	            arrayList.add((E) shellCommandListener);
	          }
	        }
	      }
	    }

			return arrayList;
		}


		/**
		   * Attempts to open the given file in an editor.<br>
		   * If there is already a connection made to the editor through the use of the
		   * "shell.port" property then the following string is printed to it:<br>
		   * <code>[LINK] <i>path</i>:<i>line</i></code><br>
		   * Where path is the absolute path to the file and line is the line number of
		   * of the file starting with 1.<br>
		   * If there is no connection made to an editor then if "shell.editor" is
		   * defined its value is executed as a command with "#{file}" replaced by 
		   * the absolute path of the file and "#{linenumber}" is replaced by the line
		   * number in the file starting with 1
		   * @param d the destination of the link
		   */
		  public void printLinkCommand(LinkDestination d){
		    if (ideOut != null){
		      ideOut.println("[LINK]" + d );
		    }else{
		      String e = getConfig().getProperty("shell.editor", "");
		      if (e.isEmpty() == false){
		        e = e.replace("#{file}", d.path);
		        e = e.replace("#{linenumber}", String.valueOf(d.line));
		        try {
		          Runtime.getRuntime().exec(e);
		        } catch (IOException ex) {
		          getLogger().severe("Command: \"" + e +"\" could not be executed "+ ex);
		        }
		      }
		    }
		  }


		  /**
		   * Gives access to the first shell that was added to this manager. Only really
		   * used when access is needed to a JFrame so that a dialog can appear.
		   * @return the first Shell added to this manager
		   */
		  public Shell getShell(){
		    for(WeakReference<Shell> r : shells)
		      if (r.get() != null){
			return r.get();
		      }
		    return null;
		  }
	  
	  


}
