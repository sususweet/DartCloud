package com.cloud.dao;

import java.sql.*;

import com.cloud.entities.User;
import com.cloud.utilFun.LogWriter;

public class UserService {

	public Connection con = null;
	private static LogWriter logWriter = LogWriter.getLogWriter();
	
	public UserService()
	{
		try
		{
			con = MySQLConnection.getInstance().getConnection();
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 在数据库中增加一个用户的信息。
	 * 
	 * @param user 
	 * @return
	 */
	public boolean addAnUser(User user)
	{
		if(null == user)
			return false;
		
		String sql = "INSERT INTO userinfo(user_name,chinese_name,user_password,birthday,gender,phone_number," +
				"address,office_phone_number, user_email,zone_id,zju_sso_uid,user_role) " +
		"VALUES ('" + user.getStrUsername() + "','" + user.getStrChineseName() +  "','" + user.getStrPassword() +  "','" + user.getDateBirthday() +"','" + user.getiGender() + "','" + user.getStrPhoneNumber() + "','" + user.getStrAddress() +  
		"','" + user.getStrOfficePhoneNumber() + "','" + user.getStrUserEmail() + "','" + user.getiUserZoneId() +
		"','" + user.getStrUserZjuSsoUid() + "','" + user.getiUserRole() + "');";
		
		logWriter.log("sql: " + sql);
		
		boolean isSucc = true;
		
		try{
			//创建数据库对象
			Statement statement = con.createStatement();
			statement.executeUpdate(sql);
		}catch(SQLException e){
			logWriter.log("用户信息插入失败！" + e.toString());
			isSucc = false;
		}finally{
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
				isSucc = false;
			}
		}
		return isSucc;
	}
	
	/**
	 * 返回userName的userId;
	 * @param userName
	 * @return
	 */
	public int getUserId(String strZjuSsoUid)
	{
		int iUserId = -1;
		String sql = "SELECT user_id FROM userinfo WHERE zju_sso_uid = '" + strZjuSsoUid + "'";
		try{
			//创建数据库对象
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next())
				iUserId = rs.getInt("user_id");
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return iUserId;
	}
	
	
	/**
	 * 返回userName的userId;
	 * @param userName
	 * @return
	 */
	public boolean isUserIdExist(String strZjuSsoUid)
	{
		boolean bIsExist = false;
		String sql = "SELECT * FROM userinfo WHERE zju_sso_uid = '" + strZjuSsoUid + "'";
		try{
			//创建数据库对象
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next())
				bIsExist = true;
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return bIsExist;
	}
	
	
	/**
	 * 返回userName的zju_sso_uid,即浙大统一认证账号的id;
	 * @param userName
	 * @return
	 */
	public String getZjuSsoUid(String userName)
	{
		String strZjuSsoUid = "";
		String sql = "SELECT zju_sso_uid FROM userinfo WHERE user_name = '" + userName + "'";
		try{
			//创建数据库对象
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			if(rs.next())
				strZjuSsoUid = rs.getString("zju_sso_uid");
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return strZjuSsoUid;
	}
	
	
	/**
	 * 查看某一用户是否已经存在。
	 * @param strUsername
	 * @return
	 * @throws Exception
	 */
	public boolean isUsernameExist(String strUsername) throws Exception
	{
		//默认该业务名不存在
		boolean isUsernameExist = false;
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT * FROM userinfo WHERE user_name = '" + strUsername + "' ;";//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果			
			if(rs.next())
				isUsernameExist = true;
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
		return isUsernameExist;
	}
	
	/**
	 * 获取某一用户的一些相关信息：如用户名，所在单位，邮件地址等。
	 * @param iUserId
	 * @return
	 */
	public User getUserInfo(int iUserId) throws Exception
	{
		User user = new User();
		String sql = "SELECT * FROM userinfo WHERE user_id = '" + iUserId + "'";
		Connection connection = null; //定义连接对象
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果
			if(rs.next())
			{
				user.setiUserId(iUserId);
				user.setiUserRole(rs.getInt("user_role"));
				user.setiUserZoneId(rs.getInt("zone_id"));
				user.setiUserStatus(rs.getInt("user_status"));
				user.setiAccountBalance(rs.getInt("account_balance"));
				user.setStrUsername(rs.getString("user_name"));
				user.setStrPassword(rs.getString("user_password"));
				user.setStrUserDep(rs.getString("user_department"));
				user.setStrUserEmail(rs.getString("user_email"));
				user.setStrLoginIp(rs.getString("login_ip"));
				user.setStrChineseName(rs.getString("chinese_name"));
				user.setStrUserZjuSsoUid(rs.getString("zju_sso_uid"));
//				user.setDateBirthday(rs.getString("birthday"));
				user.setiGender(rs.getInt("gender"));
				user.setStrPhoneNumber(rs.getString("phone_number"));
				user.setStrAddress(rs.getString("address"));
				user.setStrOfficePhoneNumber(rs.getString("office_phone_number"));
				user.setiUserZoneId(rs.getInt("zone_id"));
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
		return user;
	}
	
	public boolean updateUserInfo(User user) throws Exception
	{
		if(null == user)
			return false;
		
		String sql = "UPDATE userinfo SET user_name='" + user.getStrUsername() 
		+ "', user_email='" +user.getStrUserEmail() + "', user_role='" + user.getiUserRole() + "', account_balance='" + user.getiAccountBalance() 
		+ "' where zju_sso_uid='" + user.getStrUserZjuSsoUid() +"';";
		
		logWriter.log("sql: " + sql);
		
		boolean isSucc = true;
		
		try{
			//创建数据库对象
			Statement statement = con.createStatement();
			statement.executeUpdate(sql);
		}catch(SQLException e){
			logWriter.log("用户信息更新失败！" + e.toString());
			isSucc = false;
		}finally{
			try{
				con.close();
			}catch(Exception e){
				e.printStackTrace();
				isSucc = false;
			}
		}
		if(isSucc)
			logWriter.log("用户" + user.getStrUserZjuSsoUid() + "的信息更新成功！");
		return isSucc;
	}
	
	
	/**
	 * 判断是否允许用户再次申请iCurApplyNum个虚拟机；每个用户都有申请虚拟机个数的上限，目前是10个。
	 * @param iUserId	用户id
	 * @param iCurApplyNum	本次申请的虚拟机
	 * @return
	 * @throws Exception
	 */
	public boolean isAllowUserApplyMoreVMs(int iUserId, int iCurApplyNum) throws Exception
	{
		boolean bRet = false;
		Connection connection = null; //定义连接对象
		try{
			String strSql = "SELECT * FROM userinfo WHERE user_id = '" + iUserId + "' ;";//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(strSql); //获取查询结果			
			if(rs.next())
			{
				int iMaxVmNum = rs.getInt("vm_apply_max_num");
				int iCurTotalVmNum = VmService.getUserApplyVMNum(iUserId);
				if(iCurTotalVmNum+iCurApplyNum <= iMaxVmNum) //已经申请的数目+本次申请的数目 <= 最大允许申请的数目
					bRet = true;
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
		return bRet;
	}
	
	
	public static void main(String args[]) throws Exception 
	{
//		User user = new User();
		UserService us = new UserService();
//		user = us.getUserInfo(1044);
//		logWriter.log("Result: strZjuSsoUid=" + user.getStrUserZjuSsoUid()
//				+", Email="+user.getStrUserEmail() + ", strUserName=" + user.getStrUsername()
//				+",balance="+user.getiAccountBalance());
		System.out.println("res: " + us.isAllowUserApplyMoreVMs(1044,4));
	}
	
}
