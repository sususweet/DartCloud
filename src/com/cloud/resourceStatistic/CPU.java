package com.cloud.resourceStatistic;

public class CPU//将每一个物理地域中的服务器CPU信息封装，包含所属服务器名称，所在地，数量，占总数量的百分比
{
	public String cpuSeverName;
	public String cpuSeverLocation;
	public int cpuNumber;
	public String cpuPercentage;
	
	public CPU()
	{
	}
	
	String getCpuSeverName(){return cpuSeverName;}
	String getCpuSeverLocation(){return cpuSeverLocation;}
	int getCpuNumber(){return cpuNumber;}
	String getCpuPercentage(){return cpuPercentage;}
	
	void setCpuSeverName(String text)
	{
		cpuSeverName = text;
	}
	void setCpuSeverLocation(String text)
	{
		cpuSeverLocation = text;
	}
	void setCpuNumber(int number)
	{
		cpuNumber = number;
	}
	void setCpuPercentage(int number, int total)
	{
		float temp = (float)number / (float)total;
		temp *= 100;
		cpuPercentage = temp + "%";
	}
}
