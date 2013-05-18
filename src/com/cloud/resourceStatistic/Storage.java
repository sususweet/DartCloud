package com.cloud.resourceStatistic;

public class Storage//将每一个物理地域中的服务器Storage信息封装，包含所属服务器名称，所在地，尺寸，占总尺寸的百分比
{
	public String storageSeverName;
	public String storageSeverLocation;
	public int storageNumber;
	public String storagePercentage;
	
	public Storage()
	{
	}
	
	String getStorageSeverName(){return storageSeverName;}
	String getStorageSeverLocation(){return storageSeverLocation;}
	int getStorageNumber(){return storageNumber;}
	String getStoragePercentage(){return storagePercentage;}
	
	void setStorageSeverName(String text)
	{
		storageSeverName = text;
	}
	void setStorageSeverLocation(String text)
	{
		storageSeverLocation = text;
	}
	void setStorageNumber(int number)
	{
		storageNumber = number;
	}
	void setStoragePercentage(int number, int total)
	{
		float temp = (float)number / (float)total;
		temp *= 100;
		storagePercentage = temp + "%";
	}
}
