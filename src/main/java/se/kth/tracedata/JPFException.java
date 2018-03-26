package se.kth.tracedata;

import java.io.PrintStream;

/**
 * common root for all exceptions thrown by JPF
 */
@SuppressWarnings("serial")
public class JPFException extends RuntimeException {

  public JPFException (String details) {
    super(details);
  }

  public JPFException (Throwable cause) {
    super(cause);
  }

  public JPFException (String details, Throwable cause){
    super(details, cause);
  }
  
  @Override
  public void printStackTrace (PrintStream out) {
    out.println("---------------------- JPF error stack trace ---------------------");
    super.printStackTrace(out);
  }

}
