package se.kth.shell;

import javax.swing.text.AttributeSet;

//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import se.kth.shell.HyperlinkPattern;

public interface HyperlinkDecorator {
	  public AttributeSet getActiveStyle(HyperlinkPattern hpe, Object result);
	  public AttributeSet getInactiveStyle(HyperlinkPattern hpe, Object result);
	}

