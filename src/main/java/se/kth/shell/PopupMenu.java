package se.kth.shell;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
* little helper to make it less tedious to create popups with corresponding
* KeyStroke actions (accelerators are not processed if the popup is not visible)
*/
public class PopupMenu extends JPopupMenu {
 
 JComponent target;
 
 public PopupMenu (JComponent target){
   this.target = target;
   target.setComponentPopupMenu(this);
 }
 
 public void add (String mItemName, Action action){
   JMenuItem mItem = new JMenuItem(mItemName);

   mItem.addActionListener( action);
   add(mItem);    
 }
 
 public void add (String mItemName, Action action, KeyStroke keyStroke){
   JMenuItem mItem = new JMenuItem(mItemName);

   mItem.addActionListener( action);
   mItem.setAccelerator(keyStroke);
   add(mItem);
   
   target.getInputMap().put(keyStroke, mItemName);
   target.getActionMap().put(mItemName, action);
 }

}
