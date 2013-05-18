package com.cloud.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import com.cloud.config.DBInfo;;

/*
* @author  Frank
* @version 1.0, 2012-3-14
*/

public class MySQLConnection {
	//定义一个静态属性，也就是MySQLConnection对象唯一的实例
	private static MySQLConnection instance;
	//构造函数，private类型，外部无法访问，不允许外部通过构造函数创建实例
	private MySQLConnection(){
		//调用数据库驱动
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	//获得对象的唯一实例
	public static MySQLConnection getInstance(){
		//如果对象还没有被初始化，则调用构造函数
		if(instance == null){
			instance = new MySQLConnection();
		}
		return instance;
	}
	//获得数据库连接，并捕捉异常
	public Connection getConnection() throws java.sql.SQLException{
		//定义数据库信息，如果有变动，只需用修改一次
		DBInfo dbInfo = DBInfo.getInstance();
		
		String url = dbInfo.getUrl();
		String user = dbInfo.getUsername();
		String password = dbInfo.getPassword();
		String connStr = url + "?user=" + user + "&password="+password+"&useUnicode=true&characterEncoding=utf8";
		return DriverManager.getConnection(connStr);
	}

}
