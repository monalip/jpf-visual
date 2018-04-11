package se.kth.tracedata.jpf;

import gov.nasa.jpf.vm.ClassInfo;

public class MethodInfo implements se.kth.tracedata.MethodInfo
{
	gov.nasa.jpf.vm.MethodInfo jpfMethodinfo;
	public MethodInfo(gov.nasa.jpf.vm.MethodInfo jpfMethodinfo)
	{
		this.jpfMethodinfo = jpfMethodinfo;
	}
	@Override
	public boolean isSynchronized() {
		return jpfMethodinfo.isSynchronized();
	}
	@Override
	public ClassInfo getClassInfo() {
		return jpfMethodinfo.getClassInfo();
	}
	@Override
	public String getUniqueName() {
		return jpfMethodinfo.getUniqueName();
	}
	@Override
	public String getFullName() {
		return jpfMethodinfo.getFullName();
	}
	@Override
	public String getClassName() {
		return jpfMethodinfo.getClassName();
	}

}
