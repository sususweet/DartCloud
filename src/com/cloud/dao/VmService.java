package com.cloud.dao;


import com.cloud.entities.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * 虚拟机的数据库操作
 * @author  Bruce Yan
 * @version 1.0, 2012-3-14
 */
public class VmService {
	
	/*
	 * 将一个VM对象插入到数据库中
	 * @param oneVmInfo 待插入的虚拟机对象
	 * @return 如果插入成功，则返回TRUE，否则返回FALSE
	 */
	public static boolean addAnVmInfo(VM oneVmInfo){
		//在数据库中增加一条虚拟机信息
		String strVmId = oneVmInfo.getstrVmId();
		String strVmName = oneVmInfo.getStrVmName();
		int iVmJobId = oneVmInfo.getiVmJobId();
		int iServerId = oneVmInfo.getiServerId();
		int iVmCpuNum = oneVmInfo.getiVMCpuNum();
		int iVmMemorySize = oneVmInfo.getiVmMemorySize();
		int iVmStorageSize = oneVmInfo.getiVmStorageSize();
		int iImageId = oneVmInfo.getiImageId();
		String strVmPass = oneVmInfo.getStrVmPass();
		String strVmIp = oneVmInfo.getStrVmIp();
		int iNetStatus = oneVmInfo.getiNetStatus();
		boolean isSucc = false; 
		
		//定义SQL语句，now()函数是MySQL的内置函数，获得当前具体时间
		//除了job_id为自动增长字段外，其他字段均要手动写入；
		String sql = "INSERT INTO vm_info(vm_id,vm_name,vm_job_id,vm_sever_id,vm_cpu_num,vm_memory,vm_storage,image_id,vm_pass,vm_ip, net_status)" +
				"VALUES ('" + strVmId + "','" + strVmName + "','" + iVmJobId + "','" + iServerId
				+ "','" + iVmCpuNum + "','" + iVmMemorySize + "','" + iVmStorageSize +  "','" + iImageId + 
				"','" + strVmPass + "','" + strVmIp +  "','" + iNetStatus +"');";
		
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

	public static int getImageId(String strVmId){
		int imageId = -1;
		String sql = "select image_id from vm_info where vm_id = '" + strVmId + "';";
		Connection connection = null; //定义连接对象
		ResultSet rs = null; //定义变量存放执行结果
		
		//********************调试-打印获取镜像的sql语句**********************//
		System.out.println(sql);
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if(rs == null){
				System.out.println("没有镜像了！");
			}
			else{
				rs.next();
				imageId = rs.getInt("image_id");
			}
		}catch(SQLException e){
			System.out.println("虚拟机"+ strVmId +"的镜像数据获取失败！");
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return imageId;
	}
	
	/*
	 * 以一虚拟机ID号获取一个虚拟机
	 * @param vid 待获取的虚拟机ID号
	 * @return 如果查询到
	 */
	public static VM getVmById(String strVmId){
		VM vm = null;
		String sql = "select * from vm_info where vm_id = '" + strVmId +"'";
		
		Connection connection = null; //定义连接对象
		ResultSet rs = null; //定义变量存放执行结果
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if(rs == null){
				System.out.println("没有获得"+strVmId+"号虚拟机。");
			}
			else{
				rs.next();
				vm = new VM(rs.getString("vm_id"), rs.getString("vm_name"), rs.getInt("vm_job_id"), rs.getInt("vm_sever_id"),
						rs.getInt("vm_cpu_num"), rs.getInt("vm_memory"), rs.getInt("vm_storage"),rs.getString("vm_pass"),
						rs.getString("vm_ip"),rs.getInt("net_status"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return vm;
	}
	
	/**
	 * 通过虚拟机的id号获取该虚拟机所对应的iJobId.
	 * @param strVmId 虚拟机id号
	 * @return
	 */
	public static int getJobIdByVmId(String strVmId)
	{
		int iJobId = 0;
		String sql = "select * from vm_info where vm_id = '" + strVmId +"'";
		
		Connection connection = null; //定义连接对象
		ResultSet rs = null; //定义变量存放执行结果
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			//创建数据库对象
			Statement statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			if(rs.next()){
				iJobId = rs.getInt("vm_job_id");
			}
			else{
				System.out.println("没有获得id为"+strVmId+"的虚拟机iJobId。");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return iJobId;
	}
	
	/*
	 * 删除strVmId号虚拟机
	 * 
	 */
	public static boolean deleteVm(String strVmId){
		
		String sql = "DELETE FROM vm_info WHERE vm_id = '" + strVmId + "';";
		if(!excuteUpdateSql(sql))
		{
			System.out.println("删除虚拟机失败！");
			return false;
		}
		return true;
	}
	
	/*
	 * 一次性删除多个虚拟机
	 * @param vmIdArr 待删除虚拟机Id数组
	 * 
	 */
	public static boolean deleteVms(String vmIdArr[]){
		boolean ret = true;
		for(int i=0; i<vmIdArr.length; i++){
			if(!deleteVm(vmIdArr[i]))
				ret = false;
		}
		return ret;
	}
	
	/*
	 * 更新虚拟机信息
	 * @param vm 新的虚拟机的信息，其中只更新提供的数据。vm_job_id,vm_server_id,vm_cpu_num,vm_memory,vm_storage小于零时不更新（一般为-1）。vm_name为“”空字符串时，不进行更新。
	 * @return 如果成功，返回TRUE，失败返回FALSE
	 */
	public static boolean updateVm(VM vm){
		String vmId = vm.getstrVmId();
		String sql = "update vm_info set ";
		if(!vm.getStrVmName().equals("")){
			sql = sql + "vm_name = " + vm.getStrVmName();
		}
		if(vm.getiVmJobId()< 0){
			sql = sql + ", vm_job_id ='" + vm.getiVmJobId() + "'";
		}
		if(vm.getiServerId() < 0){
			sql = sql + ", vm_server_id ='" + vm.getiServerId() + "'";
		}
		if(vm.getiVMCpuNum()<=0){
			sql = sql + ", vm_cpu_num ='" + vm.getiVMCpuNum() + "'";
		}
		if(vm.getiVmMemorySize()<=0){
			sql = sql + ", vm_memory ='" + vm.getiVmMemorySize() + "'";
		}
		if(vm.getiVmStorageSize()<=0){
			sql = sql + ", vm_storage ='" + vm.getiVmStorageSize() + "'";
		}
		sql = sql + " where vm_id = '" + vmId +"'";
		
		if(!excuteUpdateSql(sql)){
			System.out.println("更新虚拟机配置失败！");
			return false;
		}
		
		return true;
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
	
	
	public static void main(String[] args){
		int i = VmService.getImageId("33");
		System.out.println(i);
		VM vm = getVmById("33");
		System.out.println(vm.getstrVmId());
	}
}
