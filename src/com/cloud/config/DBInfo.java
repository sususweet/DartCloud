
package com.cloud.config;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**数据库配置信息，单例.
 * 
 * @author Yan Haiming
 *
 */

public class DBInfo 
{
	static private DBInfo _instance = null;
	private String url = null;
	private String username = null;
	private String password = null;
	
	private DBInfo()
	{
		try{
			
			//获取绝对路径
			String filePath = this.getClass().getResource("/").toString().substring(5) + "conf/dataBase.properties";
			
			File file = new File(filePath);
			InputStream filestream = new FileInputStream(file);
			
		
			
			Properties props = new Properties();
			props.load(filestream);
			url = props.getProperty("url");
			username = props.getProperty("username");
			password = props.getProperty("password");
			
			
			filestream.close();
			

			
		}
		catch(Exception e){
			System.out.println("error: " + e);
		}
		
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}


	static public synchronized DBInfo getInstance()
	{
		if(_instance == null)
			_instance = new DBInfo();
		return _instance;
	}
	
	public static void main(String[] args)
	{
		DBInfo dbInfo = DBInfo.getInstance();
		System.out.println(dbInfo.url);
		System.out.println(dbInfo.username);
		System.out.println(dbInfo.password);

		String url = "jdbc:mysql://192.168.0.64:3306/chinacloud";
		String username = "root";
		String password = "19861225";
		System.out.println(url);
		System.out.println(username);
		System.out.println(password);
	}

}
