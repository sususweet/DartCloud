package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author weibin
 *
 */
public class SocketServerTest {
	
	public static void main(String[] args) {
		ServerSocket socket;
		try {
			socket = new ServerSocket(8888);
			while(true){
			Socket s = socket.accept();
			
			InputStream is = s.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
//			PrintWriter
			
			System.out.println("8888"+br.readLine());
			is.close();
			s.close();
			}
//			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
