package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
   
/**
 * 
 * @author weibin
 *
 */
public class ClientSocket {
	
	public static void main(String[] args) {
		try {
			System.out.println("main function......");
			Socket s = new Socket("localhost",8888);
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader bri = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			writer.println(br.readLine());
			writer.flush();
			
//			System.out.println("get answer:"+bri.readLine());
			s.getOutputStream().close();
			s.getInputStream().close();
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
