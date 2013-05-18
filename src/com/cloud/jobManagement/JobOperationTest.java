/*
 * JobOperation
 * @version 1.0,
 * @Date 2012-3-23
 * @author  Frank
*/
package com.cloud.jobManagement;

import org.junit.Before;
import org.junit.Test;

import com.cloud.entities.Job;


public class JobOperationTest {
	Job jobTest = new Job();
	
	//JUnit进行测试时，每调用一个测试函数就会新建一个测试对象.所有要用static.
	static int iJobId = 0;
	
	@Before
	public void init(){
 		jobTest.setStrJobName("this_is_a test_job");
 		jobTest.setStrJobDescription("this is a sort job");
 		jobTest.setiCpuNum(1);
 		jobTest.setiJobStatus(0);
 		jobTest.setiMemSize(1024);//128M内存
 		jobTest.setiStorageSize(20);//20G存储
 		jobTest.setiUserId(1002);
 		jobTest.setiVmNum(1);//申请1个虚拟机
	}
	
	@Test
	public void testCreateOneJob(){
		System.out.println("creating a job......");
		try{
	 		JobOperation.createOneJob(jobTest);
	 		iJobId = jobTest.getiJobId();
	 		System.out.println("Jobid :" + iJobId);
	 		System.out.println("JobName :" + jobTest.getStrJobName());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testDeleteOneJob() throws Exception{
		//System.out.println("deleting a job......" + iJobId);
		//JobOperation.deleteOneJob(iJobId);
	}
	

}
