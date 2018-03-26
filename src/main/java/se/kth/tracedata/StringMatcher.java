package se.kth.tracedata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * simple wrapper around Patter/Matcher pairs, with patterns using '*' wildcards
 * same as StringSetMatcher but for single patterns
 */
public class StringMatcher {

  boolean isAnyPattern; // do we have a universal '*' pattern?

  Pattern pattern;
  Matcher matcher;

  public static boolean hasWildcard (String patternSpec){
    return (patternSpec.indexOf('*') >= 0);
  }

  public StringMatcher (String patternSpec){
    if (patternSpec.equals("*")) {
      isAnyPattern = true;
      // no need to compile this

    } else {
      Pattern p = createPattern(patternSpec);
      pattern = p;
      matcher = p.matcher(""); // gets reset upon use
    }
  }

  protected Pattern createPattern (String s){
    Pattern p;

    StringBuilder sb = new StringBuilder();

    int len = s.length();
    for (int j=0; j<len; j++){
      char c = s.charAt(j);
      switch (c){
      case '.' : sb.append("\\."); break;
      case '$' : sb.append("\\$"); break;
      case '[' : sb.append("\\["); break;
      case ']' : sb.append("\\]"); break;
      case '*' : sb.append(".*"); break;
      case '(' : sb.append("\\("); break;
      case ')' : sb.append("\\)"); break; 
      // <2do> and probably more..
      default:   sb.append(c);
      }
    }

    p = Pattern.compile(sb.toString());
    return p;
  }

  public boolean matches (String s){
    if (isAnyPattern){
      return true;
    } else {
      matcher.reset(s);
      return matcher.matches();
    }
  }

  @Override
  public String toString() {
    if (isAnyPattern){
      return ".*";
    } else {
      return pattern.toString();
    }
  }
}
