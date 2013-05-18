package com.cloud;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**用于读取配置文件信息的单例类.
 * 
 * @author Yan Haiming
 *
 */
public class ReadPropertiesUtils 
{
	private static ReadPropertiesUtils readPro = null; //初始化一个单例对象
	private static ResourceBundle resourceBundle = null; 
	
	//私有的构造函数
	private ReadPropertiesUtils(String fileName)
	{
		resourceBundle = ResourceBundle.getBundle(fileName);
	}
	
	public synchronized static ReadPropertiesUtils getInstance(String fileName)
	{
		if(null == readPro)
		{
			readPro = new ReadPropertiesUtils(fileName);
		}
		return readPro;
	}
	
	//根据key获得相应的属性值
	public String getValue(String key)
	{
		return resourceBundle.getString(key);
	}
	
	public static void main(String[] args)
	{
		String configFile = "/com/cloud/config/dataBase.properties";
		ReadPropertiesUtils util =  ReadPropertiesUtils.getInstance(configFile);
		Enumeration enumm = resourceBundle.getKeys();
		  while (enumm.hasMoreElements()) {
		   String str = (String) enumm.nextElement();
		   System.out.println(str + "=" +readPro.getValue(str));
		  }
	}
	
}
