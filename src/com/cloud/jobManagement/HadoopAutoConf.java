package com.cloud.jobManagement;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.InputStream;
import ch.ethz.ssh2.*;
import java.nio.charset.*;

import com.cloud.config.DataCenterInfo;
import com.cloud.utilFun.LogWriter;

public class HadoopAutoConf 
{
	/**
	 * 
	 * @param strIpList Hadoop集群的ip列表
	 * @return
	 * @throws FileNotFoundException
	 */
	private static DataCenterInfo dcInfo = DataCenterInfo.getInstance();
	private static LogWriter logWriter = LogWriter.getLogWriter();
	
	public static boolean GenerateXmlFiles(ArrayList<String> strIpList) throws FileNotFoundException,Exception
	{
		String strHadoopDataDir = dcInfo.getHadoopGenerDataDir();
		if(null == strHadoopDataDir)
			strHadoopDataDir = "/usr/local/tomcat/webapps/DartCloud/hadoopData/";
		logWriter.log("strHadoopDataDir: " + strHadoopDataDir);
		if(strIpList.size() == 0){
			logWriter.log("没有参数");
		}
		else
		{
			PrintWriter hdfs = new PrintWriter(strHadoopDataDir + "hdfs-site.xml");
			PrintWriter core = new PrintWriter(strHadoopDataDir + "core-site.xml");
			PrintWriter mapred = new PrintWriter(strHadoopDataDir + "mapred-site.xml");
			PrintWriter master = new PrintWriter(strHadoopDataDir + "masters");
			PrintWriter slave = new PrintWriter(strHadoopDataDir + "slaves");
			int num = strIpList.size() -1;
			
			if(num > 3) //HDFS副本的最大数目为三个。
				num = 3;
			
			hdfs.println("<?xml version=\"1.0\"?>");
			hdfs.println("<?xml-stylesheet type=\"text/xsl\" href\"figuration.xsl\"?>");
			hdfs.println("<!-- Put site-specific property overrides in this file. -->");
			hdfs.println("<configuration>");
			hdfs.println("<property>");
			hdfs.println("  <name>dfs.replication</name>");
			hdfs.println("  <value>" + num + "</value>");
			hdfs.println("  <description>Default block replication.The actual number of replications can be specified when the file is created.The default is used if replication is not specified in create time.</description>");
			hdfs.println("</property>");
			hdfs.println("</configuration>");
			hdfs.close();
			core.println("<?xml version=\"1.0\"?>");
			core.println("<?xml-stylesheet type=\"text/xsl\" href=\"figuration.xsl\"?>");
			core.println("<!-- Put site-specific property overrides in this file. -->");
			core.println("<configuration>");
			core.println("<property>");
			core.println("  <name>hadoop.tmp.dir</name>");
			core.println("  <value>/root/hadoop</value>");
			core.println("  <description>A base for other temporary directories.</description>");
			core.println("</property>");
			core.println("<property>");
			core.println("  <name>fs.default.name</name>");
			core.println("  <value>hdfs://" + strIpList.get(0) + ":54310</value>");
			core.println("  <description>The name of the default file system.  A URI whose scheme and authority determine the FileSystem implementation.  The uri's scheme determines the config property (fs.SCHEME.impl) naming the FileSystem implementation class.  The uri's authority is used to determine the host, port, etc. for a filesystem.</description>");
			core.println("</property>");
			core.println("</configuration>");
			core.close();
			mapred.println("<?xml version=\"1.0\"?>");
			mapred.println("<?xml-stylesheet type=\"text/xsl\" href=\"figuration.xsl\"?>");
			mapred.println("<!-- Put site-specific property overrides in this file. -->");
			mapred.println("<configuration>");
			mapred.println("<property>");
			mapred.println("  <name>mapred.job.tracker</name>");
			mapred.println("  <value>" + strIpList.get(0) + ":54311</value>");
			mapred.println("  <description>The host and port that the MapReduce job tracker runs at. If \"local\", then jobs are run in-process as a single map and reduce task. </description>");
			mapred.println("</property>");
			mapred.println("</configuration>");
			mapred.close();
			master.println(strIpList.get(0));
			master.close();
			for ( int i = 1; i < strIpList.size(); i++)
			{
				slave.println(strIpList.get(i));
			}
			slave.close();
			
		}
		return true;
	}
	
	/**
	 * 
	 * @param strIpList Hadoop集群的ip列表
	 * @return
	 * @throws Exception
	 */
	public static boolean Scp_XML(ArrayList<String> strIpList)throws Exception
	{
		Connection con = new Connection("192.168.100.250");
        con.connect();
        boolean result = con.authenticateWithPassword("root", "sys#000");
        logWriter.log("con.authenticateWithPassword result: " + result);
		for( int i = 0; i < strIpList.size(); i++)
		{
			//String str = "sh /usr/local/tomcat/webapps/DartCloud/WEB-INF/classes/com/cloud/jobManagement/scp_xml.sh " + strIpList.get(i);
			//logWriter.log(str);		
			//Runtime.getRuntime().exec(str);
			Session session1 = con.openSession();
			session1.execCommand("scp /usr/local/tomcat/webapps/DartCloud/hadoopData/masters root@" + strIpList.get(i) + ":/root/" );
			Session session2 = con.openSession();
			session2.execCommand("scp /usr/local/tomcat/webapps/DartCloud/hadoopData/slaves root@" + strIpList.get(i) + ":/root/" );
			Session session3 = con.openSession();
			session3.execCommand("scp /usr/local/tomcat/webapps/DartCloud/hadoopData/core-site.xml root@" + strIpList.get(i) + ":/root/" );
			Session session4 = con.openSession();
			session4.execCommand("scp /usr/local/tomcat/webapps/DartCloud/hadoopData/mapred-site.xml root@" + strIpList.get(i) + ":/root/" );
			Session session5 = con.openSession();
			session5.execCommand("scp /usr/local/tomcat/webapps/DartCloud/hadoopData/hdfs-site root@" + strIpList.get(i) + ":/root/" );
			Session session6 = con.openSession();
			session6.execCommand("scp /root/.ssh root@" + strIpList.get(i) + ":/root/" );     
		}
		return true;
	}
	
	/**
	 * 将远程终端上的标准输出进行处理；
	 * 处理后格式：每一行都是String。
	 * @param in 远程终端的标准输出
	 * @return
	 * @throws Exception
	 */
	private static String processStream(InputStream in) throws Exception {
		String charset = Charset.defaultCharset().toString();
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }
	
	/**
	 * 远程启动Hadoop集群，登录到集群的Namenode执行脚本。
	 * @param strNameNodeIp 
	 * @param strNamenodeRootPass namenode的密码
	 * @return
	 */
	public static boolean Remote_shell(String strNameNodeIp, String strNamenodeRootPass) {
    	
        try {
        	InputStream stdOut = null;
            InputStream stdErr = null;
            String outStr = "";
            String outErr = "";
            Connection con = new Connection(strNameNodeIp);
            con.connect();
            boolean result = con.authenticateWithPassword("root", strNamenodeRootPass);
            logWriter.log("result:" + result);
            Session session1 = con.openSession();
            
            //执行的路径在镜像中已经固定。
            session1.execCommand("/root/hadoop/hadoop-1.0.4/bin/hadoop namenode -format");
            stdOut = new StreamGobbler(session1.getStdout());
            outStr = processStream(stdOut);
            stdErr = new StreamGobbler(session1.getStderr());
            outErr = processStream(stdErr);
            logWriter.log("outStr=" + outStr);
            logWriter.log("outErr=" + outErr);
            logWriter.log("ExitCode: " + session1.getExitStatus());
            session1.close();
            Session session2 = con.openSession();
            session2.execCommand("/root/hadoop/hadoop-1.0.4/bin/start-all.sh");
            stdOut = new StreamGobbler(session2.getStdout());
            outStr = processStream(stdOut);
            stdErr = new StreamGobbler(session2.getStderr());
            outErr = processStream(stdErr);
            logWriter.log("outStr=" + outStr);
            logWriter.log("outErr=" + outErr);
            logWriter.log("ExitCode: " + session2.getExitStatus());
            session2.close();
            con.close();
        } catch (Exception ex) {
            logWriter.log(ex.getLocalizedMessage());
        }
        return true;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
