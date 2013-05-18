/*
 * JobOperation
 * 
 * @version 1.0,
 * 
 * @Date 2012-3-14
 * 
 * @author  Frank
*/
package com.cloud.jobManagement;

import com.cloud.resourceMonitor.VMMonitor;
import com.cloud.entities.*;
import com.cloud.config.DataCenterInfo;
import com.cloud.config.TemplateInfo;
import com.cloud.dao.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opennebula.client.Client;
import org.opennebula.client.vm.VirtualMachine;
import com.cloud.utilFun.*;

/**
 * JobOpertion类主要提供和业务相关的一些操作，具体包括：新建、修改、删除、查询；
 */
public class JobOperation {
	
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public JobOperation(){	
	}
	
	/**
     * 新建一个业务
     *
     * @param job 业务对象job中是所创建的业务的一些配置信息。
	 * @throws Exception 
     */
	public static boolean createOneJob(Job job) throws Exception{
		
		boolean result = true; 
		int iJobId = 0;
		
		//更新数据库中相应的记录；
		try {
			//由于jobId字段在数据库中是自动增长的，而且在申请计算资源更新vm_info表时需要用到这个字段，
			//所以要先获取数据库中job的jobId;
			iJobId = JobService.addJob(job);
			job.setiJobId(iJobId);
		} catch (Exception e) {
			result = false;
			logWriter.log(e);
		}
		
		//若数据库更新成功，则向数据中心申请计算资源；否则返回false;
		if(result){
			String zoneHost = ZoneService.getZonehostIp(job.getiZoneId(), ZoneService.ZONE_HOST);
			StorageAllocator sa = new StorageAllocator(zoneHost);
			sa.allocateDisk(iJobId);
			result = applyComputeResource(job);
			//申请存储如何实现?
		}		     		
		return result;
	}
	
	/**
	 * 在某一业务下添加新的虚拟机。
	 * @param job 该业务的详细信息。
	 * @return
	 * @throws Exception
	 */
	public static boolean addVms2Job(Job job) throws Exception{
		boolean result = true;
		try {
			String zoneHost = ZoneService.getZonehostIp(job.getiZoneId(), ZoneService.ZONE_HOST);
			StorageAllocator sa = new StorageAllocator(zoneHost);
			sa.allocateDisk(job.getiJobId());
			result = applyComputeResource(job);
		} catch (Exception e) {
			result = false;
			throw new Exception(e);
		}
		return result;
	}
	
	/**
	 * 通过用户在前端所选择的操作系统类型，来获得不同虚拟机配置文件的名称；
	 * @param iOsTypeId 操作系统类型
	 * @return
	 */
	public static String getTempfileName(int iOsTypeId)
	{
		if(iOsTypeId == OsType.LINUX_DEBIAN)
		{
			return VmTempfileName.LINUX_DEBIAN;
		}
		else if(iOsTypeId == OsType.LINUX_UBUNTU)
		{
			return VmTempfileName.LINUX_UBUNTU;
		}
		else if(iOsTypeId == OsType.LINUX_CENTOS)
		{
			return VmTempfileName.LINUX_CENTOS;
		}
		else if(iOsTypeId == OsType.LINUX_FEDORA)
		{
			return VmTempfileName.LINUX_FEDORA;
		}
		else if(iOsTypeId == OsType.WIN7)
		{
			return VmTempfileName.WIN7;
		}
		else if(iOsTypeId == OsType.WINXP)
		{
			return VmTempfileName.WINXP;
		}
		else
		{
			logWriter.log("虚拟机操作系统类型iOsType出错！");
			return null;
		}
	}
	
	
	/**
     * 调用openNebula相关接口，向数据中心申请计算资源。
     *
     * @param job 业务对象job中是所创建的业务的一些配置信息。
     */
	public static boolean applyComputeResource(Job job)throws Exception{
		
		try{
			String strVmTempName = getTempfileName(job.getiOSType());
			String strVmPass = job.getstrVmPass();
			String zoneHost = ZoneService.getZonehostIp(job.getiZoneId(), ZoneService.ZONE_HOST);
			String zoneSecret = ZoneService.getZoneSecret(job.getiZoneId());
			String zoneEndpoint = "http://" + zoneHost + ":2633/RPC2";
			logWriter.log("创建虚拟机时的endpoint: " + zoneSecret + " " + zoneEndpoint);
			Client oneClient = new Client(zoneSecret, zoneEndpoint);
			
			//操作系统类型
			int iOsType = job.getiOSType();
			
			StorageAllocator sa = new StorageAllocator(zoneHost);
			
			//虚拟机的网络开通需要经过管理员的审批。
			//NetworkAllocator na = new NetworkAllocator(zoneHost);
	        ImageOperation imageOperation = new ImageOperation(oneClient);
	        
			for(int i=0; i < job.getiVmNum(); i++){
		        
				/*
		         * 以下面这种方式得到虚拟机对象只有其ID信息，没有其他具体的信息；
		         * 应从虚拟机池中得到虚拟机。
		         * 获取空闲的虚拟机镜像
		         * 获取一个可用的虚拟机镜像
		         */
		        int iImageId = imageOperation.getAvailableImage(iOsType);
		        logWriter.log("Current vm os type is :"+iOsType);
		        
		        if(iImageId == -1){
		        	logWriter.log("没有可用镜像！");
		        	return false;
		        }
		        //更新template，每个template对应一个虚拟机镜像；
		        TemplateInfo vmTempInfo = new TemplateInfo(strVmTempName,job);
		        logWriter.log("strVmTempName :" + strVmTempName);
				String vm_template = vmTempInfo.getStr_template();
		        logWriter.log(vm_template);
		        VirtualMachine vm = VmOperation.create(oneClient,vm_template);
		        
		        //使用了一个镜像，再补充一个，防止镜像不够用
		        //imageOperation.createImage(iOsType);

		        vm.info();
		        //获得该虚拟机的id，并将id与镜像的id的一一对应关系保存起来。
		        String strVmId = vm.getId();
		        strVmId = job.getiZoneId() + "-" + strVmId;
		        //imageMap.addMapInfo(iImageId, iVmId, iOsType);
		      //*******************调试***************************//
		        logWriter.log("虚拟机创建成功：id - "+strVmId + ", 虚拟机的密码是：" + strVmPass);
		        
		        //初始化申请到的每个虚拟机信息。
		        VM oneVmInfo = new VM();
		        oneVmInfo.setstrVmId(strVmId);
		        
		        String vmIp = VmOperation.vmId2IP(strVmId); //获取当前虚拟机的IP信息
		        String strVmName = vmIp + "_" + vm.getName();
		        oneVmInfo.setStrVmName(strVmName);
		        oneVmInfo.setiVmJobId(job.getiJobId());
		        oneVmInfo.setStrVmPass(strVmPass);
		        oneVmInfo.setiServerId(0); //先初始化为0
		        oneVmInfo.setiVMCpuNum(job.getiCpuNum());
		        oneVmInfo.setiVmMemorySize(job.getiMemSize());
		        oneVmInfo.setiVmStorageSize(job.getiStorageSize());
		        oneVmInfo.setiVmJobId(job.getiJobId());
		        oneVmInfo.setiImageId(iImageId);
		        oneVmInfo.setStrVmIp(VmOperation.vmId2IP(strVmId));
		        logWriter.log("虚拟机网络状态job.getiNetStatus() :" + job.getiNetStatus());
		        oneVmInfo.setiNetStatus(job.getiNetStatus());
		        
		        //申请到一个虚拟机以后，向数据库中的vm_info添加记录
		        VmService.addAnVmInfo(oneVmInfo);
		        
		        //创建虚拟机的监控信息文件。
		        initXMLFile(vmIp);
		        sa.addNfsEntry(job.getiJobId(), vmIp);
		        sa.refreshNfsEntry();
		       // na.addNetworkEntry(vmIp);
		        
			}
		}catch (Exception e){
            logWriter.log("Exception in creating Job " + e.getMessage());
            return false;
        }
		
		return true;
	}
	
	/**
	 * 删除一个业务，包括两个部分：修改数据库和回收计算资源。
     *
     * @param job 业务对象job中是所创建的业务的一些配置信息。
	 * @throws Exception 
     */
	public static boolean deleteOneJob(int iJobId) throws Exception{
		boolean result = true;
		//获取虚拟机中属于业务iJobId的所有vm_id
		ArrayList<VM> vmInfoList = JobService.getVmInfoListInAJob(iJobId);
		Iterator<VM> it = vmInfoList.iterator();
		
		while(it.hasNext())
		{
			VM tmpVm = it.next();
			try{
				String strVmId = tmpVm.getstrVmId();
				VmOperation.deleteOneVm(strVmId);
				//删除镜像
				//imageMap.freeImage(strVmId);
			}catch(Exception e){
				logWriter.log(e);
				result = false;
			}
			
			//若计算资源回收成功，
			//则调用VmService中的接口删除数据库中tmpVmId对应的记录.
			if(result){
				VmService.deleteVm(tmpVm.getstrVmId());
			}
			
		}
		
		//若之前的资源回收成功，则从数据库中删除相应的业务记录
		if(result){
			result = JobService.deleteJob(iJobId);
		}
		return result;
	}
	
	
	/**
	 * 根据虚拟机的id获得一组虚拟机的IP地址.
	 * @param vmIdList
	 * @return
	 */
	public static List<String> vmIdList2IPList(ArrayList<String> vmIdList) throws Exception
	{
		List<String> strIPList = new ArrayList<String>();
		Iterator<String> it = vmIdList.iterator();
		
		while(it.hasNext())
		{
			String strVmId = it.next();
			strIPList.add(VmOperation.vmId2IP(strVmId));
		}
		
		return strIPList;
	}
	
	/**
	 * 初始化一个GangLia监控文件，开始时全部参数均为0.
	 * 
	 * @param ip
	 */
	public static void initXMLFile(String ip){
		VMMonitor.initCpuXMLFile(ip);
		VMMonitor.initMemXMLFile(ip);
	}
	
	/**
	 * 在调用info函数后返回的RpcXml中查找一个子字符串，该字符串以startStr的后一个位置开始，并以endStr的前一个字符结束。
	 * @param rpcString
	 * @param startStr
	 * @param endStr
	 * @return
	 */
	public static String parseRpcStr(String rpcString, String startStr, String endStr)
	{
		int iLen = startStr.length();
		int beginIndex = rpcString.indexOf(startStr) + iLen;
		int endIndex = rpcString.indexOf(endStr);
		String strRet = rpcString.substring(beginIndex, endIndex);
		return strRet;
	}
	
	/**
	 * Hadoop自动部署函数
	 * @param iJobId 业务id，用于获取虚拟机域等相关信息。
	 * @param iNamenodeId Hadoop的集群namenode
	 * @param vmInfoList 集群的虚拟机信息
	 * @return
	 * @throws Exception
	 */
	public static boolean hadoopAutoConf(int iJobId,String strNamenodeId, ArrayList<VM> vmInfoList)throws Exception
	{
		DataCenterInfo dcInfo = DataCenterInfo.getInstance();
		logWriter.log("hadoopGen: " + dcInfo.getHadoopGenerDataDir());
		logWriter.log("MainHostIP: " + dcInfo.getMainHostIp());
		
		if(null == vmInfoList || vmInfoList.size() < 2)
		{
			logWriter.log("Hadoop 集群数量不足...");
			return false;
		}
		try 
		{
			ArrayList<String> strIpList = getClusterIpList(iJobId,strNamenodeId, vmInfoList);
			HadoopAutoConf.GenerateXmlFiles(strIpList);
			HadoopAutoConf.Scp_XML(strIpList);
			
			//获取namenode的password
			VM vmNamenode = VmService.getVmById(strNamenodeId);
			String strNamenodePass = vmNamenode.getStrVmPass();
			logWriter.log("get namenode pass word: " + strNamenodePass);
			HadoopAutoConf.Remote_shell(strIpList.get(0),strNamenodePass);
		} catch (Exception e) {
			logWriter.log(e);
		}
		
		return true;
	}
	
	/**
	 * 得到虚拟机的ip列表
	 * @param iJobId 业务id，用于获取虚拟机域等相关信息。
	 * @param iNamenodeId Hadoop的集群namenode
	 * @param vmInfoList 集群的虚拟机信息
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getClusterIpList(int iJobId,String strNamenodeId, ArrayList<VM> vmInfoList) throws Exception
	{
		logWriter.log("name node: " + strNamenodeId);
		ArrayList<String> strIpList = new ArrayList<String>();
		
		//namenode是strIpList中的第一个元素
		strIpList.add(VmOperation.vmId2IP(strNamenodeId));
		
		if(vmInfoList == null)
			return null;
		Iterator<VM> it = vmInfoList.iterator();
		while(it.hasNext())
		{
			VM tmpVm = it.next();
			
			//iNamenodeId的虚拟机ip已经在之前添加，是strIpList的第一个元素。
			if(tmpVm.getstrVmId() != strNamenodeId)
				strIpList.add(VmOperation.vmId2IP(tmpVm.getstrVmId()));
			logWriter.log("id :" + tmpVm.getstrVmId() + " name:" +tmpVm.getStrVmName());
		}
		return strIpList;
	}

}
