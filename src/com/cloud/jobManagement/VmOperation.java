package com.cloud.jobManagement;

import org.opennebula.client.*;
import org.opennebula.client.vm.*;
import org.opennebula.client.OneResponse;
import com.cloud.dao.VmService;
import com.cloud.dao.ZoneService;
import com.cloud.utilFun.LogWriter;

/*
 * 虚拟机的操作类
 * @author  Bruce Yan
 * @version 1.0, 2012-3-14
 */

public class VmOperation {
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public VmOperation(){
		
	}
	/**
	 * 得到虚拟机strVmId的Client信息
	 * @param strVmId
	 * @return
	 * @throws Exception
	 */
	public static Client getClientByVmId(String strVmId) throws Exception{
		String zoneAndVmId[] = strVmId.split("-");
		int zoneId = Integer.parseInt(zoneAndVmId[0]);
		Client c = getClientByZoneId(zoneId);
		return c;
	}
	
	
	public static Client getClientByZoneId(int iZoneId) throws Exception{
		String zoneHost = ZoneService.getZonehostIp(iZoneId, ZoneService.ZONE_HOST);
		String zonSecret = ZoneService.getZoneSecret(iZoneId);
		String zoneEndpoint = "http://" + zoneHost + ":2633/RPC2";
		//logWriter.log("#############打印zone信息##################");
		//logWriter.log(zonSecret+"  " + zoneEndpoint);
		Client c = new Client(zonSecret, zoneEndpoint);
		return c;
	}
	
	/* 
	 * 获取strVmId号虚拟机
	 * @param strVmId 虚拟机ID号
	 * @return 虚拟机对象
	 */
	public static VirtualMachine getVmById(String strVmId) throws Exception {
		String zoneAndVmId[] = strVmId.split("-");
		int iZoneId = Integer.parseInt(zoneAndVmId[0]);
		int iVmId = Integer.parseInt(zoneAndVmId[1]);
		Client client = getClientByZoneId(iZoneId);
		VirtualMachinePool vmPool = new VirtualMachinePool(client);
		vmPool.info(Pool.ALL, iVmId, iVmId, Pool.ALL);
		return vmPool.getById(iVmId);
	}
	
	/* 
	 * 获取所有虚拟机
	 * @param oneClient 虚拟机ID号
	 * @return 虚拟机列表
	 */
//	public Iterator<VirtualMachine> getAllVmList(){
//		VirtualMachinePool vmPool = new VirtualMachinePool(oneClient);
//		vmPool.infoAll();
//		return vmPool.iterator();
//	}
	
	/* 
	 * 获取当前用户的所有虚拟机
	 * @param oneClient 虚拟机ID号
	 * @return 虚拟机列表
	 */
//	public Iterator<VirtualMachine> getMyVmList(){
//		VirtualMachinePool vmPool = new VirtualMachinePool(oneClient);
//		vmPool.infoMine();
//		return vmPool.iterator();
//	}
	
//	public Iterator<VirtualMachine> getVmList(int uid){
//		vmPool.info(uid);
//		return vmPool.iterator();
//	}
//	
	/* 
	 * 创建虚拟机
	 * @param template 虚拟机的模板
	 * @return 虚拟机对象
	 */
	public static VirtualMachine create(Client oneClient, String template) throws Exception{
		//*****************调试*************************//\
		logWriter.log("Trying to create a VM...");
		try {
			logWriter.log("oneClient: " + oneClient);
			OneResponse response = VirtualMachine.allocate(oneClient, template);
			logWriter.log("response: " + response.getErrorMessage());
			
			int newVMID = Integer.parseInt(response.getMessage());
			logWriter.log(response.getErrorMessage());
			logWriter.log("new vm ID = :" + newVMID);
			
			if(response.isError()){
				logWriter.log("VirtualMachine.allocate's exception:" + response.getMessage());
			}
			
			int vmId = Integer.parseInt(response.getMessage());
			VirtualMachinePool vmPool = new VirtualMachinePool(oneClient);
			logWriter.log("vmPool = :" + vmPool);
			//在调用vmPool.getById之前一定要先调用此函数.
			vmPool.info(Pool.ALL, vmId, vmId, Pool.ALL);
			return vmPool.getById(vmId);
			
		} catch (Exception e) {
			logWriter.log("新建虚拟机发生错误！" + e.getMessage());
			return null;
		}
	}
	
	/*
	 * 以虚拟机模板id的形式创建虚拟机。
	 * @param iTemplateId 模板id
	 * @return 虚拟机对象
	 */
//	public VirtualMachine createByTemplateId(int iTemplateId) throws Exception{
//		OneResponse response;
//		response = VirtualMachine.allocateFromTemplate(oneClient, iTemplateId);
//		int newVMID = Integer.parseInt(response.getMessage());
//		logWriter.log("new vm ID = :" + newVMID);
//		
//		if(response.isError()){
//			logWriter.log("新建虚拟机发生错误，请查看是否有对应的模板文件id！");
//			throw new Exception(response.getMessage());
//		}
//		else{
//			int vmId = Integer.parseInt(response.getMessage());
//			
//			logWriter.log("vmPool = :" + vmPool);
//			
//			//在调用vmPool.getById之前一定要先调用此函数.
//			vmPool.info(Pool.ALL, vmId, vmId, Pool.ALL);
//			return vmPool.getById(vmId);
//		}
//	}

	/* 
	 * 重新提交strVmId号虚拟机，虚拟机状态可以是除了SUSPENDED和DONE和任意状态
	 * @param strVmId 虚拟机ID号
	 */
	public static String resubmit(String strVmId) throws Exception{
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		String strRes = "";
		
		if(vm == null){
			strRes = "虚拟机" + strVmId + "不存在，请核实后再操作!";
			logWriter.log(strRes);
			return strRes;
		}		
		response = vm.resubmit();
		if(response.isError()){
			strRes = "对虚拟机" + strVmId + "的重新提交操作失败！不能对SUSPENDED和DONE状态的虚拟机进行重新提交操作，虚拟机目前的状态为："+vm.status();
			logWriter.log(strRes);
			throw new Exception(response.getErrorMessage());
		}
		else{
			strRes = "对虚拟机" + strVmId + "重新提交成功。";
			logWriter.log(strRes);
		}
		return strRes;
	}
	
	/* ？？？？？？？？？？？添加判断Host是否存在
	 * 将strVmId号虚拟机部署到iHostId主机，虚拟机状态必须是PENDING
	 * @param strVmId 虚拟机ID号
	 * @param iHostId 主机ID号
	 */
	public static String deploy(String strVmId, int iHostId) throws Exception{
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		String strRes = "";
		
		if(vm == null){
			strRes = "虚拟机" + strVmId + "不存在，请核实后再操作!";
			logWriter.log(strRes);
			return strRes;
		}		
		response = vm.deploy(iHostId);
		if(response.isError()){
			strRes = "对虚拟机" + strVmId + "的部署操作失败！只能对PENDING状态的虚拟机进行部署操作，虚拟机目前的状态为："+vm.status();
			logWriter.log(strRes);
			throw new Exception(response.getErrorMessage());
		}
		else{
			strRes = "对虚拟机" + strVmId + "部署成功。";
			logWriter.log(strRes);
		}
		return strRes;
	}
	
		
	
	
	/* ?????????????????????加一个Host是否存在的判断
	 * 将strVmId号虚拟机迁移到iHostId主机，虚拟机状态必须是RUNNING
	 * @param strVmId 虚拟机ID号
	 * @param iHostId 主机ID号
	 */
	public static String migrate(String strVmId, int iHostId) throws Exception{
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		String strRes= "";
		
		if(vm == null){
			strRes = "虚拟机" + strVmId + "不存在，请核实后再操作!";
			logWriter.log(strRes);
			return strRes;
		}		
		response = vm.migrate(iHostId);
		if(response.isError()){
			strRes = "对虚拟机" + strVmId + "的迁移操作失败！只能对RUNNING状态的虚拟机进行迁移操作，虚拟机目前的状态为："+vm.status();
			logWriter.log(strRes);
			throw new Exception(response.getErrorMessage());
		}
		else{
			strRes = "对虚拟机" + strVmId + "的迁移操作成功。";
			logWriter.log(strRes);
		}
		return strRes;
	}
	
	/* ？？？？？？？？？？加一个Host是否存在的判断
	 * 将strVmId号虚拟机在线迁移到iHostId主机，虚拟机状态必须是RUNNING
	 * @param strVmId 虚拟机ID号
	 * @param iHostId 主机ID号
	 */
	public static void liveMigrate(String strVmId, int iHostId) throws Exception{
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		if(vm == null){
			logWriter.log("虚拟机" + strVmId + "不存在，请核实后再操作!");
			return;
		}		
		response = vm.liveMigrate(iHostId);
		if(response.isError()){
			logWriter.log("对虚拟机" + strVmId + "的在线迁移操作失败！只能对RUNNING状态的虚拟机进行在线迁移操作，虚拟机目前的状态为："+vm.status());
			throw new Exception(response.getErrorMessage());
		}
		else{
			logWriter.log("对虚拟机" + strVmId + "的在线迁移操作成功。");
		}
	}
	
	
	
	/* 
	 * 一次性删除多个虚拟机。
	 * @param vmIdArr 待删除的虚拟机ID数组
	 */
	public static boolean deleteVms(String vmIdArr[]) throws Exception{
		if(null == vmIdArr)
		{
			logWriter.log("删除虚拟机出现异常： vmIdArr is null!");
			return false;
		}
		boolean ret = true;
		for(int i=0; i<vmIdArr.length; i++){
			if(!deleteOneVm(vmIdArr[i]))
				ret = false;
		}
		return ret;
	}
	
	/* 
	 * 删除strVmId号虚拟机，虚拟机可以是任意状态
	 * @param strVmId 虚拟机ID号
	 */
	public static boolean deleteOneVm_inside(String strVmId) throws Exception {
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		//将虚拟机strVmId的镜像回收。
		//imageMap.freeImage(strVmId);
		int imageId = VmService.getImageId(strVmId);
		logWriter.log("//***************调试-打印获取镜像的的方法************************//");
		logWriter.log("VmService.getImageId("+ strVmId +");");
		logWriter.log("//***************调试-打印镜像************************//");
		logWriter.log(Integer.toString(imageId));
		
		
		if(vm == null){
			logWriter.log("虚拟机" + strVmId + "不存在，请核实后再操作!");
			//若虚拟机不存在而数据库中有相应的信息，则直接将其删除，并返回true.
			if(VmService.getVmById(strVmId) != null)
				VmService.deleteVm(strVmId); //add by frank yan.
			return true;
		}
		
		response = vm.finalizeVM();
		if(response.isError()){
			logWriter.log("对虚拟机" + strVmId + "的删除操作失败！:" + response.getErrorMessage());
//			throw new Exception(response.getErrorMessage());
			return false;
		}
		else{
			//删除镜像
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logWriter.log(e);
			}
			Client client = getClientByVmId(strVmId);
			ImageOperation imgOperation = new ImageOperation(client);
			logWriter.log("//***************调试-删除镜像************************//");
			logWriter.log("imgOperation.deleteImage("+imageId+");");
			imgOperation.deleteImage(imageId);
			logWriter.log("在openNebula中对虚拟机" + strVmId + "的删除操作成功。");
			VmService.deleteVm(strVmId); //add by frank yan.
			return true;
		}
	}
	
	public static OneResponse deleteVm(VirtualMachine vm, String strVmId)
	{
		OneResponse response;
		if(vm == null){
			logWriter.log("虚拟机" + strVmId + "不存在，请核实后再操作!");
			//若虚拟机不存在而数据库中有相应的信息，则直接将其删除，并返回true.
			if(VmService.getVmById(strVmId) != null)
				VmService.deleteVm(strVmId); //add by frank yan.
			return null;
		}		
		response = vm.finalizeVM();
		return response;
	}
	
	/**
	 * 虚拟机的操作类，供前台调用。由于系统有好几个数据中心，而每个数据中心都有一个
	 * openNebula进行管理，对应着不同的secret和endpoint信息。所以要先通过数据库查找到这些
	 * 信息，才能对不同域(zone)的云资源进行管理。
	 * @param action
	 * @param strVmId
	 * @return
	 * @throws Exception
	 */
	public static boolean vm_action(String action, String strVmId) throws Exception{
		OneResponse rs = vmAction(action, strVmId);
		
		if(rs.isError()){
			return false;
		}
		return true;
	}
	
	/**
	 * 删除虚拟机，供前端调用。和vm_action方法类似，也要先获取域的相关信息。
	 * @param strVmId
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteOneVm(String strVmId) throws Exception{
		return deleteOneVm_inside(strVmId);
	}

	/*
	 * 对虚拟机进行操作.
	 * @param action The action name to be performed, can be:<br/>
     * "shutdown", "reboot", "hold", "release", "stop", "cancel", "suspend",
     * "resume", "restart", "finalize".
     * 
     * @return If an error occurs the error message contains the reason.
     *  @param strVmId 虚拟机ID号
	 */
	public static OneResponse vmAction(String action, String strVmId) 
	{
		try 
		{
			OneResponse response = null;
			//oneClient = getClientByVmId(strVmId);
			VirtualMachine vm = getVmById(strVmId);
			logWriter.log("Trying to " + action + " the vm " + strVmId + ", the vm's current status is " + vm.status());
			
			if(vm == null){
				logWriter.log("虚拟机" + strVmId + "不存在，请核实后再操作!");
				return null;
			}	
			if(action.equals("poweroff"))
			{
				response = vm.poweroff();
			}
			if(action.equals("shutdown"))
			{
				response = vm.shutdown();
			}
			else if(action.equals("reboot"))
			{
				//为什么会没有reboot方法？
//				response = vm.reboot();
			}
			else if(action.equals("hold"))
			{
				response = vm.hold();
			}
			else if(action.equals("release"))
			{
				response = vm.release();
			}
			else if(action.equals("stop"))
			{
				response = vm.stop();
			}
			else if(action.equals("cancel"))
			{
				response = vm.cancel();
			}
			else if(action.equals("suspend"))
			{
				response = vm.suspend();
			}
			else if(action.equals("resume"))
			{
				response = vm.resume();
			}
			else if(action.equals("restart"))
			{
				response = vm.restart();
			}
			else if(action.equals("finalize"))
			{
				vm.finalizeVM();
			}
			else if(action.equals("resubmit"))
			{
				vm.resubmit();
				
				//应该还有更好的方法，若启动不起来，则直接将其depoy到hostId为40的主机上。
				vm.deploy(40);
			}
			else if(action.equals("delete"))
			{
				deleteVm(vm, strVmId);
			}
			else
			{
				logWriter.log("the parameter action is not correct in function vm_action");
			}
			
			if(response.isError()){
				logWriter.log("对虚拟机" + strVmId + "的" + action + "操作失败！" + response.getErrorMessage());
			}
			else{
				logWriter.log("对虚拟机" + strVmId + "的" + action + "操作成功。");
			}
			
			return response;
			
		} catch (Exception e) {
			logWriter.log("虚拟机操作出现异常： " + e);
			return null;
		}
		
	}
	
	/* 
	 * 获取虚拟机的状态
	 * @param strVmId 虚拟机ID号
	 */
	public static String getVmStatus(String strVmId) throws Exception{
		VirtualMachine vm = getVmById(strVmId);
		
		return vm.status();
	}
	
	
	/**
	 * 在虚拟机关机之后，重新开启该虚拟机。
	 * @param strVmId 虚拟机id
	 * @return
	 */
	public boolean deployVm(String strVmId)
	{
		//前台不断地调用此类，会造成大量的日志信息！想办法修改下。
		try 
		{
			VirtualMachine vm = getVmById(strVmId);
			String strServerId = parseRpcStr(vm.info().getMessage(),"<HID>","</HID>");
			if(strServerId.equals(""))//如果没有找到虚拟机所属的物理机Id，那么就将strServerId设置为"0"
			{
				strServerId = "0";
//				logWriter.log("strVmId: " + strVmId + ", strServerId: " + strServerId);
			}
			int iServerId = Integer.parseInt(strServerId);
			vm.deploy(iServerId);
			
		} catch (Exception e) {
//			logWriter.log("Deploy vm error: " + e);
			return false;
		}
//		logWriter.log("deploy vm" + strVmId + " successfully!");
		return true;
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
		String strRet = ""; 
		int iLen = startStr.length();
		int beginIndex = rpcString.indexOf(startStr);
		int endIndex = rpcString.indexOf(endStr);
		if(beginIndex==-1 || endIndex==-1)
		{
//			logWriter.log("cann't find the String beweent" + startStr + " and " + endStr);
//			logWriter.log("The RPC file is :");
//			logWriter.log(rpcString);
			return strRet;
		}
		beginIndex += iLen;
		try 
		{
			strRet = rpcString.substring(beginIndex, endIndex);
		} catch (Exception e) {
//			logWriter.log("VmOperation.parseRpcStr:" + e);
			
		}
		return strRet;
	}
	
	/**
	 * 返回ID号为strVmId的虚拟机的Xml描述信息。
	 * @param strVmId
	 * @return
	 * @throws Exception 
	 */
	public static String getVmInfoXml(String strVmId) throws Exception
	{
		OneResponse response;
		VirtualMachine vm = getVmById(strVmId);
		response = vm.info();
		return response.getMessage();
	}
	
	/**
	 * 解析虚拟机或者主机的IP 
	 * @param id OpenNebula中的id号
	 * @return
	 */
	public static String vmId2IP(String strVmId) throws Exception{
		String ip = null;
		String zoneAndVmId[] = strVmId.split("-");
		int iZoneId = Integer.parseInt(zoneAndVmId[0]);
		int iVmId = Integer.parseInt(zoneAndVmId[1]);
		
		Client oneClient = getClientByZoneId(iZoneId);
		
		String startStr = "<IP><![CDATA[";
		String endStr = "]]></IP>";
		
		int iLen = startStr.length();
		
		try{
			OneResponse rc = VirtualMachine.info(oneClient, iVmId);
			String arr = rc.getMessage();
			if(arr != null)//能够获取到虚拟机信息
			{
				int start = arr.indexOf(startStr);
				int stop = arr.indexOf(endStr);
				ip = arr.substring(start+iLen, stop);
			}
			else//不能获取到虚拟机信息
			{
				logWriter.log("can not get the one-" + strVmId + "'s information!");
			}
		} catch(Exception e){
			logWriter.log(e);
		}
		return ip;
	}
	
	
	public static void main(String[] args) throws ClientConfigurationException{
	}
	
}
