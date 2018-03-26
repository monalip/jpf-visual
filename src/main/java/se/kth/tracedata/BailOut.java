package se.kth.tracedata;

/**
* this is just an evil control flow exception for non-local gotos
* Useful in a context where you have to terminate execution from different (recursive) levels
*/
public class BailOut extends RuntimeException {

 public BailOut(){
   // nothing here
 }
 
 public BailOut (String details){
   super(details);
 }
}
