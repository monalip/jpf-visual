package se.kth.tracedata;

public interface ErrorTracePanelGui {
	public void initialization();
	public void displayErrMsg(boolean err);
	public void drowErrTrace(Path path, boolean found);

}
