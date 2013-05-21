/*
 * JobOperation
 * @version 1.0,
 * @Date 2013-5-21
 * @author  Frank
*/
package com.cloud.jobManagement;

import org.junit.Before;
import org.junit.Test;
import com.cloud.entities.*;
import com.cloud.entities.Job;


public class JobOperationTest {
	//由于有6个域，所以应该要分别在每个域内新建一个业务，看是否有问题；
//	private int JOB_NUM = 6;
//	Job m_jobTest[] = new Job[6];
	Job myJob = new Job();
	//JUnit进行测试时，每调用一个测试函数就会新建一个测试对象.所有要用static.
	private int m_iJobId = 0;
	private int m_iCpuNum = 1;
	private int iMemSize = 1024;//1024m=1G
	private int m_iStorageSize = 0; //这个选项暂时是没有用的;
	private int m_iVmNum = 1;
	private int m_iUserId = 1044;//frank's id
	private int m_iOsType = OsType.LINUX_UBUNTU;//目前只支持ubuntu操作系统；
	/**
	 * 域与iZoneId的对应关系：
	 * 1-玉泉， 2-西溪， 3-计算所，
	 * 4-清华，5-自动化所，哈工大；
	 */
	private int m_iZoneId = 2; 
	private String m_strJobName = "xixi_test";
	private int m_iNetStatus = 2;
	
	
	@Before
	public void init(){
//		m_jobTest.setiJobId(m_iJobId); //需要数据库返回，所以不能预先设定。
//		for(int i=0; i<JOB_NUM; i++)
//		{
			myJob.setiCpuNum(m_iCpuNum);
			myJob.setiStorageSize(m_iStorageSize);
			myJob.setiVmNum(m_iVmNum);
			myJob.setiUserId(m_iUserId);
			myJob.setiOSType(m_iOsType);
			myJob.setiZoneId(m_iZoneId);
			myJob.setiOSType(m_iOsType);
			myJob.setiZoneId(2); 
			myJob.setStrJobName(m_strJobName);
			myJob.setiNetStatus(m_iNetStatus);
		//}
	}
	
	
	@Test
	public void testCreateOneJob(){
		System.out.println("creating jobs in different zones......");
		try{
//			for(int i=1; i<=JOB_NUM; i++)
//			{
				System.out.println("creating job in zone " + myJob.getiZoneId() + " :");
				JobOperation.createOneJob(myJob);
		 		int iJobId = myJob.getiJobId();
		 		myJob.setiJobId(iJobId);
		 		System.out.println("Jobid :" + m_iJobId);
		 		System.out.println("JobName :" + myJob.getStrJobName());
			//}
	 		
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testDeleteOneJob() throws Exception{
		System.out.println("deleting jobs in different zones......");
		try{
//			for(int i=1; i<=JOB_NUM; i++)
//			{
				System.out.println("creating job in zone " + myJob.getiZoneId() + " :");
				JobOperation.deleteOneJob(myJob.getiJobId());
		 		System.out.println("Jobid :" + m_iJobId);
		 		System.out.println("JobName :" + myJob.getStrJobName());
			//}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	

}
