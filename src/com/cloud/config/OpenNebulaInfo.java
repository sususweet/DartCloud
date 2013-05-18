package com.cloud.config;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.host.HostPool;
import org.xml.sax.InputSource;

/**
 * 从openNebula数据库中读取到的一些重要信息，单例。
 * @author relic
 *
 */
public class OpenNebulaInfo 
{
	static private OpenNebulaInfo _instance = null;
	private HashMap serverMap = new HashMap();
	private Client oneClient;

	private OpenNebulaInfo(){
		//获取配置信息
		DataCenterInfo dataCenterInfo = DataCenterInfo.getInstance();
		
		//初始化oneClient，从而能通过oneClient与OpenNebula进行交互。
		try {
			oneClient = null;
			oneClient = new Client(dataCenterInfo.getSecret(), dataCenterInfo.getEndpoint());
			
		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		}
		HostPool hp = new HostPool(oneClient);
		initServerMap(hp.info().getMessage());
	}
	
	/**
	 * 调用HostPool的info方法之后，将获得Host池中所有的Host的信息，且信息是以xml的形式返回的。
	 * @param xmlStr HostPool中所有的host信息。
	 * @return
	 */
	public boolean initServerMap(String xmlStr)
	{
		//清空serverMap
		serverMap.clear();
		//创建一个新的字符串
		StringReader read = new StringReader(xmlStr);
		//创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        try {
            //通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
//            System.out.println(root.getName());//输出根元素的名称（测试）
            //得到根元素所有子元素的集合
            List firstLevel = root.getChildren();
            //获得XML中的命名空间（XML中未定义可不写）
            Namespace ns = root.getNamespace();
            Element et = null;
            for(int i=0;i<firstLevel.size();i++){
                et = (Element) firstLevel.get(i);//循环依次得到子元素
                /**//*
                 * 无命名空间定义时
                 * et.getChild("users_id").getText();
                 * et.getChild("users_address",ns).getText()
                 */
//                System.out.println(et.getChild("NAME",ns).getText());
//                System.out.println(et.getChild("ID",ns).getText());
                int iServerId = Integer.parseInt(et.getChild("ID",ns).getText());
                String strServerName = et.getChild("NAME",ns).getText();
                serverMap.put(strServerName, iServerId);
                
            }

//            //若要取第二层节点的元素
//            et = (Element) firstLevel.get(0);
//            List zjiedian = et.getChildren();
//            for(int j=0;j<zjiedian.size();j++){
//                Element xet = (Element) zjiedian.get(j);
//                System.out.println(xet.getName());
//            }
        } catch (JDOMException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}
	
	static public synchronized OpenNebulaInfo getInstance()
	{
		if(_instance == null)
			_instance = new OpenNebulaInfo();
		return _instance;
	}
	
	public HashMap getServerMap() {
		return serverMap;
	}

	public void setServerMap(HashMap serverMap) {
		this.serverMap = serverMap;
	}
	
	
	public static void main(String args[])
	{
		OpenNebulaInfo onInfo = OpenNebulaInfo.getInstance();
		System.out.println(onInfo.getServerMap().get("debian2"));
		System.out.println(onInfo.getServerMap().get("Debian1"));
	}

	
}




























