package se.kth.shell;

public class LinkDestination{
	  public String path;
	  public int line;

	  public LinkDestination(String path, int line){
	    this.path = path;
	    this.line = line;
	  }

	  @Override
	  public String toString(){
	    return path + ":" + line;
	  }
	}