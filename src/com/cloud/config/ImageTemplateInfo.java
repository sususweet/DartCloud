package com.cloud.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/**
 * 不同类型的虚拟机配置信息.
 * 
 * @author Yan Haiming
 *
 */

public class ImageTemplateInfo 
{
	private String str_template = "";
	
	public ImageTemplateInfo(String strTempfileName)
	{
		try{
			//获取Tomcat下本项目的根目录。
			String projectRootDir = this.getClass().getResource("/").toString().substring(5);
			//获取绝对路径。
			String filePath = projectRootDir + "conf/"+strTempfileName;
			File file = new File(filePath);
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str_temp = "";
			try {
				while((str_temp = in.readLine() ) != null)
				{
					if(str_temp != null)
						str_template += str_temp + "\n";
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e){
			System.out.println("error: " + e);
		}
		
	}
	
	
	public String getStr_template() {
		return str_template;
	}


	public void setStr_template(String strTemplate) {
		str_template = strTemplate;
	}
	
	
	public static void main(String[] args)
	{
	}

}
