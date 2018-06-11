package se.kth.jpf_visual.jvm;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunJVM {

	public static void main(String[] args) {
		System.out.println("Here is JVM starting");
		/**
		 * ProcessBuilder take the executable jar of the DiningPhil program which 
		 * generate the error trace and show the output there
		 * 
		 */
		 ProcessBuilder pb = new ProcessBuilder("java", "-jar", "RunDiningPhil.jar");
	        pb.directory(new File("/Users/monali/eclipse-workspace/JPF_VISUAL/Trace_Jar"));
	        try {
	            Process p = pb.start();
	            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
	            Thread thread = new Thread(lsr, "LogStreamReader");
	            thread.start();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	}


class LogStreamReader implements Runnable {

    private BufferedReader reader;

    public LogStreamReader(InputStream is) {
    	
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            String line = reader.readLine();
            while (line != null) {
            	System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
