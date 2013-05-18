/**
 * 本类主要处理与上网申请相关的操作。
 */
package com.cloud.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.cloud.entities.NetApply;
import com.cloud.entities.Job;
import com.cloud.utilFun.LogWriter;

/**
 * @author relic
 *
 */
public class NetApplyService {
	private static LogWriter logWriter = LogWriter.getLogWriter();
	private static final int APPLY_PUBLIC_NET = 2;
	public NetApplyService(){}
	
	public static ArrayList<NetApply> getAllNetApplyRecords() throws Exception
	{
		ArrayList<NetApply> result = new ArrayList<NetApply>();
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT * FROM vm_info WHERE net_status = '" + APPLY_PUBLIC_NET + "';"; //查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果			
			while(rs.next()){
				NetApply oneRecord = new NetApply();
				oneRecord.setStrVmId(rs.getString("vm_id"));
				oneRecord.setStrVmName(rs.getString("vm_name"));
				oneRecord.setStrVmIp(rs.getString("vm_ip"));
				oneRecord.setiVmNetStatus(rs.getInt("net_status"));
				int iJobId = rs.getInt("vm_job_id");
				Job tmpJob = JobService.getJobInfoByJobId(iJobId);
				oneRecord.setStrJobName(tmpJob.getStrJobName());
				int iZoneId = JobService.getZoneId(iJobId);
				String strZoneName = ZoneService.getZoneName(iZoneId);
				oneRecord.setStrZoneName(strZoneName);
				UserService us = new UserService();
				String strUserName = us.getUserInfo(tmpJob.getiUserId()).getStrUsername();
				oneRecord.setStrUsrName(strUserName);
				result.add(oneRecord);
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
		return result;
	}
	
	/**
	 * 更新数据库vm_info表中的iNewVmNetStatus字段；
	 * 0表示只能访问局域网、1表示可以访问外网、2表示申请开通外网访问、4表示关闭外网访问。
	 * @param arrStrVmId
	 * @param iNewVmNetStatus
	 * @return
	 */
	public static boolean updateVmNetStatus(ArrayList<String> arrStrVmId, int iNewVmNetStatus)
	{
		boolean ret = true;
		String strVmId = "";
		for(int i=0; i<arrStrVmId.size(); i++)
		{
			strVmId = arrStrVmId.get(i);
			String sql = "update vm_info set net_status = '" +iNewVmNetStatus + "' "
			 + "where vm_id = '" + strVmId + "';";
			if(!ExcuteUpdataSql.excuteUpdateSql(sql)){
				logWriter.log("更新虚拟机网络状态失败！");
				ret = false;
			}
		}
		return ret;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
