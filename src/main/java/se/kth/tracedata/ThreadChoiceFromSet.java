package se.kth.tracedata;


public class ThreadChoiceFromSet extends ChoiceGenerator<ThreadInfo>{
	protected ThreadInfo[] values;
	
	public ThreadInfo[] getChoices(){
	    return values;
	  }
	
	public ThreadInfo getChoice (int idx){
	    if (idx >= 0 && idx < values.length){
	      return values[idx];
	    } else {
	      throw new IllegalArgumentException("choice index out of range: " + idx);
	    }
	  }

}
