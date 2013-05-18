package com.cloud.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ZoneService {

	//这里的数据与前台绑定，不要轻易修改
	public static final int ZONE_HOST = 4;
	public static final int LAN_IP = 2;
	public static final int PUBLIC_IP = 0;
	public static final int SCHOOL_IP = 1;
	
	/**
	 * 通过一个业务iJobId来获取该业务所在域的zone_host_ip信息。
	 * 一般这个IP也是VNC和webssh的代理服务器IP。
	 * @param iJobId 业务的id号
	 * @param iFlag 标识：LAN_IP返回局域网IP；SCHOOL_IP返回校园网IP；
	 * PUBLIC_IP返回公网IP。
	 * @return
	 * @throws Exception
	 */
	public static String getZonehostIp(int iZoneId, int iFlag)throws Exception{
		String strHostIp = null;
		Connection connection = null; //定义连接对象
		ResultSet rs = null; //定义变量存放执行结果
		try{
			String sql = "select * from zone_info where zone_id = " + iZoneId;
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if(rs == null){
				System.out.println("没有找到zone_host信息！");
			}
			else{
				rs.next();
				if(iFlag == LAN_IP)
				{
					strHostIp = rs.getString("zone_host_LAN_ip");
				}
				else if(iFlag == SCHOOL_IP)
				{
					strHostIp = rs.getString("zone_host_school_ip");
				}
				else if(iFlag == PUBLIC_IP)
				{
					strHostIp = rs.getString("zone_host_public_ip");
				}
				else if(iFlag == ZONE_HOST)
				{
					strHostIp = rs.getString("zone_host");
				}
				else
				{
					System.out.println("参数错误");
					return null;
				}
			}
		}catch(SQLException e){
			System.out.println("域"+ iZoneId +"的zone_host信息获取失败！");
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return strHostIp;
	}
	

	/**
	 * 根据iZoneId获得zone的secret信息。
	 * @param iZoneId
	 * @return
	 * @throws Exception
	 */
	public static String getZoneSecret(int iZoneId) throws Exception
	{
		Connection connection = null; //定义连接对象
		String zoneSecret = null;
		try{
			String sql = "SELECT zone_secret FROM zone_info WHERE zone_id = " + iZoneId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
			if(rs.next()){
				zoneSecret = rs.getString("zone_secret");
			}
		}catch(SQLException e){
			throw new Exception(e);
		}finally{
			//finally块中的代码总是执行的。
			try{
				//关闭数据库，如果已打开连接。执行完毕后要关闭，释放资源
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return zoneSecret;
	}
	
	/**
	 * 获得iZoneId的域名，如果不存在，则返回null
	 * @param iZoneId
	 * @return
	 * @throws Exception
	 */
	public static String getZoneName(int iZoneId) throws Exception
	{
		String strZoneName = null;
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT zone_chinese_name FROM zone_info WHERE zone_id = " + iZoneId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
			if(rs.next())
				strZoneName = rs.getString("zone_chinese_name");
		}catch(SQLException e){
			throw new Exception(e);
		}finally{
			//finally块中的代码总是执行的。
			try{
				//关闭数据库，如果已打开连接。执行完毕后要关闭，释放资源
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return strZoneName;
	}
	
	public static void main(String args[]) throws Exception
	{
		System.out.println(getZoneName(1));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
