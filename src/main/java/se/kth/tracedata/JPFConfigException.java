package se.kth.tracedata;

/**
 * this class wraps the various exceptions we might encounter during
 * initialization and use of Config
 */
@SuppressWarnings("serial")
public class JPFConfigException extends JPFException {
	

  public JPFConfigException(String msg) {
    super(msg);
  }

  public JPFConfigException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public JPFConfigException(String key, Class<?> cls, String failure) {
    super("error instantiating class " + cls.getName() + " for entry \"" + key + "\":" + failure);
  }

  public JPFConfigException(String key, Class<?> cls, String failure, Throwable cause) {
    this(key, cls, failure);
    initCause(cause);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("JPF configuration error: ");
    sb.append(getMessage());

    return sb.toString();
  }
}
