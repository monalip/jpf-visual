package kth.se.Adapters;

import java.util.Map;

import gov.nasa.jpf.vm.ElementInfo;

import kth.se.Interfaces.ThreadInfoInterface;


public class ThreadInfoAdapt implements ThreadInfoInterface {
	
	static Map<Integer, Integer> globalTids;  // initialized by init
	 protected int computeId (int objRef) {
		    Integer id = globalTids.get(objRef);
		    
		    if(id == null) {
		      id = globalTids.size();
		      //addId(objRef, id);
		    }

		    return id;
		  }
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ElementInfo getElementInfo(int objRef) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getStateName() {
		return null;
	}

	
	

}
