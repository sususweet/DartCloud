package com.cloud.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import org.opennebula.client.Client;
import com.cloud.utilFun.LogWriter;
import com.cloud.dao.JobService;
import com.cloud.dao.ZoneService;
import com.cloud.entities.Job;
import com.cloud.jobManagement.ImageOperation;

/**
 * 不同类型的虚拟机配置信息.
 * 
 * @author Yan Haiming
 *
 */

public class TemplateInfo 
{
	private String str_template = "";
	private static LogWriter logWriter = LogWriter.getLogWriter();
	
	/**
	 * Template的构造函数;
	 * @param strTemplateName 模板文件名，如ubuntu12.04.txt，改文件在com.cloud.config目录下;
	 * @param job 用户新建的业务信息
	 */
	public TemplateInfo(String strTemplateName,Job job)
	{
		str_template = getTemplateFileStr(strTemplateName);
		initTemplate(job);
	}
	
	/**
	 * 获取模板strTemplateName中的内容;
	 * @param strTemplateName
	 * @return
	 */
	private String getTemplateFileStr(String strTemplateName )
	{
		String strRet = "";
		try{
			//获取Tomcat下本项目的根目录。
			String projectRootDir = this.getClass().getResource("/").toString().substring(5);
			//获取绝对路径。
			String filePath = projectRootDir + "conf/"+strTemplateName;
			File file = new File(filePath);
			logWriter.log("move into dir: " + filePath);
			
			//读取配置文件中的内容并返回
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str_temp = "";
			while((str_temp = in.readLine() ) != null)
			{
				strRet += str_temp + "\n";
			}
			in.close();
		}
		catch(Exception e){
			logWriter.log("Reading template file error: " + e);
		}
		return strRet;
	}
	
	/**
	 * 根据job中的内容初始化虚拟机模板文件template，如cpu数量、内存大小等信息；
	 * @param job
	 * @return 
	 */
	private boolean initTemplate(Job job)
	{
		try {
			int iZoneId = job.getiZoneId();
			int iOsType = job.getiOSType();
			String zoneHost = ZoneService.getZonehostIp(job.getiZoneId(), ZoneService.ZONE_HOST);
			String zoneSecret = ZoneService.getZoneSecret(iZoneId);
			String nfsServer = JobService.getNfsServer(iZoneId);
			String zoneEndpoint = "http://" + zoneHost + ":2633/RPC2";
			Client oneClient = new Client(zoneSecret, zoneEndpoint);
			ImageOperation imageOperation = new ImageOperation(oneClient);
			
			int iImageId = imageOperation.getAvailableImage(iOsType);
			//注意：配置文件中不要把CPU=1这个选项去掉，否则openNebula会报错
			//response: [VirtualMachineAllocate] user [2] CPU attribute must be a positive float or integer value
			str_template = str_template.replaceAll("<vm_vcpu_num>", String.valueOf(job.getiCpuNum()));
			str_template = str_template.replaceAll("<vm_memory_size>", String.valueOf(job.getiMemSize()));
			str_template = str_template.replaceAll("<vm_name>", String.valueOf(iImageId));
			str_template = str_template.replaceAll("<image_id>", String.valueOf(iImageId));
			str_template = str_template.replaceAll("<pass>", job.getstrVmPass());
			str_template = str_template.replaceAll("<jobid>", String.valueOf(job.getiJobId()));
			str_template = str_template.replaceAll("<nfsserver>", nfsServer);
		} catch (Exception e) {
			logWriter.log("Exception in initTemplate: " + e);
			return false;
		}
		return true;
		
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
