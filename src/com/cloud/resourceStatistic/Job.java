package com.cloud.resourceStatistic;

public class Job//封装JOB对象，包含jobId和jobName
{
	public String jobName;
	public int jobId;
	
	public Job()
	{
	}
	
	String getJobName(){return jobName;}
	int getJobId(){return jobId;}
	
	void setJobName(String text)
	{
		jobName = text;
	}
	void setJobId(int number)
	{
		jobId = number;
	}
}
