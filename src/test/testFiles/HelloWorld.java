package test.testFiles;

import com.cloud.entities.*;
import java.util.ArrayList;

public class HelloWorld {

	public void sayHello(){
		System.out.println("hello world!");
	}
	public String getStrHello(){
		return "This is hello from Java!";
	}
	public String getStrHello2(){
		return "aaaaaaaaaaaa";
	}
	public Job getOneJobTest(){
		Job g_jobTest = new Job();
		g_jobTest.setStrJobName("wordCount5");
		g_jobTest.setStrJobDescription("this is a wordCount1 job");
		g_jobTest.setiCpuNum(2);//2个cpu
		g_jobTest.setiJobStatus(0);
		g_jobTest.setiMemSize(128);//1G内存
		g_jobTest.setiStorageSize(20);//20G存储
		g_jobTest.setiUserId(1002);
		g_jobTest.setiVmNum(2);//申请1个虚拟机
		return g_jobTest;
	}
	
	public User getAnUser(){
		User user = new User();
		user.setName("frank");
		user.setId("21121189");
		return user;
	}
	
	public static void main(String[] args) {
		Job job = new Job();
		HelloWorld  helloWorld = new HelloWorld();
		job = helloWorld.getOneJobTest();
		System.out.println(job.getStrJobName());
	}
}
