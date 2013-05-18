package com.cloud.resourceStatistic;

import com.cloud.dao.MySQLConnection;
import java.sql.*;
import java.util.ArrayList;

/*资源统计栏---->资源地域分布模块---->存储后台数据获取服务*/

public class ResGeoDistribution_Storage
{
	/*public static void main(String args[]) throws Exception
	{
		ResGeoDistribution_Memory test = new ResGeoDistribution_Memory();
		test.getAllInformation();
	}*/
	
	public int storageTotalNumber = 0;//记录sever_info表中总内存数量
	
	//public Object[] preHandler() throws Exception
	//{
	//	return getAllInformation().toArray();
	//}
	
	
	public ArrayList<Storage> getAllInformation() throws Exception
	{
		ArrayList<Storage> storageList = new ArrayList<Storage>();
		Storage tempStorage;
		
		Connection connection = null;
		String firstsql = "select sum(storage_size) from sever_info";
		String sql = "select sever_name,sever_location,storage_size from sever_info";
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(firstsql);
			if(rs.next())
			{
				storageTotalNumber = rs.getInt(1);
				//System.out.println("--->" + cpuTotalNumber);
			}
			
			rs = null;
			rs = statement.executeQuery(sql);
			while(rs.next())
			{
				tempStorage = new Storage();
				tempStorage.setStorageSeverName(rs.getString("sever_name"));
				tempStorage.setStorageSeverLocation(rs.getString("sever_location"));
				tempStorage.setStorageNumber(rs.getInt("storage_size"));
				tempStorage.setStoragePercentage(tempStorage.getStorageNumber(), storageTotalNumber);
				//System.out.println(tempCPU.get_cpuNumber() + " " + tempCPU.get_cpuPercentage());
				storageList.add(tempStorage);
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
        return storageList;
	}
}