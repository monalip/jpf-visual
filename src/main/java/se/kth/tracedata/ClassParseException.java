package se.kth.tracedata;

/**
 * an exception while parsing a ClassFile
 */
public class ClassParseException extends Exception {

  public ClassParseException (String details){
    super(details);
  }
  
  public ClassParseException (String details, Throwable cause){
    super(details, cause);
  }
}
