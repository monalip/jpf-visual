package kth.se.Interfaces;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.SystemAttribute;
import gov.nasa.jpf.jvm.bytecode.EXECUTENATIVE;
import gov.nasa.jpf.jvm.bytecode.INVOKESTATIC;
import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.util.HashData;
import gov.nasa.jpf.util.IntVector;
import gov.nasa.jpf.util.JPFLogger;
import gov.nasa.jpf.util.Predicate;
import gov.nasa.jpf.util.StringSetMatcher;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.bytecode.ReturnInstruction;
import gov.nasa.jpf.vm.choice.BreakGenerator;
import gov.nasa.jpf.vm.ThreadData;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;



public interface ThreadInfoInterface {
	
	public int getId();
	public ElementInfo getElementInfo (int objRef);
	 public String getStateName();
	 //public threadData.getState();
}
