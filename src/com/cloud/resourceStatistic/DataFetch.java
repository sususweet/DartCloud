package com.cloud.resourceStatistic;
import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStream; 
import java.io.InputStreamReader; 
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import java.util.ArrayList;
import java.util.Date;

import com.cloud.config.DataCenterInfo;

public class DataFetch {
	public static ArrayList<Float> data;
	
	public static void computData(StatisticModel sm){
		data = new ArrayList<Float>();
		String dataSourceFile = sm.getDataSourceFile();
		String startTime = sm.getStartTime();
		String endTime = sm.getEndTime();
		String timeUnit = sm.getTimeUnit();
		
		DataCenterInfo dataCenterInfo = DataCenterInfo.getInstance();
		
		String hostname = dataCenterInfo.getMainHostIp(); 
		String username = dataCenterInfo.getUsername();
		String password = dataCenterInfo.getPassword();
		
		try 
		{ 
			/* Create a connection instance */ 
			Connection conn = new Connection(hostname); 
			/* Now connect */ 
			conn.connect(); 
			/* Authenticate */ 
			boolean isAuthenticated = conn.authenticateWithPassword(username, password); 
			if (isAuthenticated == false) 
				throw new IOException("获取数据失败！"); 
			/* Create a session */ 
			Session sess = conn.openSession(); 
			//sess.execCommand("uname -a && date && uptime && who"); 
			String fecthCMD = "rrdtool fetch " + dataSourceFile + " AVERAGE -s " + startTime + " -e " + endTime;
			System.out.println(fecthCMD);
			System.out.println(sm.startTime);
			System.out.println(sm.endTime);
			sess.execCommand(fecthCMD);
			
			
			InputStream stdout = new StreamGobbler(sess.getStdout()); 
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			if(timeUnit.equals("日")){
				int year = sm.reserveTime1;
				int month = sm.reserveTime2;
				computByDayUnit(year, month, br);
			}
			else if(timeUnit.equals("月")){
				int year = sm.reserveTime1;
				computeByMonthUnit(year, br);
			}
//			while (true) 
//			{ 
//				String line = br.readLine();
//				
//				if (line == null) 
//				break; 
//				String[] a = line.split(": ");
//				//System.out.println(line);
//				if(a[1].equals("-nan"))
//					continue;
//				
//				
//				float num = Float.parseFloat(a[1]);
//				data.add(num);
//			} 
			/* Show exit status, if available (otherwise "null") */ 
			System.out.println("ExitCode: " + sess.getExitStatus()); 
			/* Close this session */ 
			sess.close(); 
			/* Close the connection */ 
			conn.close(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(System.err); System.exit(2); 
		}
	}
	
	public static void computByDayUnit(int year, int month, BufferedReader br) throws IOException{
		br.readLine();
		br.readLine();
		Date startDate = new Date(year-1900, month, 0, 0, 0);
		Date endDate = new Date(year-1900, month, 0, 23, 23, 59);
		String startTime = "";
		String endTime = "";
		ArrayList<Float> tmpData = new ArrayList<Float>();
		int monthLastDay = getLastDayOfMonth(year, month);
		int day = 1;
		String line = br.readLine();
		String[] a = line.split(": ");
		while(day <= monthLastDay){
			tmpData.clear();
			startDate.setDate(day);
			endDate.setDate(day);
			//System.out.println(startDate);
			//System.out.println(endDate);
			
			startTime = (startDate.getTime()+"").substring(0, 10);
			endTime = (endDate.getTime()+"").substring(0, 10);
			
			if (line == null) 
			break;
			
			while((a[0].compareTo(startTime) > 0 || a[0].compareTo(startTime) == 0) && (a[0].compareTo(endTime) < 0 || a[0].compareTo(endTime) == 0)){
				if(!a[1].equals("-nan")){
					tmpData.add(Float.parseFloat(a[1]));
				}
				line = br.readLine();
				a = line.split(": ");
				if (line == null)
				break;
			}
			float tmpSum = 0, tmpAver = 0;
			if(tmpData.size()==0){
				tmpAver = 0;
			}
			else{
				for(int i = 0; i < tmpData.size(); i++){
					tmpSum += tmpData.get(i);
				}
				tmpAver = tmpSum / tmpData.size();
			}
			data.add(tmpAver);
			day++;
		}
		
	}
	
	public static void computeByMonthUnit(int year, BufferedReader br) throws IOException{
		int monthIndex = 1;
		int lastDayOfMonth;
		br.readLine();
		br.readLine();
		Date startDate = new Date(year-1900, 1, 1, 0, 0);
		Date endDate = new Date(year-1900, 1, 0, 23, 23, 59);
		String startTime = "";
		String endTime = "";
		ArrayList<Float> tmpData = new ArrayList<Float>();
		String line = br.readLine();
		String[] a = line.split(": ");
		while(monthIndex <= 12){
			tmpData.clear();
			lastDayOfMonth = getLastDayOfMonth(year, monthIndex);
			startDate.setMonth(monthIndex);
			endDate.setMonth(monthIndex);
			endDate.setDate(lastDayOfMonth);
			//System.out.println(startDate);
			//System.out.println(endDate);
			
			startTime = (startDate.getTime()+"").substring(0, 10);
			endTime = (endDate.getTime()+"").substring(0, 10);
			
			if (line == null) 
			break;
			
			while((a[0].compareTo(startTime) > 0 || a[0].compareTo(startTime) == 0) && (a[0].compareTo(endTime) < 0 || a[0].compareTo(endTime) == 0)){
				if(!a[1].equals("-nan")){
					tmpData.add(Float.parseFloat(a[1]));
				}
				line = br.readLine();
				a = line.split(": ");
				if (line == null)
				break;
			}
			float tmpSum = 0, tmpAver = 0;
			if(tmpData.size()==0){
				tmpAver = 0;
			}
			else{
				for(int i = 0; i < tmpData.size(); i++){
					tmpSum += tmpData.get(i);
				}
				tmpAver = tmpSum / tmpData.size();
			}
			data.add(tmpAver);
			monthIndex++;
		}
	}
	
	protected static int getLastDayOfMonth(int year, int month){
		int monthLastDay;
		if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
			monthLastDay = 31;
		}
		else if(month == 4 || month == 6 || month == 9 || month == 11){
			monthLastDay = 30;
		}
		else if(month == 2){
			if((year%4 == 0 && year % 100 != 0) || year % 400 == 0)
				monthLastDay = 29;
			else monthLastDay = 28;
		}
		else{
			monthLastDay = -1;
		}
		return monthLastDay;
	}
	
	public static ArrayList<Float> getData(StatisticModel sm){
		computData(sm);
		return data;
	}
	
	public static void main(String[] args){
		StatisticModel sm = new StatisticModel();
		sm.setStartTime(2012, 1, 1, 0, 0, 0);
		sm.setEndTime(2012, 12, 31, 23, 59, 59);
		
//		System.out.println(sm.getStartTime());
//		System.out.println(new Date(2012-1900, 6, 1, 0, 0, 0).getTime()+"");
		
		sm.setTimeUnit("月");
		sm.setDataSource("/var/lib/ganglia/rrds/dartcloud/debian2/cpu_user.rrd");
		sm.reserveTime1 = 2012;
		//sm.reserveTime2 = 6;
		DataFetch.computData(sm);
		for(int i = 0; i< data.size(); i++){
			System.out.print(data.get(i)+ " ");
		}
	}

}
