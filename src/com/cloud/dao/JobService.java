/*
 * JobService
 * @version 1.0,
 * @Date 2012-3-14
 * @author  Frank
*/

package com.cloud.dao;

import com.cloud.entities.*;
import com.cloud.utilFun.LogWriter;

import java.sql.*;
import java.util.ArrayList;

/**
 * 业务(Job)级别的数据库连接服务.
 * 
 * @author Yan Haiming
 *
 */

public class JobService {
	
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public JobService(){
	}
	
	/**
     * 根据业务标识（JobId）来获得该业务对应的信息。
     *
     * @param iJobId 业务标识
	 * @throws Exception 
     */
	public static Job getJobInfoByJobId(int iJobId) throws Exception{
		String sql = "SELECT * FROM job_info WHERE job_id = '" + iJobId + "'";
		Connection connection = null; //定义连接对象
		Job job = new Job();
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果
			if(rs.next())
			{
				job.setiJobId(rs.getInt("job_id"));
				job.setStrJobName(rs.getString("job_name"));
				job.setiUserId(rs.getInt("job_user_id"));
				job.setStrJobDescription(rs.getString("job_description"));
				job.setiJobStatus(rs.getInt("job_status"));
				job.setiStorageSize(rs.getInt("job_storage"));
				job.setDateJobStartTime(rs.getDate("job_start_time"));
				int iZoneId = rs.getInt("zone_id");
				job.setiZoneId(iZoneId);
				job.setStrZoneName(ZoneService.getZoneName(iZoneId));
				job.setStrZoneHost(ZoneService.getZonehostIp(iZoneId, ZoneService.ZONE_HOST));
				job.setStrZoneLAN_Ip(ZoneService.getZonehostIp(iZoneId, ZoneService.LAN_IP));
				job.setStrZonePublicIp(ZoneService.getZonehostIp(iZoneId, ZoneService.PUBLIC_IP));
				job.setStrZoneSchoolIp(ZoneService.getZonehostIp(iZoneId, ZoneService.SCHOOL_IP));
				job.setVmArrayInfo( getVmInfoListInAJob(job.getiJobId()));
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
		return job;
	}
	
	/**
     * 本函数根据用户的ID从业务信息表中查询所有属于该用户的业务信息，
     * 并以ArrayList的形式返回。
     *
     * @param iUserId 用户ID
	 * @throws Exception 
     */
	public static ArrayList<Job> getUserJobs(int iUserId) throws Exception{
		ArrayList<Job> jobInfoList = new ArrayList<Job>();
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT job_id FROM job_info WHERE job_user_id = " + iUserId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果			
			while(rs.next()){
				Job tmpJob = new Job();	
				int iJobId = rs.getInt("job_id");
				tmpJob = getJobInfoByJobId(iJobId);
				jobInfoList.add(tmpJob);
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
		return jobInfoList;
	}
	
	/**
     * 字段job_id是以自增的方式生成的，此函数返回插入业务信息时所产生的job_id.
     *
     * @param sql 待执行的sql语句.
     */
	public static int excuteAndGetJobId(String sql)throws Exception{
		Connection connection = null; //定义连接对象
		ResultSet rs = null;
		Statement statement = null;
		int result = -1; //定义变量存放执行结果
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			statement = connection.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
			rs = statement.getGeneratedKeys();
			if(rs.next()){
				result = rs.getInt(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new Exception(e);
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				throw new Exception(e);
			}
		}
		
		return result;
	}

	/**
     * 向数据库中添加一条业务信息记录
     *
     * @param job 业务对象
     */
	public static int addJob(Job job) throws Exception{
		String strJobName = job.getStrJobName();
		int iUserId = job.getiUserId();
		String strJobDescription = job.getStrJobDescription();
		int iJobStatus = job.getiJobStatus();
		int iJobStorage = job.getiStorageSize();
		int iZoneId = job.getiZoneId();
		int iJobId = 0;
		
		//定义SQL语句，now()函数是MySQL的内置函数，获得当前具体时间
		//除了job_id为自动增长字段外，其他字段均要手动写入；
		String sql = "INSERT INTO job_info(job_name,job_user_id,job_description,job_status,job_storage,zone_id)" +
				"VALUES ('" + strJobName + "'," + iUserId + ",'" + strJobDescription + "'," + iJobStatus
				+ "," + iJobStorage + "," + iZoneId + ");";
		try{
			iJobId = excuteAndGetJobId(sql);
			job.setiJobId(iJobId);//由于是引用传递，故可修改传入的job对象；
		}catch(Exception e)
		{
			throw new Exception(e);
		}
		return iJobId;
	}
	
	/**
     * 从数据库中删除一条业务信息记录。
     * 成功返回true，否则返回false
     *
     * @param iJobId 业务标识
	 * @throws Exception 
     */
	public static boolean deleteJob(int iJobId) throws Exception{
		
		boolean isSucc = true; 
		
		String sql = "DELETE FROM job_info WHERE job_id = " + iJobId + ";";
		try{
			if(!excuteUpdateSql(sql))
			{
				isSucc = false;
				logWriter.log("Fail to delete job in database!");
			}
		}catch(Exception e)
		{
			throw new Exception(e);
		}
		return isSucc;
	}
	
	/**
	 * 更新数据库中的一条业务信息。
     * 成功返回true，否则返回false
     *
     * @param job 业务对象
	 * @throws Exception 
     */
	public static boolean updateJobInfo(Job job) throws Exception{
		
		int iJobId = job.getiJobId();
		String strJobName = job.getStrJobName();
		int iUserId = job.getiUserId();
		String strJobDescription = job.getStrJobDescription();
		int iJobStatus = job.getiJobStatus();
		int iJobStorage = job.getiStorageSize();
		boolean isSucc = false; 
		
		//,,,job_start_time
		String sql = "UPDATE job_info SET job_name = '" + strJobName + "',job_user_id = " + iUserId + ",job_description = '" 
			+ strJobDescription + "',job_status = " + iJobStatus + ",job_storage = " + iJobStorage +" WHERE job_id = " + iJobId + ";";
		try{
			isSucc = excuteUpdateSql(sql);
			if(!isSucc)
			{
				logWriter.log("添加业务失败");
			}
		}catch(Exception e)
		{
			throw new Exception(e);
		}
		return isSucc;
	}

	/**
	 * 本函数执行一些不需要返回具体结果的函数，包括：插入、删除、更新；
     *
     * @param sql 待执行的sql语句
	 * @throws Exception 
     */
	public static boolean excuteUpdateSql(String sql) throws Exception
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
				throw new Exception(e);
			}
		}
		if(result != 0){ //执行成功
			isSucc = true;
		}
		return isSucc;
	}
	
	
	/**
     * 从虚拟机信息表(vm_info)中查找所有属于特定业务的虚拟机id
     * 并以数组ArrayLis的形式返回；
     *
     * @param iJobId  业务的ID号。
	 * @throws Exception 
     */
	public static ArrayList<VM> getVmInfoListInAJob(int iJobId) throws Exception{
		ArrayList<VM> result = new ArrayList<VM>();
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT * FROM vm_info WHERE vm_job_id = " + iJobId ;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果			
			while(rs.next()){
				VM tmpVm = new VM();
				tmpVm.setstrVmId(rs.getString("vm_id"));
				tmpVm.setStrVmName(rs.getString("vm_name"));
				tmpVm.setiServerId(rs.getInt("vm_sever_id"));
				tmpVm.setiVmJobId(rs.getInt("vm_job_id"));
				tmpVm.setiVMCpuNum(rs.getInt("vm_cpu_num"));
				tmpVm.setiVmMemorySize(rs.getInt("vm_memory"));
				tmpVm.setiVmStorageSize(rs.getInt("vm_storage"));
				tmpVm.setiImageId(rs.getInt("image_id"));
				tmpVm.setStrVmIp(rs.getString("vm_ip"));
				tmpVm.setiNetStatus(rs.getInt("net_status"));
				result.add(tmpVm);
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
	 * 查看用户iUserId下是否已经存在业务名为strJobName的业务；
	 * 因为同一用户下是不允许有多个相同业务名的，但不同用户可以有相同的业务名。
	 * @param strJobName
	 * @return 
	 */
	public boolean isJobExist(String strJobName, int iUserId) throws Exception
	{
		//默认该业务名不存在
		boolean isJobExist = false;
		Connection connection = null; //定义连接对象
		try{
			String sql = "SELECT * FROM job_info WHERE job_name = '" + strJobName 
			+ "' and job_user_id = " + iUserId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果			
			if(rs.next())
				isJobExist = true;
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
		return isJobExist;
	}
	
	public static int getZoneId(int jobId) throws Exception{
		Connection connection = null; //定义连接对象
		int zoneId = -1;
		try{
			String sql = "SELECT zone_id FROM job_info WHERE job_id = " + jobId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
			if(rs.next()){
				zoneId = rs.getInt("zone_id");
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
		return zoneId;
	}
	
//	public static String getZoneHost(int zoneId) throws Exception
//	{
//		Connection connection = null; //定义连接对象
//		String zoneHost = null;
//		try{
//			String sql = "SELECT zone_host FROM zone_info WHERE zone_id = " + zoneId;//查询语句
//			connection = MySQLConnection.getInstance().getConnection();
//			//创建数据库对象
//			Statement statement = connection.createStatement();
//			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
//			if(rs.next()){
//				zoneHost = rs.getString("zone_host");
//			}
//		}catch(SQLException e){
//			throw new Exception(e);
//		}finally{
//			//finally块中的代码总是执行的。
//			try{
//				//关闭数据库，如果已打开连接。执行完毕后要关闭，释放资源
//				connection.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		return zoneHost;
//	}
	
//	public static String getZoneSecret(int zoneId) throws Exception
//	{
//		Connection connection = null; //定义连接对象
//		String zoneSecret = null;
//		try{
//			String sql = "SELECT zone_secret FROM zone_info WHERE zone_id = " + zoneId;//查询语句
//			connection = MySQLConnection.getInstance().getConnection();
//			//创建数据库对象
//			Statement statement = connection.createStatement();
//			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
//			if(rs.next()){
//				zoneSecret = rs.getString("zone_secret");
//			}
//		}catch(SQLException e){
//			throw new Exception(e);
//		}finally{
//			//finally块中的代码总是执行的。
//			try{
//				//关闭数据库，如果已打开连接。执行完毕后要关闭，释放资源
//				connection.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//		return zoneSecret;
//	}
	
	public static String getNfsServer(int zoneId) throws Exception
	{
		Connection connection = null; //定义连接对象
		String nfsServer = null;
		try{
			String sql = "SELECT nfs_server FROM zone_info WHERE zone_id = " + zoneId;//查询语句
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			ResultSet rs  = statement.executeQuery(sql); //获取查询结果	
			if(rs.next()){
				nfsServer = rs.getString("nfs_server");
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
		return nfsServer;
	}
	
	
	//这是一个测试函数。
	public static int addTest(int a, int b)
	{
		return a+b;
	}
	public static void main(String args[]){
	}
}	
