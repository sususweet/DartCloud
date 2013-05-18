package com.cloud.config;


import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * 数据中心配置信息，单例.
 * 
 * @author Yan Haiming
 *
 */

public class DataCenterInfo 
{
	static private DataCenterInfo _instance = null;
	private String secret = null;
	private String endpoint = null;
	private String mainHostIp = null;
	private String monitorFileDir = null;
	private String username = null;
	private String password = null;
	private String tomcatRootDir = null;
	private String publiDirName = null;
	private String hadoopGenerDataDir = null;
	
	private DataCenterInfo()
	{
		try{
			//获取Tomcat下本项目的根目录。
			String projectRootDir = this.getClass().getResource("/").toString().substring(5);
			
//			System.out.println(projectRootDir);
			
//			int endIndex = projectRootDir.lastIndexOf("classes/");
//			tomcatRootDir = projectRootDir.substring(0, endIndex);
//			System.out.println("tomcatRoot:  " + tomcatRootDir);
			
			//获取绝对路径。
			String filePath = projectRootDir + "conf/dataCenter.properties";
			
			File file = new File(filePath);
			InputStream filestream = new FileInputStream(file);
			
			Properties props = new Properties();
			props.load(filestream);
			secret = props.getProperty("secret");
			endpoint = props.getProperty("endpoint");
			mainHostIp = props.getProperty("mainHostIp");
			monitorFileDir = projectRootDir + props.getProperty("monitorFileDir");
			username = props.getProperty("username");
			password = props.getProperty("password");
			publiDirName = props.getProperty("publiDirName");
			tomcatRootDir = props.getProperty("tomcatRootDir");
			hadoopGenerDataDir = props.getProperty("hadoopGenerDataDir");
			
			
			filestream.close();
		}
		catch(Exception e){
			System.out.println("error: " + e);
		}
		
	}
	
	

	public String getHadoopGenerDataDir() {
		return hadoopGenerDataDir;
	}



	public void setHadoopGenerDataDir(String hadoopGenerDataDir) {
		this.hadoopGenerDataDir = hadoopGenerDataDir;
	}


	public String getPubliDirName() {
		return publiDirName;
	}

	public void setPubliDirName(String publiDirName) {
		this.publiDirName = publiDirName;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setMainHostIp(String mainHostIp) {
		this.mainHostIp = mainHostIp;
	}

	public void setMonitorFileDir(String monitorFileDir) {
		this.monitorFileDir = monitorFileDir;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTomcatRootDir(String tomcatRootDir) {
		this.tomcatRootDir = tomcatRootDir;
	}

	public String getTomcatRootDir(){
		return tomcatRootDir;
	}
	
	public String getSecret() {
		return secret;
	}

	public String getEndpoint() {
		return endpoint;
	}
	
	public String getMainHostIp() {
		return mainHostIp;
	}
	
	public String getMonitorFileDir() {
		return monitorFileDir;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	static public synchronized DataCenterInfo getInstance()
	{
		if(_instance == null)
			_instance = new DataCenterInfo();
		return _instance;
	}
	
	public void test(){
//		String projectRootDir = this.getClass().getResource("/").toString().substring(5);
//		System.out.println(projectRootDir);
	}
	
	public static void main(String[] args)
	{
		DataCenterInfo dc = new DataCenterInfo();
		dc.test();
	}

}
