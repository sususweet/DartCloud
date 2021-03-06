/**
 * 
 */
package com.cloud.testCases;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.cloud.entities.Job;
import com.cloud.entities.OsType;
import com.cloud.jobManagement.JobOperation;


/**
 * @author Administrator
 *
 */
public class Tsinghua_jobTest {
	//JUnit进行测试时，每调用一个测试函数就会新建一个测试对象.所有要用static.
	static Job tsinghua_jobTest = new Job();
	
	private int m_iJobId = 0;
	private int m_iCpuNum = 1;
	private int m_iMemSize = 1024;//1024m=1G
	private int m_iStorageSize = 0; //这个选项暂时是没有用的;
	private int m_iVmNum = 1;
	private int m_iUserId = 1044;//frank's id
	private int m_iOsType = OsType.LINUX_UBUNTU;//目前只支持ubuntu操作系统；
	/**
	 * 域与iZoneId的对应关系：
	 * 1-玉泉， 2-西溪， 3-计算所，
	 * 4-清华，5-自动化所，哈工大；
	 */
	private int m_iZoneId = 4; 
	private String m_strJobName = "xixi_test";
	private String m_strVmPass="111111"; //虚拟机的root和cloud用户密码;
	private int m_iNetStatus = 2;
	
	
	@Before
	public void init(){
		//m_iJobId需要数据库返回，所以不能预先设定。
		tsinghua_jobTest.setiCpuNum(m_iCpuNum);
		tsinghua_jobTest.setiMemSize(m_iMemSize);
		tsinghua_jobTest.setiStorageSize(m_iStorageSize);
		tsinghua_jobTest.setiVmNum(m_iVmNum);
		tsinghua_jobTest.setiUserId(m_iUserId);
		tsinghua_jobTest.setiOSType(m_iOsType);
		tsinghua_jobTest.setiZoneId(m_iZoneId);
		tsinghua_jobTest.setiOSType(m_iOsType);
		tsinghua_jobTest.setiZoneId(m_iZoneId); 
		tsinghua_jobTest.setStrJobName(m_strJobName);
		tsinghua_jobTest.setstrVmPass(m_strVmPass);
		tsinghua_jobTest.setiNetStatus(m_iNetStatus);
	}
	
	
	@Test
	public void testCreateOneJob(){
		System.out.println("creating jobs in different zones......");
		try{
			System.out.println("creating job in zone " + tsinghua_jobTest.getiJobId() + " :");
			assertTrue("creating tsinghua_jobTest result:",JobOperation.createOneJob(tsinghua_jobTest));
	 		int iJobId = tsinghua_jobTest.getiJobId();
	 		tsinghua_jobTest.setiJobId(iJobId);
	 		System.out.println("Jobid :" + m_iJobId);
	 		System.out.println("JobName :" + tsinghua_jobTest.getStrJobName());
	 		
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testDeleteOneJob() throws Exception{
		System.out.println("deleting jobs in different zones......");
		try{
			System.out.println("creating job in zone " + tsinghua_jobTest.getiJobId() + " :");
			assertTrue("Deleting tsinghua_jobTest result:",JobOperation.deleteOneJob(tsinghua_jobTest.getiJobId()));
	 		System.out.println("Jobid :" + m_iJobId);
	 		System.out.println("JobName :" + tsinghua_jobTest.getStrJobName());
		}catch(Exception e){
			System.out.println(e);
		}
	}

}
