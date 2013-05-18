package com.cloud.resourceMonitor;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cloud.config.DataCenterInfo;
import com.cloud.utilFun.LogWriter;

/**
 * 在用此类获得虚拟机的监控数据时，要先查看该虚拟机是否能够正常被ganglia监控；
 * 查看的方法为：cd /var/lib/ganglia/rrds/dartcloud
 * 然后查看该目录下是否有对应的虚拟机监控信息。
 */

public class VMService {
	
	private static String[] hostnames = null;
	private static String[] ipaddrs = null;
	//获取数据中心的配置信息.
	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public VMService(){
		
	}
	
	public static List<VMInfo> getVMInfo(String[] vmNameList)
	{
		hostnames = new String[vmNameList.length];
		ipaddrs = new String[vmNameList.length];
		hostnames = vmNameList;
		for(int i=0; i<vmNameList.length; i++){
			String host = vmNameList[i];
			int id = Integer.parseInt(host.substring(4));
			String ip = hostname2IP(id);
			ipaddrs[i] = ip;
		}
		
		return getVMInfoClass(ipaddrs);
	}
	
	/**
	 * 将虚拟机id所对应的IP返回。如果没有找到相关的IP信息，则返回null.
	 * @param id
	 * @return
	 */
	public static String hostname2IP(int id)
	{
		String ip = null;
		try{
			Client oneClient = new Client(dcInfo.getSecret(), dcInfo.getEndpoint());
			OneResponse rc = VirtualMachine.info(oneClient, id);
			String arr = rc.getMessage();
			
			if(arr != null)
			{
				int start = arr.indexOf("<IP><![CDATA[");
				int stop = arr.indexOf("]]></IP>");
				
				if(start != -1 && stop!=-1)
					ip = arr.substring(start+13, stop);
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 
	 * @param ips
	 * @return
	 */
    public static List<VMInfo> getVMInfoClass(String[] ips){
    	
        List<VMInfo> list = new ArrayList<VMInfo>();

        Socket socket = null;
    	String XMLString = "";
    	String temp;
    	Document document = null;
    	
    	String name = "";
    	String ip = "";
    	String cpu_idle = "";
    	String cpu_users = "";
    	String cpu_systems = "";
    	String mem_free = "";
    	String mem_total = "";
    	
        double cpu_used = 0;
        double cpu_user = 0;
        double cpu_system = 0;
        double mem_used = 0;
        
    	try {
    		//与收集ganglia监控信息文件的服务器建立Socket连接。
			socket = new Socket(dcInfo.getMainHostIp(), 8651); 
			BufferedReader netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while((temp = netIn.readLine()) != null)
				XMLString = XMLString.concat(temp+"\r\n");
			netIn.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DocumentBuilder parser = null;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} 
        try {
			document = parser.parse( new InputSource(new StringReader(XMLString)));
			
			//document1 = parser.parse( new InputSource(new StringReader(s)));
			//System.out.print(document);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Node root = document.getDocumentElement(); 
		//System.out.print(root.getFirstChild().getNodeType());
	      /** 如果root有子元素 */ 
	      if(root.hasChildNodes()) 
	       { 
	         /** ftpnodes */ 
	          NodeList grid = root.getChildNodes(); 
	          
	          //System.out.print(ftpnodes[0])
	         /** 循环取得ftp所有节点 */ 
	         for (int i=0; i<grid.getLength(); i++)
	         {
	        	 if(grid.item(i).getAttributes() != null){
	        		 NodeList clust = grid.item(i).getChildNodes();
	        		 for(int k=0; k<clust.getLength(); k++){
	        			 if(clust.item(k).getAttributes() != null){
//	        				 cluster = clust.item(k).getAttributes().getNamedItem("NAME").getNodeValue();
	        				 
	        				 NodeList host = clust.item(k).getChildNodes();
	        				 for(int j=0; j<host.getLength(); j++){
			        			 if(host.item(j).getAttributes() != null){
//			        				 name = host.item(j).getAttributes().getNamedItem("NAME").getNodeValue();
			        				 ip = host.item(j).getAttributes().getNamedItem("IP").getNodeValue();
			        				 for(int m=0; m<ipaddrs.length; m++){
			        					 if(ipaddrs[m].equals(ip)){
			        						 name = hostnames[m];
			        						 
			        						 NodeList metric = host.item(j).getChildNodes();
					        				 for(int l=0; l<metric.getLength(); l++){
							        			 if(metric.item(l).getAttributes() != null){
							        				 
							        				 if( metric.item(l).getAttributes().getNamedItem("NAME").getNodeValue().equals("cpu_user")){
							        					 cpu_users = metric.item(l).getAttributes().getNamedItem("VAL").getNodeValue();
							        				 }
							        				 else if( metric.item(l).getAttributes().getNamedItem("NAME").getNodeValue().equals("cpu_idle")){
							        					 cpu_idle = metric.item(l).getAttributes().getNamedItem("VAL").getNodeValue();
							        				 }
							        				 else if( metric.item(l).getAttributes().getNamedItem("NAME").getNodeValue().equals("mem_free")){
							        					 mem_free = metric.item(l).getAttributes().getNamedItem("VAL").getNodeValue();
							        				 }
							        				 else if( metric.item(l).getAttributes().getNamedItem("NAME").getNodeValue().equals("cpu_system")){
							        					 cpu_systems = metric.item(l).getAttributes().getNamedItem("VAL").getNodeValue();
							        				 }
							        				 else if( metric.item(l).getAttributes().getNamedItem("NAME").getNodeValue().equals("mem_total")){
							        					 mem_total = metric.item(l).getAttributes().getNamedItem("VAL").getNodeValue();
							        				 }
							        			 }
					        				 }
					        				 if(cpu_users != "")
					        					 cpu_user = Double.parseDouble(cpu_users);
					        				 if(cpu_systems != "")
					        					 cpu_system = Double.parseDouble(cpu_systems);
					        				 if(cpu_idle != "")
					        					 cpu_used = 100.0 - Double.parseDouble(cpu_idle);
					        				 if(mem_total != "" && Double.parseDouble(mem_total) != 0)
					        					 mem_used = (1.0 - (double)(Double.parseDouble(mem_free)/Double.parseDouble(mem_total))) * 100.0;
					        				 
					        				 list.add(new VMInfo(name, ip, cpu_used, cpu_user, cpu_system, mem_used));
			        					 }
			        				 }

			        			 }
	        				 }
	        			 }
	        		 }
	        	 }
	          } 
	       }
        return list;
    }
    
    /**
     * 返回虚拟机的cpu使用率
     *
     * @param strVmId 虚拟机ID
     */
    public static double getCpuUsage(String strVmId)
    {
    	double ret = 0;
    	String strVmName[] = {"one-"+strVmId};
    	List<VMInfo> vmInfoList = new ArrayList<VMInfo>();
    	vmInfoList = VMService.getVMInfo(strVmName);
    	if(vmInfoList != null && vmInfoList.size()!=0)
    	{
    		VMInfo onevm = new VMInfo();
    		onevm = vmInfoList.get(0);
    		ret = onevm.cpu_used;
    	}
    	return ret;
    }
    
    /**
     * 返回虚拟机的Mem使用率
     *
     * @param strVmId 虚拟机ID
     */
    public static double getMemUsage(String strVmId)
    {
    	double ret = 0;
    	String strVmName[] = {"one-"+strVmId};
    	List<VMInfo> vmInfoList = new ArrayList<VMInfo>();
    	vmInfoList = VMService.getVMInfo(strVmName);
    	if(vmInfoList != null && vmInfoList.size()!=0)
    	{
    		VMInfo onevm = new VMInfo();
    		onevm = vmInfoList.get(0);
    		ret = onevm.mem_used;
    	}
    	return ret;
    }
    
    public static String getHello()
	{
		return "Hello";
	}
    
}
