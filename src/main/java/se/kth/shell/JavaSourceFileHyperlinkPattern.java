package se.kth.shell;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

//import gov.nasa.jpf.shell.ShellManager;
import se.kth.shell.ShellManager;
//import gov.nasa.jpf.shell.util.LinkDestination;
import se.kth.shell.LinkDestination;
//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkFileCache;
import se.kth.shell.HyperlinkFileCache;
//import gov.nasa.jpf.shell.util.hyperlinks.HyperlinkPattern;
import se.kth.shell.HyperlinkPattern;

public class JavaSourceFileHyperlinkPattern implements HyperlinkPattern{

	  private static final Pattern filename = Pattern.compile("([\\w\\\\/]+\\.java)(?::(\\d+))?");


	  private int modifier = 0;

	  public Pattern getPattern() { return filename; }

	  public Object getResult(String str, MatchResult m){
	    String filePath = HyperlinkFileCache.getSourcePath(m.group(1));
	    if (filePath != null) {
	      int lineNumber = m.group(2) == null ? 0 : new Integer(m.group(2));
	      return new LinkDestination(filePath, lineNumber);
	    }else{
	      return null;
	    }
	  }

	  public void onClick(Object result) {
	    LinkDestination dst = (LinkDestination) result;
	    ShellManager.getManager().printLinkCommand(dst);
	  }


	  public String getTooltip(Object result){
	    StringBuilder t = new StringBuilder("Open Link to ");
	    LinkDestination dest = (LinkDestination) result;
	    if (dest.line > -1){
	      t.append("line ").append(dest.line).append(" in ");
	    }
	    t.append(dest.path);
	    return t.toString();
	  }

	  public int getModifiers(){
	    return modifier;
	  }

	  public void setModifier(int modifier){
	    this.modifier = modifier;
	  }

	}