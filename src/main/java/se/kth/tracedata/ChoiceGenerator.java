package se.kth.tracedata;

import se.kth.tracedata.jpf.ThreadInfo;

public interface ChoiceGenerator<T> {
	String getId();
	 int getTotalNumberOfChoices();
	 //boolean method is created to check choicegenerator is instace of ThreadChoiceFromSet
	 boolean isInstaceofThreadChoiceFromSet();
	 public ThreadInfo getChoice (int idx);

}
