package se.kth.shell;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkDecorator;
import se.kth.shell.HyperlinkDecorator;
//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import se.kth.shell.HyperlinkDecorator;

public class BasicHyperLinkDecorator implements HyperlinkDecorator{

	  private Color color = Color.BLUE;

	  public BasicHyperLinkDecorator(){ }

	  public BasicHyperLinkDecorator(Color c){
	    this.color = c;
	  }

	  public AttributeSet getActiveStyle(HyperlinkPattern hpe, Object result) {
	    SimpleAttributeSet as = new SimpleAttributeSet();
	    StyleConstants.setForeground(as, color);
	    StyleConstants.setUnderline(as, true);
	    return as;
	  }

	  public AttributeSet getInactiveStyle(HyperlinkPattern hpe, Object result) {
	    SimpleAttributeSet as = new SimpleAttributeSet();
	    StyleConstants.setForeground(as, color);
	    return as;
	  }

	}
