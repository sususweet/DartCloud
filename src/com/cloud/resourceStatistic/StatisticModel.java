package com.cloud.resourceStatistic;
import java.util.Date;
public class StatisticModel {
	public Date startTime;
	public Date endTime;
	public String timeUnit;
	public String dataSourceFile;
	public int reserveTime1;
	public int reserveTime2;
	
	@SuppressWarnings("deprecation")
	public void setStartTime(int year, int month, int day, int hour, int munite, int second){
		startTime = new Date(year-1900, month-1, day, hour, munite, second);
	}
	
	@SuppressWarnings("deprecation")
	public void setEndTime(int year, int month, int day, int hour, int munite, int second){
		endTime = new Date(year-1900, month-1, day, hour, munite, second);
	}
	
	public void setTimeUnit(String timeUnit){
		this.timeUnit = timeUnit;
	}
	
	public void setDataSource(String file){
		dataSourceFile = file;
	}
	
	public String getStartTime(){
		return (startTime.getTime()+"").substring(0,10);
		//return "1338346296";
	}
	
	public String getEndTime(){
		return (endTime.getTime()+"").substring(0,10);
		//return "1338515136";
	}
	
	
	
	public String getTimeUnit(){
		return timeUnit;
	}
	
	public String getDataSourceFile(){
		return dataSourceFile;
	}
	
	
}
