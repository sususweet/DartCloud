package com.cloud.resourceStatistic;

import com.cloud.dao.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;

/*资源统计栏---->资源地域分布模块---->CPU后台数据获取服务*/

public class HX_resGeoDistribution_CPU 
{
	/*public static void main(String args[]) throws Exception
	{
		HX_resGeoDistribution_CPU test = new HX_resGeoDistribution_CPU();
		test.getAllInformation();
	}*/
	
	public int cpuTotalNumber = 0;//记录sever_info表中总CPU数量
	
	//public Object[] preHandler() throws Exception
	//{
	//	return getAllInformation().toArray();
	//}
	
	
	public ArrayList<CPU> getAllInformation() throws Exception
	{
		ArrayList<CPU> cpuList = new ArrayList<CPU>();
		CPU tempCPU;
		
		Connection connection = null;
		String firstsql = "select sum(cpu_num) from sever_info";
		String sql = "select sever_name,sever_location,cpu_num from sever_info";
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(firstsql);
			if(rs.next())
			{
				cpuTotalNumber = rs.getInt(1);
				//System.out.println("--->" + cpuTotalNumber);
			}
			
			rs = null;
			rs = statement.executeQuery(sql);
			while(rs.next())
			{
				tempCPU = new CPU();
				tempCPU.setCpuSeverName(rs.getString("sever_name"));
				tempCPU.setCpuSeverLocation(rs.getString("sever_location"));
				tempCPU.setCpuNumber(rs.getInt("cpu_num"));
				tempCPU.setCpuPercentage(tempCPU.getCpuNumber(), cpuTotalNumber);
				//System.out.println(tempCPU.get_cpuNumber() + " " + tempCPU.get_cpuPercentage());
				cpuList.add(tempCPU);
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
        return cpuList;
	}
}