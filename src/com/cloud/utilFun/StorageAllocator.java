package com.cloud.utilFun;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class StorageAllocator {
	private String host;
	private String user = "root";
	private String passwd = "sys#000";
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public StorageAllocator( String host ) {
		this.host = host;
	}
	public boolean addNfsEntry( int job, String ip ) {
		
		String content = "/image-pool/userspace/" + job + " " + ip + "(rw,sync,no_root_squash,no_subtree_check)";
		try 
		{ 
			if(ip.equals("192.168.100.250")) //如果是西溪的服务，则密码应该是863cloud
				passwd = "863cloud";
			/* Create a connection instance */ 
			Connection conn = new Connection(host); 
			/* Now connect */ 
			conn.connect(); 
			/* Authenticate */ 
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd); 
			if (isAuthenticated == false) {
				//throw new IOException("获取数据失败！");
				logWriter.log("Authentication failure, addNfsEntry to " + ip + " fail!"); 
				return false;
			}
			/* Create a session */ 
			Session sess = conn.openSession(); 
			String cmd = "echo \"" + content + " \" >> /etc/exports";
			logWriter.log(cmd);
			sess.execCommand( cmd );
			
			/* Show exit status, if available (otherwise "null") */ 
			logWriter.log("ExitCode: " + sess.getExitStatus()); 
			/* Close this session */ 
			sess.close(); 
			/* Close the connection */ 
			conn.close(); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(System.err); 
			return false;
		}
		return true;
	}
	
	public boolean refreshNfsEntry(){
		Connection conn = new Connection(host); 
		/* Now connect */ 
		
		try{
			conn.connect(); 
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd); 
			if(isAuthenticated == false){
				logWriter.log("Authentication failure"); 
				return false;
			}	
			Session sess = conn.openSession(); 
			String cmd = "exportfs -r";
			logWriter.log(cmd);
			sess.execCommand(cmd);
		}catch(IOException e) 
		{ 
			e.printStackTrace(System.err); 
			return false;
		}
		return true;
		
	}
	
	public boolean allocateDisk(int jobId){
		Connection conn = new Connection(host); 
		/* Now connect */ 
		
		try{
			conn.connect(); 
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd); 
			if(isAuthenticated == false){
				logWriter.log("Authentication failure"); 
				return false;
			}	
			Session sess = conn.openSession();
			String diskPath = "/image-pool/userspace/" + jobId;
			String softLink = "/usr/local/tomcat/webapps/mupload/upload/" + jobId;
			String cmd = "mkdir " + diskPath + ";ln -s " + diskPath + " " + softLink + ";";
			logWriter.log(cmd);
			sess.execCommand(cmd);
		}catch(IOException e) 
		{ 
			e.printStackTrace(System.err); 
			return false;
		}
		return true;
	}
	
	public boolean deleteDisk(int jobId){
		Connection conn = new Connection(host); 
		/* Now connect */ 
		
		try{
			conn.connect(); 
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd); 
			if(isAuthenticated == false){
				logWriter.log("Authentication failure"); 
				return false;
			}	
			Session sess = conn.openSession();
			String cmd = "rm -rf /image-pool/userspace/" + jobId + ";";
			logWriter.log(cmd);
			sess.execCommand(cmd);
		}catch(IOException e) 
		{ 
			e.printStackTrace(System.err); 
			return false;
		}
		return true;
	}
	
	public static void main(String args[]) {
		StorageAllocator pool = new StorageAllocator("192.168.100.250");
		
		if ( false == pool.addNfsEntry(1, "192.168.1.1") ) {
			logWriter.log("addip error");
		}
		if ( false == pool.addNfsEntry(2, "192.168.2.2") ) {
			logWriter.log("addip error");
		}
		pool.deleteDisk(2);
	}
}
