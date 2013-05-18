package com.cloud.resourceMonitor;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opennebula.client.Client;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cloud.config.DataCenterInfo;
import com.cloud.utilFun.LogWriter;

public class VMMonitor {
	
	//获取数据中心的配置信息.
	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	private static LogWriter logWriter = LogWriter.getLogWriter();
	
	/**
     * 输入虚拟机id，返回其对应的IP
     *
     * @param id 虚拟机id
     */
	public  String oneVmId2IP(int id)
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
			logWriter.log(e);
		}
		return ip;
	}
	
	/**
     * 输入一组虚拟机id，返回其对应的IP
     *
     * @param arrIp 一组虚拟机id
     */
	public List<String> vmId2Ip(ArrayList<Integer> arrIp ){
		List<String> list = new ArrayList<String>();
		
		Iterator<Integer> iter = arrIp.iterator();
		while(iter.hasNext()){
			Integer vmId = iter.next();
			String strIp = oneVmId2IP(vmId);
			list.add(strIp);
		}
		return list;
	}
	

	/**
     * 获取GangLia监控到的数据，并将这些数据以XML的格式写入到服务器磁盘中。
     *
     * @param 
     */
    public void getVMInfoClass()
    {
    	Socket socket = null;
    	String XMLString = "";
    	String temp;
    	Document document = null;
    	
    	String ip = null;
    	String cpu_idle = null;
    	String cpu_users = null;
    	String cpu_systems = null;
    	String mem_free = null;
    	String mem_total = null;
    	String monitorFileDir = dcInfo.getMonitorFileDir();
    	
        double cpu_used;
        double cpu_user;
        double cpu_system;
        double mem_used;
        
    	try {
			socket = new Socket(dcInfo.getMainHostIp(), 8651); 
			BufferedReader netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while((temp = netIn.readLine()) != null)
				XMLString = XMLString.concat(temp + "\r\n");
			netIn.close();
			socket.close();
		} catch (UnknownHostException e) {
			logWriter.log(e);
		} catch (IOException e) {
			logWriter.log(e);
		}
		
		DocumentBuilder parser = null;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logWriter.log(e);
		} 
        try {
			document = parser.parse( new InputSource(new StringReader(XMLString)));
			
		} catch (SAXException e) {
			logWriter.log(e);
		} catch (IOException e) {
			logWriter.log(e);
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
	        					 
	        					 try{
	        			            
	        						 if(host.item(j).getAttributes() != null){
//			        				 name = host.item(j).getAttributes().getNamedItem("NAME").getNodeValue();
	        							 ip = host.item(j).getAttributes().getNamedItem("IP").getNodeValue();
			        				 
	        							 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		        			            DocumentBuilder builder = factory.newDocumentBuilder();
		        			            
		        			            File cpuf = new File(monitorFileDir + ip + "_cpu.xml");
		        			            File memf = new File(monitorFileDir + ip + "_mem.xml");
		        			            if(!cpuf.exists())
		        			            	initCpuXMLFile(ip);
		        			            if(!memf.exists())
		        			            	initMemXMLFile(ip);
		        			            
		        			            Document document1 = builder.parse(new FileInputStream(cpuf));
		        			            Node root1 = document1.getDocumentElement();
		        			            root1.removeChild(root1.getFirstChild());
		        			            
		        			            Document document2 = builder.parse(new FileInputStream(memf));
		        			            Node root2 = document2.getDocumentElement();
		        			            root2.removeChild(root2.getFirstChild());
			        						 
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
					        				 
					        				 cpu_user = Double.parseDouble(cpu_users);
					        				 cpu_system = Double.parseDouble(cpu_systems);
					        				 cpu_used = 100.0 - Double.parseDouble(cpu_idle);
					        				 mem_used = (1.0 - (double)(Double.parseDouble(mem_free)/Double.parseDouble(mem_total))) * 100.0;
					        				 
					        				 Element cpu_new = document1.createElement("cpu");
					        				 root1.appendChild(cpu_new);
					        				 
					        				 Element mem_new = document2.createElement("mem");
					        				 root2.appendChild(mem_new);
					        				 
					        				 Date now = new Date();
					        		         DateFormat df = DateFormat.getTimeInstance();
					        		         String date = df.format(now);
					        		         
					        		         cpu_new.setAttribute("time", date);
					        		         Element element = document1.createElement("cpu_used");
					     					 element.appendChild(document1.createTextNode(Double.toString(cpu_used)));
					     					 cpu_new.appendChild(element);
					     					 
					        		         element = document1.createElement("cpu_user");
					     					 element.appendChild(document1.createTextNode(Double.toString(cpu_user)));
					     					 cpu_new.appendChild(element);
					     					 
					        		         element = document1.createElement("cpu_system");
					     					 element.appendChild(document1.createTextNode(Double.toString(cpu_system)));
					     					 cpu_new.appendChild(element);
					     					 
					     					 mem_new.setAttribute("time", date);
					     					 element = document2.createElement("mem_used");
					     					 element.appendChild(document2.createTextNode(Double.toString(mem_used)));
					     					 mem_new.appendChild(element);
					     					 
					     					doc2XmlFile(document1, monitorFileDir + ip + "_cpu.xml");
					     					doc2XmlFile(document2, monitorFileDir + ip + "_mem.xml");
//					        				 list.add(new VMInfo(name, ip, cpu_used, cpu_user, cpu_system, mem_used));
			        			 }
	        				 }catch(Exception e){ 
		        		           logWriter.log(e);
	  	      	           }
	        			 }
	        			 }
	        		 }
	        	 }
	          } 
	       }
    }
    
    /**
     * 将IP地址作为文件名称来生成CPU的监控信息文件。
     * 
     * @param ip
     * @return
     */
    public static boolean initCpuXMLFile(String ip){
    	if(null == ip || ip == "")
    		return false;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document document1 = builder.newDocument();
            Element cpuMetric = document1.createElement("cpu_metric");
            document1.appendChild(cpuMetric);
            
            Element[] cpu = new Element[20];
            for(int i=0; i<20; i++){
            	cpu[i] = document1.createElement("cpu");
            	cpuMetric.appendChild(cpu[i]);
            	Element element1 = document1.createElement("cpu_used");
				element1.appendChild(document1.createTextNode("0.00"));
				cpu[i].appendChild(element1);
				
				Element element2 = document1.createElement("cpu_system");
				element2.appendChild(document1.createTextNode("0.00"));
				cpu[i].appendChild(element2);
				
				Element element3 = document1.createElement("cpu_user");
				element3.appendChild(document1.createTextNode("0.00"));
				cpu[i].appendChild(element3);
            }
            //将生产的文件写入到本地文件夹中。
            logWriter.log(dcInfo.getMonitorFileDir() + ip + "_cpu.xml");
//            doc2XmlFile(document1, dcInfo.getMonitorFileDir() + ip + "_cpu.xml");
            doc2XmlFile(document1, "/usr/local/tomcat/webapps/DartCloud/data/" + ip + "_cpu.xml");
		}
		catch(Exception e)
		{ 
			logWriter.log(e);
			return false;
        }
		return true;
	}
    
    /**
     * 将IP地址作为文件名称来生成memory的监控信息文件。
     * @param ip
     * @return
     */
    public static boolean initMemXMLFile(String ip)
    {
    	if(null == ip || ip == "")
    		return false;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document document2 = builder.newDocument();
            Element memMetric = document2.createElement("mem_metric");
            document2.appendChild(memMetric);
            
            Element[] mem = new Element[20];
            for(int i=0; i<20; i++){
            	mem[i] = document2.createElement("mem");
            	memMetric.appendChild(mem[i]);
            	
            	Element element = document2.createElement("mem_used");
				element.appendChild(document2.createTextNode("0.00"));
				mem[i].appendChild(element);
            }
            
//			doc2XmlFile(document2, dcInfo.getMonitorFileDir() + ip + "_mem.xml");
			doc2XmlFile(document2, "/usr/local/tomcat/webapps/DartCloud/data/" + ip + "_mem.xml");
		}
		catch(Exception e)
		{ 
			logWriter.log(e);
			return false;
        }
		return true;
	}
    
    /**
	 * 写文件操作
	 * 
	 * @param document 文件内容
	 * @param filename 文件名称
	 * @return
	 */
    public static boolean doc2XmlFile(Document document, String filename)
    {
    	boolean flag = true;
    	try
		{ 
    		/** 将document中的内容写入文件中   */
    		TransformerFactory tFactory = TransformerFactory.newInstance();
    		Transformer transformer = tFactory.newTransformer();
    		/** 编码 */
    		//transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
		     DOMSource source = new DOMSource(document);
		     //logWriter.log("VMMonitor: "filename);
		     StreamResult result = new StreamResult();
		     result.setOutputStream(new FileOutputStream(new File(filename)));
		     transformer.transform(source, result);
		 }
    	 catch(Exception e)
		 {
    		 flag = false;
    		 logWriter.log(e);
		 }
    	 return flag;
    }
    
    
}
