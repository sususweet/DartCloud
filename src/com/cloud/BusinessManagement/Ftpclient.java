package com.cloud.BusinessManagement;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamException;

public class Ftpclient {
	/**
	 * Description: 向FTP服务器上传文件
	 * @Version1.0 Jul 27, 2008 4:31:09 PM by 崔红保（cuihongbao@d-heaven.com）创建
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);//连接FTP服务器
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			if ( !FTPReply.isPositiveCompletion(reply) ) {
				ftp.disconnect();
				System.out.print(ftp.getReplyString());
				return success;
			}
			
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			if ( false == ftp.changeWorkingDirectory(path) ) {
				System.out.println("change "+path+ "false");
			}
		
			if ( false == ftp.storeFile( new String(filename.getBytes("UTF-8"),"iso-8859-1" ), input) ) {
				System.out.println("occur error");
			}
			
			input.close();
			ftp.logout();
			success = true;
		} 
		catch (FTPConnectionClosedException e) {
			System.out.println("server has closed connection");
		}
		catch (CopyStreamException e) {
			System.out.println("occur IO error");
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}
	
	/**
	 * Description: 从FTP服务器下载文件
	 * @Version1.0 Jul 27, 2008 5:32:36 PM by 崔红保（cuihongbao@d-heaven.com）创建
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(url, port);

			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				System.out.print(ftp.getReplyString());
				ftp.disconnect();
				return success;
			}
			
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
			
			FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff:fs){
				if(ff.getName().equals(fileName)){
					File localFile = new File(localPath+"/"+ff.getName());
					
					OutputStream is = new FileOutputStream(localFile); 
					if(false ==ftp.retrieveFile(ff.getName(), is))
					{
						System.out.println("false!!!!");
					}
					
					is.close();
				}
			}
			
			ftp.logout();
			success = true;
		} 
		catch (FTPConnectionClosedException e) {
			System.out.println("server has closed connection");
		}
		catch (CopyStreamException e) {
			System.out.println("occur IO error");
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					
				}
			}
		}
		return success;
	}
	
	//应该还要添加创建删除目录功能
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//try {  
			//上传文本文件（包含中文成功）
			//上传二进制文件
	       //FileInputStream in=new FileInputStream(new File("E:/FTPClientTemplate.java"));  
			//InputStream in = new ByteArrayInputStream("test ftp sbsbsssbssb".getBytes("utf-8")); 
			/**
			 * 注意hp这个本地用户对/srv/ftp要有读写权限，且用户名不能使root
			 */
	        //boolean flag = uploadFile("10.214.0.170", 21, "hp", "hpghy", "/srv/ftp/", "test.java", in);   
			boolean flag = downFile("10.214.0.170", 21, "hp", "hpghy", "/srv/ftp", "test.txt", "e:");
	        if ( false == flag ) {
	        	System.out.println("error");
	        }
	        else {
	        	System.out.println("ok");
	        }
	   // } 
		
		//catch (FileNotFoundException e) {  
	   //     e.printStackTrace();  
	   // } 
		/*
	    catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	    } 
	    */
	}

}
