package com.cloud.jobManagement;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.cloud.utilFun.LogWriter;

//import javax.swing.filechooser.FileSystemView;

public class DownloadFileList 
{
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public DownloadFileList()
	{
		
	}
	public ArrayList<SingleFileInfo> getFileList1()//将公有文件夹下的所有文件名称做成列表打包传给flex,过滤掉文件夹
	{
		ArrayList<SingleFileInfo> fileList1 = new ArrayList<SingleFileInfo>();
		
		File fileDir = new File("D:/apache-tomcat-6.0.30/webapps/mupload/upload");
		File[] fileCollection = fileDir.listFiles();
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
			singleFile.fileSize = df.format((double)fileCollection[i].length() / 1048576) + "MB";
			//logWriter.log(singleFile.fileSize);
			fileList1.add(singleFile);
		}
		
		return fileList1;
	}
	
	public ArrayList<SingleFileInfo> getFileList()//将私有文件夹下的所有文件名称做成列表打包传给flex,过滤掉文件夹
	{
		ArrayList<SingleFileInfo> fileList = new ArrayList<SingleFileInfo>();
		
		File fileDir = new File("D:/apache-tomcat-6.0.30/webapps/mupload/undefined");
		File[] fileCollection = fileDir.listFiles();
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
			singleFile.fileSize = df.format((double)fileCollection[i].length() / 1048576) + "MB";
			//logWriter.log(singleFile.fileSize);
			fileList.add(singleFile);
		}
		
		return fileList;
	}
	
	/*public static void main(String[] args)
	{
		DownloadFileList test = new DownloadFileList();
		test.getFileList();
	}*/
	
	

}
