package com.cloud.resourceStatistic;

import com.cloud.dao.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;

/*资源统计栏---->资源地域分布模块---->内存后台数据获取服务*/

public class ResGeoDistribution_Memory
{
	/*public static void main(String args[]) throws Exception
	{
		ResGeoDistribution_Memory test = new ResGeoDistribution_Memory();
		test.getAllInformation();
	}*/
	
	public int memoryTotalNumber = 0;//记录sever_info表中总内存数量
	
	//public Object[] preHandler() throws Exception
	//{
	//	return getAllInformation().toArray();
	//}
	
	
	public ArrayList<Memory> getAllInformation() throws Exception
	{
		ArrayList<Memory> memoryList = new ArrayList<Memory>();
		Memory tempMemory;
		
		Connection connection = null;
		String firstsql = "select sum(memory_size) from sever_info";
		String sql = "select sever_name,sever_location,memory_size from sever_info";
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(firstsql);
			if(rs.next())
			{
				memoryTotalNumber = rs.getInt(1);
				//System.out.println("--->" + cpuTotalNumber);
			}
			
			rs = null;
			rs = statement.executeQuery(sql);
			while(rs.next())
			{
				tempMemory = new Memory();
				tempMemory.setMemorySeverName(rs.getString("sever_name"));
				tempMemory.setMemorySeverLocation(rs.getString("sever_location"));
				tempMemory.setMemoryNumber(rs.getInt("memory_size"));
				tempMemory.setMemoryPercentage(tempMemory.getMemoryNumber(), memoryTotalNumber);
				//System.out.println(tempCPU.get_cpuNumber() + " " + tempCPU.get_cpuPercentage());
				memoryList.add(tempMemory);
			}
		}catch(SQLException e)
		{
			throw new Exception(e);
		}finally
		{
			try{
				connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
        return memoryList;
	}
}