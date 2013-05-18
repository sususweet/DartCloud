package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.template.*;

import com.cloud.config.DataCenterInfo;
import com.cloud.jobManagement.*;

public class Test {

	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	
	public Test() throws FileNotFoundException
	{
		//获取Tomcat下本项目的根目录。
		String projectRootDir = this.getClass().getResource("/").toString().substring(5);
		//获取绝对路径。
		String filePath = projectRootDir + "conf/Linux_template.txt";
		
		File file = new File(filePath);
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		String str = "";
		String tmpStr = "";
		try {
			while((tmpStr = in.readLine()) != null)
			{
				str += tmpStr + "\n";
//				System.out.println(str);
			}
			in.close();
			System.out.println(str);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])
	{
//		dcInfo.getSecret();
//		Client oneClient = null;
//		try {
//			oneClient = new Client(dcInfo.getSecret(), dcInfo.getEndpoint());
//			
////			System.out.println(Template.info(oneClient, 0).isError() );
////			System.out.println(Template.info(oneClient, 0).getMessage() );
//			System.out.println(TemplatePool.infoAll(oneClient).getMessage() );
//			
//			
//		} catch (ClientConfigurationException e) {
//			e.printStackTrace();
//		}
//		System.out.println("this is a test.");
		
		try {
			Test t = new Test();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
};


