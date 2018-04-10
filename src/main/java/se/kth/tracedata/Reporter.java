package se.kth.tracedata;

import java.util.List;

import se.kth.tracedata.Path;
import se.kth.tracedata.jpf.Publisher;

public interface Reporter{
	public Path getPath();
	public List<Publisher> getPublishers();

}
