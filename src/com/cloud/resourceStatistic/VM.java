package com.cloud.resourceStatistic;

public class VM//封装VM对象，包含VMId和VMName
{
	public String vmName;
	public int vmId;
	
	public VM()
	{
	}
	
	String getVmName(){return vmName;}
	int getVMId(){return vmId;}
	
	void setVMName(String text)
	{
		vmName = text;
	}
	void setVMId(int number)
	{
		vmId = number;
	}
}
