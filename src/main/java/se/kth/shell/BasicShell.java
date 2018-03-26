package se.kth.shell;

//import gov.nasa.jpf.shell.ShellPanel;
import se.kth.shell.ShellPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SpringLayout;
import javax.swing.ToolTipManager;

//import gov.nasa.jpf.shell.basicshell.StatusPanel;
import se.kth.shell.StatusPanel;
//import gov.nasa.jpf.shell.basicshell.BasicChildShell;
import se.kth.shell.BasicChildShell;
//import gov.nasa.jpf.shell.ShellCommand;
import se.kth.shell.ShellCommand;
//import gov.nasa.jpf.shell.basicshell.ShellTabbedPane;
import se.kth.shell.ShellTabbedPane;
//import gov.nasa.jpf.shell.util.GraphicsUtil;
import se.kth.shell.GraphicsUtil;
//import gov.nasa.jpf.shell.Shell;
import se.kth.shell.Shell;

/**
* The most basic swing implementation of a Shell in the jpf-shell library. Serves
* most of the purposes that any shell need to serve. When developing your own
* shells this should be  a starting point since the BasicShell class is subclass-able.
*
* <ul>
*	<li> {@link gov.nasa.jpf.shell.commands.TestCommand}</li>
*	<li> {@link gov.nasa.jpf.shell.commands.VerifyCommand}</li>
* </ul>
*
* <h4>Tabs: </h4>
* <ul>
*	<li> {@link gov.nasa.jpf.shell.panels.PropertiesPanel} </li>
*	<li> {@link gov.nasa.jpf.shell.panels.ReportPanel} </li>
*	<li> {@link gov.nasa.jpf.shell.panels.TestConsolePanel} </li>
*	<li> {@link gov.nasa.jpf.shell.panels.VerifyConsolePanel} </li>
* </ul>
*
* <h4>Listeners: </h4>
* <ul>
*	<li> NONE </li>
* </ul>
*/
public class BasicShell extends Shell {
	  private Container commandArea;
	  private int closedTabcount = 0;
	  private StatusPanel statusBar;
	  private JMenuBar panelMenuBar;
	  private JMenu panelMenu;

	  private static final Dimension STARTING_SIZE = new Dimension(1000, 600);

	/**
	   * If this is not a child shell, the this points to itself. If this is a child shell, then this points
	   * to the non-child BasicShell that this child originally comes from. Usually, that would be the parent, but
	   * if this child shell is broken off from another child shell, the master shell is the master shell of the parent.
	   */
	  protected BasicShell masterShell;

	/**
	   * Sets up the frame to have commands and panels added to it. <br>
	   * This is where the swing code comes in to lay everything out.
	   *
	   * After that, the VerifyCommand and TestCommand is added to the manager
	   * who then adds those commands back onto us. (yes, this sounds crazy but this
	   * is what works)
	   */
	  private void createShell(){
	    this.masterShell = this;
	    // This will be overwritten by BasicChildShell if this is a basic child shell.


	    setLayout(new BorderLayout());


	    add(createToolBar(), BorderLayout.NORTH);

	    // During the creation of these items, master shell is not yet set correctly
	    add(createTabbedArea(), BorderLayout.CENTER);
	    add(createStatusBar(), BorderLayout.SOUTH);

	    pack();
	    setSize(STARTING_SIZE);
	    setLocationRelativeTo(null);
	    registerShell();
	  }
	/**
	   * The pane that holds all of the shell panels for this BasicShell
	   */
	  private ShellTabbedPane tabbedPane;
	/**
	   * @param panel makes this panel visible to the user, by selecting it.
	   */
	  @Override
	  public void requestFocus(ShellPanel panel) {
	    tabbedPane.setSelectedComponent(panel);
	  }
	  
	  /**
	   * Creates a BasicShell using the ShellManager returned by 
	   * {@link gov.nasa.jpf.shell.ShellManager#getManager()} and a custom title.
	   * <br>
	   * <b>Note:</b> this requires that the ShellManager already be initialized
	   */
	  public BasicShell(String title){
	    super(title);
	    createShell();
	  }
	  
	  protected JPanel createToolBar() {
		    Component commandPanel = createCommandArea();
		    panelMenuBar = createPanelMenu();

		    JPanel toolbar = new JPanel() {
		      @Override
		      public void paintComponent(Graphics g) {
		        GraphicsUtil.drawGradientBackground(g, getBackground().brighter(),
		                                              getBackground().darker());
		      }
		    };
		    toolbar.add(createCommandArea());
		    toolbar.add(panelMenuBar);
		    toolbar.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		    SpringLayout layout = new SpringLayout();
		    //Command area
		    layout.putConstraint(SpringLayout.NORTH, toolbar, 1, SpringLayout.NORTH, commandPanel);
		    layout.putConstraint(SpringLayout.SOUTH, toolbar, 1, SpringLayout.SOUTH, commandPanel);
		    layout.putConstraint(SpringLayout.WEST, toolbar, 1, SpringLayout.WEST, commandPanel);

		    //Panel menu
		    layout.putConstraint(SpringLayout.NORTH, panelMenuBar, 1, SpringLayout.NORTH, toolbar);
		    layout.putConstraint(SpringLayout.SOUTH, panelMenuBar, 1, SpringLayout.SOUTH, toolbar);
		    layout.putConstraint(SpringLayout.EAST, panelMenuBar, 1, SpringLayout.EAST, toolbar);
		    toolbar.setLayout(layout);

		    return toolbar;
		  }
	  /**
	   * Constructs the {@link gov.nasa.jpf.shell.basicshell.ShellTabbedPane} that will hold
	   * all of the panels. <br />
	   * Override to implement a custom ShellPanel area, or a replacement for
	   * ShellTabbedPane
	   */
	  protected Container createTabbedArea() {
	    tabbedPane = new ShellTabbedPane(this);
	    ToolTipManager.sharedInstance().registerComponent(tabbedPane);

	    //Add listener for the popup menu
	    tabbedPane.addMouseListener(new MouseAdapter() {

	      @Override
	      public void mousePressed(MouseEvent e) {
	        showTabPopup(e);
	      }

	      @Override
	      public void mouseReleased(MouseEvent e) {
	        showTabPopup(e);
	      }
	    });


	    return tabbedPane;
	  }
	  /**
	   * This function is responsible for the menu that appears when a toolbar header
	   * right clicked.
	   * @param e the mouse event generated from the toolbar header click
	   */
	  private void showTabPopup(MouseEvent e) {
	    if (!e.isPopupTrigger()) {
	      return;
	    }

	    Point p = e.getPoint();
	    int tabIndex = tabbedPane.indexAtLocation(p.x, p.y);
	    if (tabIndex == -1) {
	      return;
	    }
	    final ShellPanel panel = (ShellPanel) tabbedPane.getComponent(tabIndex);

	    JPopupMenu popup = new JPopupMenu();
	    JMenuItem close = new JMenuItem("Close");
	    close.addActionListener(new ActionListener() {

	      @Override
	      public void actionPerformed(ActionEvent e) {
	        if (panel.closing()) {
	          panel.closed();
	        }
	        removeShellPanel(panel);
	      }
	    });
	    popup.add(close);
	    popup.show(tabbedPane, p.x, p.y);
	  }

	  /**
	   * Removes the toolbar from the shell
	   * @param panel the toolbar that will be removed. Does nothing if the specified toolbar
	   * is not found on this shell. If this is the last toolbar on the shell, the shell
	   * is closed.
	   */
	  @Override
	  public void removeShellPanel(final ShellPanel panel) {

	    int index = tabbedPane.indexOfComponent(panel);
	    if (index > -1) {
	      tabbedPane.removeTabAt(index);
	    }
	    super.removeShellPanel(panel);

	    // Handle the "closed tabs" manu
	    if ( this.masterShell.closedTabcount == 0 ) {
	      this.masterShell.panelMenuBar.setVisible(true);
	    }
	    this.masterShell.closedTabcount++;
	    final JMenuItem mi = new JMenuItem(panel.getTitle());
	    mi.addActionListener(new ActionListener(){
	      @Override
	      public void actionPerformed(ActionEvent ae) {
	        masterShell.panelMenu.remove(mi);
	        masterShell.closedTabcount--;
	        masterShell.addShellPanel(panel);
	        if (masterShell.closedTabcount == 0) {
	          masterShell.panelMenuBar.setVisible(false);
	        }
	      }
	    });
	    this.masterShell.panelMenu.add(mi);
	  }
	  /**
	   * Creates a status bar for the shell that is put along the bottom of the 
	   * shell frame. The status bar serves the purpose of updating the user on
	   * what the shell is doing.
	   * @return The JPanel that holds the status bar.
	   */
	  protected StatusPanel createStatusBar(){
	    statusBar = new StatusPanel();
	    return getStatusBar();
	  }
	  /**
	   * Constructs the Container that holds all of the command buttons. <br>
	   * Override this to implement a custom Command Area toolbar.
	   */
	  protected Container createCommandArea() {
	    if (commandArea == null) {
	      JPanel panel= new JPanel();
	      panel.setBackground(new Color(0, 0, 0, 0));
	      panel.setOpaque(false);
	      commandArea = panel;
	    }
	    return commandArea;
	  }
	  protected JMenuBar createPanelMenu() {
		    JMenuBar tabsBar = new JMenuBar();
		    tabsBar.setVisible(false);
		    tabsBar.setOpaque(false);
		    panelMenu = new JMenu("Closed Tabs"){
		      @Override
		      public void paintComponent(Graphics g){
		        super.paintComponent(g);
		        GraphicsUtil.drawButtonBackground(g, this);
		      }
		    };
		    panelMenu.setOpaque(false);
		    panelMenu.setRolloverEnabled(true);
		    tabsBar.add(panelMenu);
		    return tabsBar;
		  }
	  /**
	   * This is is a convenience method that is equivalent to:
	   * <pre> addShellPanel(toolbar, getTabPane().getTabCount()) </pre>.
	   * @param panel adds this panel to the end of the shell's tabbed pane.
	   */
	  @Override
	  public void addShellPanel(final ShellPanel panel) {
	    addShellPanel(panel, tabbedPane.getTabCount());
	  }
	  /**
	   * Inserts a panel into the specified position. This WILL set the panel's "shell" member field to this shell.
	   * @param panel the shellPanel that need be inserted.
	   * @param index the position to add the toolbar into.
	   */
	  private void addShellPanel(final ShellPanel panel, int index) {
	    // getTabPane().add(panel, index);
	    tabbedPane.insertShellPanel(panel, index);
	    super.addShellPanel(panel);
	    setSize(STARTING_SIZE);
	  }
	  /**
	   * @return the toolbar that represents the status bar see {@link #createStatusBar()}
	   * for more information.
	   */
	  public StatusPanel getStatusBar(){
	    return statusBar;
	  }
	@Override
	public void start(String[] args) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updateShellCommand(ShellCommand command) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void installCommand(ShellCommand command) {
		// TODO Auto-generated method stub
		
	}
	/*
	   * Called when a toolbar is branching off into its own shell.
	   * The child shell of the BasicShell is a BasicShell with no commands
	   * at the top. See {@link gov.nasa.jpf.shell.BasicShell} for more information.
	   * @return the new child shell
	   */
	  @Override
	  public Shell createChildShell() {
	    return new BasicChildShell(this.masterShell);
	  }
	





}
