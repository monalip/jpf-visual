package se.kth.shell;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyledEditorKit;

//import gov.nasa.jpf.shell.Shell;
import se.kth.shell.Shell;
//import gov.nasa.jpf.shell.ShellManager;
import se.kth.shell.ShellManager;
//import gov.nasa.jpf.shell.util.HyperlinkEditorDocument;
import se.kth.shell.HyperlinkEditorDocument;
//import gov.nasa.jpf.shell.util.PopupMenu;
import se.kth.shell.PopupMenu;
//import gov.nasa.jpf.shell.util.Range;
import se.kth.shell.Range;
//import gov.nasa.jpf.shell.util.Tuple;
import se.kth.shell.Tuple;
//import gov.nasa.jpf.shell.util.Tuple3D;
import se.kth.shell.Tuple3D;
//import gov.nasa.jpf.shell.util.hyperlinks.BasicHyperLinkDecorator;
import se.kth.shell.BasicHyperLinkDecorator;
//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import se.kth.shell.HyperlinkDecorator;
//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import se.kth.shell.HyperlinkPattern;

public class HyperlinkEditorPane extends JEditorPane
implements MouseListener, MouseMotionListener {




	  private static final String MIME_TYPE = "text/hyperlinked-output";

	  Map<HyperlinkPattern, HyperlinkDecorator> pattern2decorator
	          = new HashMap<HyperlinkPattern, HyperlinkDecorator>();

	  public HyperlinkEditorPane(Map<HyperlinkPattern, HyperlinkDecorator> patterns){
	    this();
	    pattern2decorator = patterns;
	  }

	  public HyperlinkEditorPane(){
	    super();
	    setContentType(MIME_TYPE);
	    //setFont(UIManager.getFont("controlFont"));
	    createPopupMenu();

	    setEditorKit(new StyledEditorKit(){
	      @Override
	      public Document createDefaultDocument(){
	        return new HyperlinkEditorDocument();
	      }

	      @Override
	      public String getContentType(){
	        return MIME_TYPE;
	      }

	    });

			setEditable(false);
	    addMouseListener(this);
	    addMouseMotionListener(this);
	  }

	  @Override
	  public void setText(String txt){
	    setText(txt, pattern2decorator);
	  }

	  public void setText(String txt, Map<HyperlinkPattern, HyperlinkDecorator> patterns){
	    HyperlinkEditorDocument doc = (HyperlinkEditorDocument)getDocument();
	    try {
	      doc.remove(0, doc.getLength());
	      doc.insertString(0, txt, null, patterns);
	    } catch (BadLocationException ex) {
	      Logger.getLogger(HyperlinkEditorPane.class.getName()).log(Level.SEVERE,
	                                                                null, ex);
	    }
	  }

	  public Style getDefaultStyle(){
	    return ((HyperlinkEditorDocument) getDocument()).getDefaultStyle();
	  }

	  /**
	   * Override and return false to disable word-wrapping. The wrapping is disabled
	   * because a scroll bar is preferred.
	   */
	  @Override
	  public boolean getScrollableTracksViewportWidth(){
	    return false;
	  }

	  private void createPopupMenu() {
	    PopupMenu popup = new PopupMenu(this);
	    
	    popup.add("Copy", new AbstractAction(){
	      public void actionPerformed(ActionEvent e) {
	        copy();
	      }
	    }, KeyStroke.getKeyStroke('C', KeyEvent.META_DOWN_MASK, true));
	    
	    popup.add("Save As..", new AbstractAction() {
	      public void actionPerformed(ActionEvent e) {
	        saveAs();
	      }
	    });
	  }

	  protected void saveAs(){
	    JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

	    if (chooser.showDialog(ShellManager.getManager().getShell(), "Save") == JFileChooser.APPROVE_OPTION) {
	      saveFile( chooser.getSelectedFile());
	    }
	  }
	   
	  /**
	   * this can be a very large chunk of data, do this in background 
	   */
	  protected Future<File> saveFile (final File file){
	    final Shell shell = ShellManager.getManager().getShell();
	    
	    if (!file.isDirectory() && (!file.exists() || file.canWrite())) {
	      SwingWorker<File, Void> worker = new SwingWorker<File, Void>() {

	        @Override
	        public File doInBackground() {
	          
	          Document doc = getDocument();
	          int len = doc.getLength();
	          
	          try {
	            FileWriter fw = new FileWriter(file);
	            for (int written = 0; written < len;) {
	              int n = Math.max(1024, len - written);
	              String s = doc.getText(written, n);
	              written += s.length();
	              fw.write(s, 0, s.length());
	            }
	            fw.close();
	          } catch (BadLocationException blx){
	            shell.error("error reading document contents");
	          } catch (FileNotFoundException fnx){
	            shell.error("file not writable: " + file);
	          } catch (IOException iox){
	            shell.error("error writing to file: " + file);
	          }
	          
	          return file;
	        }
	      };

	      worker.execute();
	      return worker;
	      
	    } else {
	      shell.error("not a valid file: " + file.getPath());
	      return null;
	    }
	  }
	    
	  //Mouse Listener classes
	  public void mouseReleased(MouseEvent e) {
	    int pos = viewToModel(e.getPoint());
	    HyperlinkEditorDocument doc = (HyperlinkEditorDocument) getDocument();
	    Tuple<HyperlinkPattern, Object> hyperLink = doc.getHyperLink(pos);
	    if ( hyperLink != null ){
	      hyperLink.a.onClick(hyperLink.b);
	    }
	  }

	  private Range currentRange = null;
	  public void mouseMoved(MouseEvent e) {
	    int pos = viewToModel(e.getPoint());
	    HyperlinkEditorDocument doc = (HyperlinkEditorDocument) getDocument();
	    Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator> h
	            = doc.getHyperLink(currentRange);
	    int cm = h != null ? h.a.getModifiers() : 0;
	    if ( 
	         //We already have a currentRange
	         currentRange != null &&
	         //The mouse left this component...
	         ( !getBounds().contains(e.getPoint())
	         // We are no longer hovering over the hyperlink
	         || !currentRange.containsInt(pos))
	         // The modifer is no more
	         || (cm != 0 && (e.getModifiers() & cm) == 0)
	         ) {
	      //Turn off the currently active link
	      doc.setActive(currentRange, false);
	      currentRange = null;
	    }
	    //ShellManager.getManager().getLogger().info(String.valueOf(pos));
	    currentRange = doc.getRange(pos);

	    if (currentRange != null) {
	      Tuple3D<HyperlinkPattern, Object, HyperlinkDecorator> hyperLink
	                                              = doc.getHyperLink(currentRange);
	      int m = hyperLink.a.getModifiers();
	      String tt = hyperLink.a.getTooltip(hyperLink.b);
	      if (m == 0 || (e.getModifiers() & m) != 0) {
	        //We are mousing over a hyperlink with the correct modifiers
	        doc.setActive(currentRange, true);
	        setCursor(new Cursor(Cursor.HAND_CURSOR));
	        setToolTipText(hyperLink.a.getTooltip(hyperLink.b));
	      }else if (tt != null && tt.isEmpty() == false){
	        StringBuilder stringBuilder
	                = new StringBuilder("Hold down ")
	                .append(KeyEvent.getKeyModifiersText(m))
	                .append(" and left click to ")
	                .append(hyperLink.a.getTooltip(hyperLink.b));
	        setToolTipText(stringBuilder.toString());
	      }
	    } else {
	      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	      setToolTipText(null);
	    }
	  }

	  public void addHyperlinkPattern(HyperlinkPattern hp) {
	    pattern2decorator.put(hp, new BasicHyperLinkDecorator());
	  }

	  public void addHyperlinkPatterns(Map<HyperlinkPattern, HyperlinkDecorator> patterns){
	    if (patterns == null || patterns.isEmpty()) { return; }
	    pattern2decorator.putAll(patterns);
	  }

	  public void addHyperlinkPatterns(Collection<HyperlinkPattern> hyperlinkPatterns) {
	    if (hyperlinkPatterns == null || hyperlinkPatterns.isEmpty()) { return; }
	    for (HyperlinkPattern hyperlinkPattern : hyperlinkPatterns) {
	      addHyperlinkPattern(hyperlinkPattern);
	    }
	  }


	  public void mousePressed(MouseEvent e) {}
	  public void mouseClicked(MouseEvent e) {}
	  public void mouseEntered(MouseEvent e) {mouseMoved(e);}
	  public void mouseExited(MouseEvent e) {mouseMoved(e);}
	  public void mouseDragged(MouseEvent e) {}

	  public HyperlinkEditorDocument getHyperlinkDocument(){
	    return (HyperlinkEditorDocument) getDocument();
	  }

	  public void setHyperlinkDecorator(HyperlinkPattern pattern,
	                                                  HyperlinkDecorator decorator){
	    pattern2decorator.put(pattern, decorator);
	  }
}
