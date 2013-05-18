package com.cloud.BusinessManagement;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.*;

import com.cloud.config.DataCenterInfo;

public class DownloadZipFile {

    /**
     * @param args
     */
    
    //压缩已完成!bingo
	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	private static String downloadFileDir = dcInfo.getTomcatRootDir() + "mupload/upload/";//所有待打包文件共同存放地方的上一级，包括公有和私有
	private static String zipStoreDir = dcInfo.getTomcatRootDir() + "mupload/upload/zipStore/";//所有打包文件都放在这里
	
	public String packageFiles(ArrayList<String> downLoad, String flag, String jobId)//通过flag为公有还是私有进行区别，通过jobId取唯一压缩名，对downLoad文件集打包
	{
		String downloadAddress = null;//最后形成的下载链接
		
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
}
