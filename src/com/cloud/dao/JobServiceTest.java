/*
 * JobServiceTest
 * @version 1.0,
 * @Date 2012-3-16
 * @author  Frank
*/

package com.cloud.dao;

import java.util.ArrayList;
import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.cloud.entities.Job;

public class JobServiceTest {
	Job g_jobTest = new Job();
	
	@Before
	public void init(){
		g_jobTest.setStrJobName("wordCount5");
		g_jobTest.setStrJobDescription("this is a wordCount1 job");
		g_jobTest.setiCpuNum(2);//2个cpu
		g_jobTest.setiJobStatus(0);
		g_jobTest.setiMemSize(128);//1G内存
		g_jobTest.setiStorageSize(20);//20G存储
		g_jobTest.setiUserId(1002);
		g_jobTest.setiVmNum(2);//申请1个虚拟机
	}
	
	@Test
	/**
	 * 测试A+B的函数。
     *
     */
	public void testAddTest() throws Exception{
		assertEquals(5,JobService.addTest(2, 3));
	}
	
	@Test
	/**
	 * 测试getJobInfoByJobName函数
     *
	 * @throws Exception 
     */
	public void testGetJobInfoByJobId() throws Exception{
		int iJobId = 165;
		Job job = new Job();
		job = JobService.getJobInfoByJobId(iJobId);
		assertEquals(iJobId,job.getiJobId());
	}
	
	
	@Test
	/**
	 * 测试getUserJobs函数
     *
	 * @throws Exception 
     */
	public void testGetUserJobs() throws Exception{
		Job tmpJob = new Job();
		int iUserId = 1002;
		ArrayList<Job> jobInfoList = JobService.getUserJobs(iUserId);
		System.out.println(jobInfoList.size());
		Iterator<Job> it = jobInfoList.iterator();
		while(it.hasNext())
		{
			tmpJob = it.next();
			System.out.println("jobId: "+tmpJob.getiJobId()+"; jobName:" + tmpJob.getStrJobName());
		}
	}
	
	@Test
	/**
	 * 测试addJob；
	 * 测试方法：添加之前记录找不到，添加之后能找到相应的记录。
     *
	 * @throws Exception 
     */
	public void testJobSqlOperation() throws Exception{
		
		try {
			System.out.println("Trying to add a job to database...");
			JobService.addJob(g_jobTest);
			System.out.println("Trying to get a job's info from database...");
			g_jobTest = JobService.getJobInfoByJobId(g_jobTest.getiJobId());
			System.out.println("Trying to update a job's info");
			JobService.updateJobInfo(g_jobTest);
			
			int iJobId = g_jobTest.getiJobId();
			System.out.println("Trying to delete job " + iJobId +" from database.");
			JobService.deleteJob(iJobId);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}		
	
}















