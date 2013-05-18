/**
 * 
 */
package com.cloud.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author relic
 *
 */
public class ExcuteUpdataSql {

	/**
	 * 
	 */
	public ExcuteUpdataSql() {
	}

	/**
	 * 执行一条sql语句。
	 * @param sql
	 * @return
	 */
	public static boolean excuteUpdateSql(String sql)
	{
		boolean isSucc = false; 
		Connection connection = null; //定义连接对象
		int result = 0; //定义变量存放执行结果
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(result != 0){ //执行成功
			isSucc = true;
		}
		return isSucc;
	}
}
