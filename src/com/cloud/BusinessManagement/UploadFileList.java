/**
 * 这个文件是为了将上传的公共文件和协同共享文件信息保存在数据库中，私有文件的信息不保存
 *
 */
package com.cloud.BusinessManagement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.cloud.dao.MySQLConnection;

/**
 * @author hx  2013--05--09
 *
 */
public class UploadFileList 
{
	public void saveFileInfoToDB(ArrayList<UploadFileInfo_Java> uploadFileList)
	{
		for(int i = 0; i < uploadFileList.size(); i ++)
		{
			Connection connection = null;
			String sql = "insert into public_file_info (public_file_owner, public_file_name, public_file_size, public_file_time, " +
						 "public_file_type, public_file_category, public_file_description) values " +
						 "('" + uploadFileList.get(i).file_Owner + "', '" + 
						 uploadFileList.get(i).file_Name + "', '" +
						 uploadFileList.get(i).file_Size + "', now(), '" +
						 uploadFileList.get(i).file_Type + "', '" +
						 uploadFileList.get(i).file_Category + "', '" +
						 uploadFileList.get(i).file_Description + "')";
			try{
				connection = MySQLConnection.getInstance().getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
			}catch(SQLException e)
			{
				e.printStackTrace();
			}finally
			{
				try{
					connection.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//System.out.println(uploadFileList.get(i).file_Category + "---" + uploadFileList.get(i).file_Description + "---" + uploadFileList.get(i).file_Name + "---" + uploadFileList.get(i).file_Type);
		}
	}
}
