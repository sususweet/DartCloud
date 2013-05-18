package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Flash为了其自身的安全性，向服务器申请建立Socket连接时，
 * 都要向服务器的843端口查询是否有相应的policy.xml来允许其建立连接；
 * 即沙箱机制。
 * 
 * @author WeiBin 2012-5-23 13:00
 *
 */

public class SecurityPolicyServer {
	
	public static void main(String[] args) {
		ServerSocket socket;
		try {
			socket = new ServerSocket(843);
			Socket s = null;
			while(true){
			s = socket.accept();
			InputStream is = s.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			
			//存取<policy-file-request/>，防止没有换行符
			char[] req = new char[22];
			br.read(req,0,22);
			String str = new String(req);
			System.out.println(str);
			if(str.indexOf("request")>-1){
				String policyString = "<?xml version=\"1.0\"?>"+
										"<cross-domain-policy>"+ 
										"<site-control permitted-cross-domain-policies=\"all\"/>"+
										"<allow-access-from domain=\"*\" to-ports=\"*\" />"+
										"</cross-domain-policy>";
				System.out.println(System.currentTimeMillis()+":"+policyString);
				pw.println(policyString);
				pw.flush();
				
			}
//			PrintWriter
			s.getOutputStream().close();
			is.close();
			s.close();
			}
//			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
