package com.cloud.utilFun;

import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class NetworkAllocator {
	private String host;
	private String user = "root";
	private String passwd = "sys#000";
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public NetworkAllocator( String host ) {
		this.host = host;
	}
	
	public boolean addNetworkEntry( String ip ) {
		try 
		{ 
			/* Create a connection instance */ 
			Connection conn = new Connection(host); 
			/* Now connect */ 
			conn.connect(); 
			/* Authenticate */ 
			boolean isAuthenticated = conn.authenticateWithPassword(user, passwd); 
			if (isAuthenticated == false) {
				//throw new IOException("获取数据失败！");
				logWriter.log("Authentication failure"); 
				return false;
			}
			/* Create a session */ 
			Session sess = conn.openSession(); 
			String cmd = "/image-pool/service/natshell/natadd.sh " + ip;
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
		
	public boolean deleteDisk(String ip){
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
			String cmd = "/image-pool/service/natshell/natdel.sh " + ip;
			logWriter.log(cmd);
			sess.execCommand(cmd);
		}catch(IOException e) 
		{ 
			e.printStackTrace(System.err); 
			return false;
		}
		return true;
	}
	public static void main(String args[])
	{
		//NetworkAllocator na = new NetworkAllocator("192.168.100.250");
		//na.addNetworkEntry("192.168.0.64");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
