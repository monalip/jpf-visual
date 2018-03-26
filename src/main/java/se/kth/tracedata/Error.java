package se.kth.tracedata;

//import gov.nasa.jpf.Property;
import se.kth.tracedata.Property;
//import gov.nasa.jpf.vm.Path;
import se.kth.tracedata.jpf.Path;


/**
 * class used to store property violations (property and path)
 */
public class Error {

  int            id;
  
  Property       property;
  private String errorMessage;
  
  private Path   path;
 
  
  public Error (int id, Property prop, Path p) {
    this.id = id;
    property = prop;
    errorMessage = prop.getErrorMessage();    
    path = p; // client has to clone in case we go on

  }

  public int getId() {
    return id;
  }
  
  public String getDescription () {
    StringBuilder sb = new StringBuilder();
    sb.append(property.getClass().getName());
    
    String s = property.getExplanation();
    if (s != null) {
      sb.append(" (\"");
      sb.append(s);
      sb.append("\")");
    }
    
    return sb.toString();
  }

  public String getDetails() {
    return errorMessage;
  }
  
  public Path getPath () {
    return path;
  }

  public Property getProperty () {
    return property;
  }
}
