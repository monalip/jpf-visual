package se.kth.jpf_visual.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;




import se.kth.jpf_visual.ClassFieldExplorer;
import se.kth.jpf_visual.ClassMethodExplorer;
import se.kth.jpf_visual.ErrorTableAndMapPane;
import se.kth.jpf_visual.ErrorTracePrinter;
import se.kth.jpf_visual.FieldNode;
import se.kth.jpf_visual.MethodNode;
import se.kth.jpf_visual.PaneConstants;
import se.kth.jpf_visual.TraceData;
//import gov.nasa.jpf.util.Pair;
import se.kth.tracedata.Pair;
//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.Path;

/**
 * Basic output panel that divides new trace printer's results into browseable
 * topics. This panel uses a
 * {@link gov.nasa.jpf.shell.listeners.VerifyCommandListener} to keep track of
 * when the VerifyCommand is executed.
 */

//public class ErrorTracePanel extends ShellPanel implements VerifyCommandListener {
public class ErrorTracePanel {
	
	// Panel
		private JLabel statusLabel = new JLabel();
	/**
	 * Just show the results of the JPF verification.
	 * 
	 * @param command
	 * 
	 * If there is an error in programme then call the function displayErrMsg()
	 */
	public void displayErrMsg(boolean err)
	{
		if(err)
		{
			statusLabel.setText("An Error occured during the verify, check the Error Panel for more details");
			statusLabel.setForeground(Color.RED);
		}
		else
		{
			statusLabel.setText("The JPF run completed successfully");
			statusLabel.setForeground(Color.BLACK);
		}
	}
	
}
