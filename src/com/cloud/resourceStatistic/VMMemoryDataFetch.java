package com.cloud.resourceStatistic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.cloud.config.DataCenterInfo;

public class VMMemoryDataFetch 
{
	public ArrayList<VMMemory> fetchVMMemory(String ip)
	{
		ArrayList<VMMemory> lis = new ArrayList<VMMemory>();
		
		DataCenterInfo dataCenterInfo = DataCenterInfo.getInstance();
		String hostname = dataCenterInfo.getMainHostIp(); 
		String username = dataCenterInfo.getUsername();
		String password = dataCenterInfo.getPassword();
		//上面这四句组建出访问的服务器路径，服务器登录的用户名和密码
		
		
		String vmDirectory = "/var/lib/ganglia/rrds/dartcloud/" + ip;//虚拟机的监控文件夹位置，根据IP指定
		String vmMemoryStartTime = null;//记录CPU的开始监控时间
		String vmMemoryEndTime = null;//记录CPU的结束监控时间
		
		
		try {
			
			Connection conn = new Connection(hostname);//建立服务器连接
			conn.connect();
			
			/* 身份验证 */ 
			boolean isAuthenticated = conn.authenticateWithPassword(username, password); 
			if (isAuthenticated == false) 
				throw new IOException("获取数据失败！");
			
			/* 建立对话 */ 
			Session sess = conn.openSession();
			
			//***************获取开始监控时间************************************************
			String fecthOrder = "rrdtool first " + vmDirectory + "/mem_free.rrd";//获取监控CPU的开始时间
			sess.execCommand(fecthOrder);
			InputStream stdout = new StreamGobbler(sess.getStdout()); 
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			vmMemoryStartTime = br.readLine();
			sess.close();
			
			/*
			 * 注意：此处的session回话每次只能运行一条rrdtool命令，运行完须立即关闭，否则再运行下一条时会爆IOException，提示信息
			 * 为“已经有远程会话在运行”，所以每次用完即关，到用的时候再用conn.openSession()打开会话，这种方法略麻烦，但是这是我现在唯一能找到的
			 * 解决方案！
			 * */
			
			//***************获取结束监控时间************************************************
			sess = conn.openSession();
			fecthOrder = "rrdtool last " + vmDirectory + "/mem_free.rrd";//获取监控CPU的结束时间
			sess.execCommand(fecthOrder);
			stdout = new StreamGobbler(sess.getStdout()); 
			br = new BufferedReader(new InputStreamReader(stdout));
			vmMemoryEndTime = br.readLine();
			sess.close();
			
			//***************重头戏：获取VMCPU的所有监控数据,包括cpu_idle,cpu_user,cpu_system*****
			VMMemory temp;
			
			sess = conn.openSession();
			fecthOrder = "rrdtool fetch " + vmDirectory + "/mem_free.rrd AVERAGE -s " + vmMemoryStartTime + " -e " + vmMemoryEndTime;
			sess.execCommand(fecthOrder);
			stdout = new StreamGobbler(sess.getStdout()); 
			br = new BufferedReader(new InputStreamReader(stdout));
			br.readLine();
			br.readLine();//跳过数据列表的表头“sum”和第二行的空栏，直接进入正式数据处理
			while (true) 
			{ 
				temp = new VMMemory();
				String line = br.readLine();
				//System.out.println("--->" + line);
				
				if (line == null) break; 
				
				String[] a = line.split(": ");
				temp.memory_TimeStamp = a[0];
				if(a[1].equals("-nan")) continue;
				else
				{
					int t = Integer.valueOf(a[1].substring(15, 16));//取出科学计数法中的幂，即字符串最后一位数字
					//System.out.println("t = " + t);
					temp.memory_Free = Double.valueOf(a[1].substring(0, 9)) * Math.pow(10, t);
				}
				lis.add(temp);
				//System.out.println(temp.cpu_Idle + " ** " + temp.cpu_System + " ** " + temp.cpu_TimeStamp + " ** " + temp.cpu_User);
			}
			sess.close();
			
			//ArrayList雏形已形成，后面的两个fetch操作只需要insert对应值，不需要再add对象。
			sess = conn.openSession();
			fecthOrder = "rrdtool fetch " + vmDirectory + "/mem_total.rrd AVERAGE -s " + vmMemoryStartTime + " -e " + vmMemoryEndTime;
			sess.execCommand(fecthOrder);
			stdout = new StreamGobbler(sess.getStdout()); 
			br = new BufferedReader(new InputStreamReader(stdout));
			br.readLine();
			br.readLine();//跳过数据列表的表头“sum”和第二行的空栏，直接进入正式数据处理
			
			int i;//ArrayList<VMCPU>的游标
			i = 0;
			while (true) 
			{ 
				String line = br.readLine();
				//System.out.println("--->" + line);
				
				if (line == null) break; 
				
				String[] a = line.split(": ");
				if(a[1].equals("-nan")) continue;
				else
				{
					int t = Integer.valueOf(a[1].substring(15, 16));//取出科学计数法中的幂，即字符串最后一位数字
					lis.get(i).memory_Total = Double.valueOf(a[1].substring(0, 9)) * Math.pow(10, t);//保留两位有效数字
				}
				i ++;
			}
			sess.close();
			conn.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return lis;
	}
}
