package se.kth.tracedata;



public interface PublisherExtension {
	void publishStart (Publisher publisher);
	  void publishTransition (Publisher publisher);
	  void publishPropertyViolation (Publisher publisher);
	  void publishConstraintHit (Publisher publisher);
	  void publishFinished (Publisher publisher);
	  void publishProbe (Publisher publisher);

}
