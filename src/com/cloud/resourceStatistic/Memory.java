package com.cloud.resourceStatistic;

public class Memory//将每一个物理地域中的服务器Memory信息封装，包含所属服务器名称，所在地，尺寸，占总尺寸的百分比
{
	public String memorySeverName;
	public String memorySeverLocation;
	public int memoryNumber;
	public String memoryPercentage;
	
	public Memory()
	{
	}
	
	String getMemorySeverName(){return memorySeverName;}
	String getMemorySeverLocation(){return memorySeverLocation;}
	int getMemoryNumber(){return memoryNumber;}
	String getMemoryPercentage(){return memoryPercentage;}
	
	void setMemorySeverName(String text)
	{
		memorySeverName = text;
	}
	void setMemorySeverLocation(String text)
	{
		memorySeverLocation = text;
	}
	void setMemoryNumber(int number)
	{
		memoryNumber = number;
	}
	void setMemoryPercentage(int number, int total)
	{
		float temp = (float)number / (float)total;
		temp *= 100;
		memoryPercentage = temp + "%";
	}
}
