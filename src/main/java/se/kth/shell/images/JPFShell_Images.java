package se.kth.shell.images;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;
/**
 * Class to help manage the icon resources of jpf-shell and basicshell. Use this
 * class to load images as needed
 */
public class JPFShell_Images {

  //--------------- Command Icons -------------------------------------
  public enum CommandIcon{ Test, Verify, }
  
  private static HashMap<CommandIcon, URL> cimap= new HashMap<CommandIcon, URL>(){{
    put(CommandIcon.Test, getClass().getResource("test.png"));
    put(CommandIcon.Verify, getClass().getResource("tick.png")); //verify.png
  }};

  /**
   * Returns the icon for the given command
   * @param ci The enum representing the command
   * @param s The description wanted for the ImageIcon returned
   * @return an ImageIcon with the given icon and description
   */
  public static ImageIcon getCommandIcon(CommandIcon ci, String description){
    return new ImageIcon(cimap.get(ci), description);
  }


  //--------------- Status Icons -------------------------------------
  public enum StatusIcon{ TestOn, TestOff, VerifyOn, VerifyOff, Working}

  private static HashMap<StatusIcon, URL> simap= new HashMap<StatusIcon, URL>(){{
    put(StatusIcon.TestOn, getClass().getResource("teststatus_on.png"));
    put(StatusIcon.TestOff, getClass().getResource("teststatus_off.png"));
    put(StatusIcon.VerifyOn, getClass().getResource("verifystatus_on.png"));
    put(StatusIcon.VerifyOff, getClass().getResource("verifystatus_off.png"));
    put(StatusIcon.Working, getClass().getResource("reload.gif")); //working.gif
  }};

  public static ImageIcon getStatusIcon(StatusIcon si, String description){
    return new ImageIcon(simap.get(si), description);
  }

}
