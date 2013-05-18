/*
 * 通过读取当前活动用户ID来显示相应的JOB列表，再通过用户选择的JOB值显示对应的VM列表
 * 
 * 该类仅实现这两个功能
 * 
 * author: Red Star
 * 
 * */
package com.cloud.resourceStatistic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.cloud.dao.MySQLConnection;

public class ResUtilization_ShowJobAndVM 
{
	/*
	 * 通过userid抽取job列表中的jobname和jobid返回给flash，jobname用于显示供用户选择，jobid用于线索寻找对应的VM列表
	 * */
	public ArrayList<Job> showJobByUserId(int userId) throws Exception 
	{
		ArrayList<Job> jobList = new ArrayList<Job>();
		Job tempJob;

		Connection connection = null;
		String sql = "select job_name,job_id from job_info where job_user_id = " + userId;

		try 
		{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) 
			{
				tempJob = new Job();
				tempJob.setJobName(rs.getString("job_name"));
				tempJob.setJobId(rs.getInt("job_id"));
				jobList.add(tempJob);
			}
		} 
		catch (SQLException e) 
		{
			throw new Exception(e);
		} 
		finally 
		{
			try 
			{
				connection.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return jobList;
	}
	
	public ArrayList<VM> showVMByJobId(int jobId) throws Exception//根据jobId得到VM列表
	{
		ArrayList<VM> vmList = new ArrayList<VM>();
		VM tempVM;

		Connection connection = null;
		String sql = "select vm_id, vm_name from vm_info where vm_job_id = " + jobId;

		try 
		{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			while(rs.next())
			{
				tempVM = new VM();
				tempVM.setVMName(rs.getString("vm_name"));
				tempVM.setVMId(rs.getInt("vm_id"));
				vmList.add(tempVM);
			}
		} 
		catch (SQLException e) 
		{
			throw new Exception(e);
		} 
		finally 
		{
			try 
			{
				connection.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		return vmList;
	}
}
