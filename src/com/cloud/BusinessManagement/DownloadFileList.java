package com.cloud.BusinessManagement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.cloud.config.DataCenterInfo;
import com.cloud.dao.MySQLConnection;

//import javax.swing.filechooser.FileSystemView;

public class DownloadFileList 
{
	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	
	//upload是文件上传的根目录。
	private String fileDirBase = dcInfo.getTomcatRootDir() + "mupload/upload/";
	//private String fileDirBase = "F:/software/apache-tomcat-6.0.29/webapps/mupload/upload/";
	
	public ArrayList<UploadFileInfo_Java> getPublicFileList(String fileType)//取得公共盘或者协同共享盘的文件信息，打包成列表传给flex
	{
		ArrayList<UploadFileInfo_Java> fileList = new ArrayList<UploadFileInfo_Java>();
		UploadFileInfo_Java fileInfo;
		
		Connection connection = null;
		String sql = "select * from public_file_info where public_file_type = '" + fileType + "'";
		
		try{
			connection = MySQLConnection.getInstance().getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next())
			{
				fileInfo = new UploadFileInfo_Java();
				fileInfo.file_Category = rs.getString("public_file_category");
				fileInfo.file_Description = rs.getString("public_file_description");
				fileInfo.file_Name = rs.getString("public_file_name");
				fileInfo.file_Owner = rs.getString("public_file_owner");
				fileInfo.file_Size = rs.getString("public_file_size");
				fileInfo.file_Time = rs.getString("public_file_time");
				fileList.add(fileInfo);
			}
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
        return fileList;
		/*ArrayList<SingleFileInfo> publiFileList = new ArrayList<SingleFileInfo>();
		
		String strFileDir = fileDirBase + dcInfo.getPubliDirName();
		
		File fileDir = new File(strFileDir);
		File[] fileCollection = fileDir.listFiles();
		
		if(null == fileCollection)
		{
			System.out.println("获取公有文件夹列表失败！");
			return null;
		}
		
		DecimalFormat df = new DecimalFormat("#0.00");
		for(int i = 0; i < fileCollection.length; i ++)
		{
			if(fileCollection[i].isDirectory())
			{
				continue;
			}
			//FileSystemView fsv = FileSystemView.getFileSystemView();
			SingleFileInfo singleFile = new SingleFileInfo();
			//singleFile.fileIcon = fsv.getSystemIcon(fileCollection[i]);
			singleFile.fileName = fileCollection[i].getName();
			
			System.out.println("public dir: " + singleFile.fileName);
			
			if(fileCollection[i].length() >= 0 && fileCollection[i].length() <= 1024)
			{
				singleFile.fileSize = Long.toString(fileCollection[i].length()) + "B";
			}
			else if(fileCollection[i].length() <= 1048576)
			{
				singleFile.fileSize = df.format((double)fileCollection[i].length() / 1024) + "KB";
			}
			else
			{
				singleFile.fileSize= df.format((double)fileCollection[i].length() / 1048576) + "MB";
			}
			
			//System.out.println(singleFile.fileSize);
			publiFileList.add(singleFile);
		}
		System.out.println("获取公有文件夹列表成功！");
		return publiFileList;*/
	}
	
	/**
	 * 将私有文件夹下的所有文件名称做成列表打包传给flex,过滤掉文件夹.
	 * @param strDir 用户的私有目录，通常是一个业务的iJobId.
	 * @return
	 */
	public ArrayList<SingleFileInfo> getPrivateFileList(String strDir)
	{
		if(strDir == null)
		{
			System.out.println("未选择JobID");
			return null;
		}
		else
		{
			ArrayList<SingleFileInfo> privateDileList = new ArrayList<SingleFileInfo>();
			
			String strFileDir = fileDirBase + strDir;
			System.out.println("当前文件夹：" + strFileDir);
			
			File fileDir = new File(strFileDir);
			File[] fileCollection = fileDir.listFiles();
			
			if(null == fileCollection)
			{
				System.out.println("获取私有文件夹列表失败！");
				return null;
			}
			
			DecimalFormat df = new DecimalFormat("#0.00");
			for(int i = 0; i < fileCollection.length; i ++)
			{
				if(fileCollection[i].isDirectory())
				{
					continue;
				}
				//FileSystemView fsv = FileSystemView.getFileSystemView();
				SingleFileInfo singleFile = new SingleFileInfo();
				//singleFile.fileIcon = fsv.getSystemIcon(fileCollection[i]);
				singleFile.fileName = fileCollection[i].getName();
				
				System.out.println("private dir: " + singleFile.fileName);
				
				if(fileCollection[i].length() >= 0 && fileCollection[i].length() <= 1024)
				{
					singleFile.fileSize = Long.toString(fileCollection[i].length()) + "B";
				}
				else if(fileCollection[i].length() <= 1048576)
				{
					singleFile.fileSize = df.format((double)fileCollection[i].length() / 1024) + "KB";
				}
				else
				{
					singleFile.fileSize= df.format((double)fileCollection[i].length() / 1048576) + "MB";
				}
				//System.out.println(singleFile.fileSize);
				privateDileList.add(singleFile);
				//System.out.println(fileList.get(i).fileName + " ****       " + fileList.get(i).fileSize);
			}
			System.out.println("获取私有文件夹列表成功！");
			return privateDileList;
		}
	}
	
	//压缩已完成!bingo
	private static String downloadFileDir = dcInfo.getTomcatRootDir() + "mupload/upload/";//所有待打包文件共同存放地方的上一级，包括公有和私有
	private static String zipStoreDir = dcInfo.getTomcatRootDir() + "mupload/upload/zipStore/";//所有打包文件都放在这里
	
	
	/*通过flag为公有还是私有进行区别，通过jobId取唯一压缩名，对downLoad文件集打包
	 * flag = Public:公共盘
	 * flag = PublicNotToAll:协同共享盘
	 * flag = Private:业务盘*/
	public String packageFiles(ArrayList<String> downLoad, String flag, String jobId)
	{
		String downloadAddress = null;//最后形成的下载链接
		
		File testDir = new File(zipStoreDir);
		if(!testDir.exists())//如果zipStore文件夹不存在
		{
			testDir.mkdirs();
		}
		
		File zipFile = new File(zipStoreDir + "Download_" + flag +"_" + jobId + ".zip");//e.g: Download_Public_302.zip
		ZipOutputStream zipStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        
        byte[] bufferArea = new byte[1024 * 10];//读写缓冲区
        
        try {
			zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
			zipSource = null;//将源头文件格式化为输入流
			
			if(flag.equals("Public"))//要从公有盘下载文件
			{
				for(int i = 0; i < downLoad.size(); i ++)
				{
					System.out.println(downLoad.get(i) + "********||");
				}
				for(int i = 0; i < downLoad.size(); i ++)
				{
					File file = new File(downloadFileDir + "publicspace/" + downLoad.get(i));//取文件名创造File变量
					zipSource = new FileInputStream(file);
					
					//压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
		            ZipEntry zipEntry = new ZipEntry(file.getName());
		            zipStream.putNextEntry(zipEntry);//定位到该压缩条目位置，开始写入文件到压缩包中
		            
		            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);//输入缓冲流
		            int read = 0;		            
		            
		            //在任何情况下，b[0] 到 b[off] 的元素以及 b[off+len] 到 b[b.length-1] 的元素都不会受到影响。这个是官方API给出的read方法说明，经典！
		            while((read = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1)
		            {
		                zipStream.write(bufferArea, 0, read);
		            }
				}
			}
			else if(flag.equals("PublicNotToAll"))
			{
				for(int i = 0; i < downLoad.size(); i ++)
				{
					File file = new File(downloadFileDir + "notAllPublicSpace/" + downLoad.get(i));
					zipSource = new FileInputStream(file);
					
					ZipEntry zipEntry = new ZipEntry(file.getName());
					zipStream.putNextEntry(zipEntry);
					
					bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
					int read = 0;
					
					while((read = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1)
					{
						zipStream.write(bufferArea, 0, read);
					}
				}
			}
			else//要从私有盘下载文件
			{
				for(int i = 0; i < downLoad.size(); i ++)
				{
					File file = new File(downloadFileDir + jobId + "/" + downLoad.get(i));//取文件名创造File变量
					zipSource = new FileInputStream(file);
					
					//压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样
		            ZipEntry zipEntry = new ZipEntry(file.getName());
		            zipStream.putNextEntry(zipEntry);//定位到该压缩条目位置，开始写入文件到压缩包中
		            
		            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);//输入缓冲流
		            int read = 0;		            
		            
		            //在任何情况下，b[0] 到 b[off] 的元素以及 b[off+len] 到 b[b.length-1] 的元素都不会受到影响。这个是官方API给出的read方法说明，经典！
		            while((read = bufferStream.read(bufferArea, 0, 1024 * 10)) != -1)
		            {
		                zipStream.write(bufferArea, 0, read);
		            }
				}
			}
			downloadAddress = zipFile.getName();
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            //关闭流
            try {
                if(null != bufferStream) bufferStream.close();
                if(null != zipStream) zipStream.close();
                if(null != zipSource) zipSource.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}	
		
		return downloadAddress;
	}
	public void deletePrivateFile(String fileName, String jobId)//通过jobId读取到该私有文件的存储路径，根据fileName删除指定的私有文件
	{
		File file = new File(downloadFileDir + jobId + "/" + fileName);//取文件名创造File变量
		System.out.println(file.getAbsolutePath());
		file.delete();
	}
	
	public static void main(String[] args)
	{
		DownloadFileList test = new DownloadFileList();
		test.fileDirBase = "D:/apache-tomcat-6.0.30/webapps/mupload/upload/";
		test.getPublicFileList("public_to_all");
		test.getPrivateFileList("0");
	}
	
	

}
