package lab.util
{
	import com.adobe.cairngorm.validation.ValidatorMultipleListeners;
	
	import lab.model.DCModelLocator;
	import lab.valueObject.Job;
	import lab.valueObject.VM;
	import lab.valueObject.VMNetStatus;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Tree;
	import mx.events.ListEvent;
	
	public class UtilFunc
	{
		//单例，全局变量；程序运行时可能用到的一些配置信息。
		[Bindable]
		private static var dcModelLocator:DCModelLocator = DCModelLocator.getInstance();
		
		public function UtilFunc(){}
		
		
		//获取当前用户所有业务中的所有虚拟机信息。
		public static function getAllVmInfo(userJobInfo:ArrayCollection):ArrayCollection{
			var arrAllVmInfo:ArrayCollection = new ArrayCollection();
			for(var i:int=0; i<userJobInfo.length; i++){
				var tmpJob:Job = userJobInfo[i] as Job;
				arrAllVmInfo.addAll(getOneJobVmInfo(tmpJob));
			}
			return arrAllVmInfo;
		}
		
		//获得一个业务内部的虚拟机信息，返回一个数组作为主显示界面DataGrid的数据源。
		public static function getOneJobVmInfo(job:Job):ArrayCollection{
			var vmArr:ArrayCollection = job.vmArrayInfo;
			var oneVmInfo:VM = new VM();
			var retArr:ArrayCollection = new ArrayCollection();
			for(var i:int=0; i<vmArr.length; i++){
				oneVmInfo = vmArr[i] as VM;
				var strVmNetStatus:String = VMNetStatus.getChineseNetStatus(oneVmInfo.iNetStatus);
				retArr.addItem({strVmName:oneVmInfo.strVmName, vm_status:"正在运行", cpu_status:5, strJobName:job.strJobName,
					strVmId:oneVmInfo.strVmId,strZoneLAN_Ip:job.strZoneLAN_Ip,strZoneSchoolIp:job.strZoneSchoolIp,
					strZonePublicIp:job.strZonePublicIp,strVmIp:oneVmInfo.strVmIp,iImageId:oneVmInfo.iImageId
				,iVmJobId:oneVmInfo.iVmJobId,strZoneName:job.strZoneName,strVmNetStatus:strVmNetStatus,iNetStatus:oneVmInfo.iNetStatus});
				
//				//将初始化虚拟机信息的类抽象出来。这样抽象出来会有问题，缺少deSelected字段。
//				var newVmObj:VM = getOneVmInfo(oneVmInfo,job);
//				retArr.addItem(newVmObj);
				
			}
			return retArr;
		}
		
		/**
		 * 返回一个虚拟机信息的对象，用于前台列表显示，字段与AdvancedDataGrid列表的域绑定。
		 */
		public static function getOneVmInfo(oldVmInfo:VM, job:Job):VM
		{
			var newVmObj:VM = new VM();
			newVmObj = oldVmInfo;
			newVmObj.strJobName = job.strJobName;
			newVmObj.strZoneLAN_Ip = job.strZoneLAN_Ip;
			newVmObj.strZonePublicIp = job.strZonePublicIp;
			newVmObj.strZoneSchoolIp = job.strZoneSchoolIp;
			return newVmObj;
		}
		
		
		//将ArrayCollection形式的数据转换成XML格式的
		public function arrToXml(arr:ArrayCollection):XML{ 
			var root:XML = new XML("<jobs label=\"业务\"/>"); 
			for(var i:int = 0;i<arr.length;i++){ 
				var node:XML = new XML("<job label=\"" +arr[i].strJobName +"\"/>"); 
				var tmpVmArray:ArrayCollection = arr[i].vmArrayInfo;
				var iLength:int = tmpVmArray.length;
				if(iLength > 0){
					
					for(var j:int = 0; j<tmpVmArray.length; j++)
					{
						var oneVmInfo:VM = tmpVmArray[j] as VM;
						var subNode:XML = new XML("<vm label=\"" + oneVmInfo.strVmName + "\"/>");
						node.appendChild(subNode);
					}
				}
				root.appendChild(node); 
			} 
			return root; 
		} 
		
		/**
		 * 当点击不同的业务名称时，主列表上应该显示该业务下相应的虚拟机信息。
		 * 即需要更新selectedVmInfoAC数组。
		 * 
		 */
		public static function jobInfoTree_changeHandler(event:ListEvent):void
		{
			
			if( dcModelLocator.userJobInfoAC.length == 0 )
			{
				trace("无业务信息！", '提示');
				return;
			}	
			var selectedNode:XML = Tree(event.target).selectedItem as XML;
			
			var strSelectedLabel:String = selectedNode.@label;
			var selType:int = selectedNode.@type;
			var selectedIndex:int = selectedNode.@id;
			var tmpJob:Job = new Job();
			
			if(selType == 0)//选择根节点
			{
				dcModelLocator.selectedVmInfoAC = UtilFunc.getAllVmInfo(dcModelLocator.userJobInfoAC);
				dcModelLocator.curSelectedJob = null; 
			}
			else if(selType == 1)//选择业务
			{
				tmpJob = dcModelLocator.userJobInfoAC[selectedIndex] as Job;
				dcModelLocator.selectedVmInfoAC = UtilFunc.getOneJobVmInfo(tmpJob);
				dcModelLocator.curSelectedJob = tmpJob; 
			}
			else if(selType ==2)//选择虚拟机
			{
				var vmIndex:int = selectedIndex % 1000;
				var jobIndex:int = selectedIndex / 1000;
				tmpJob = dcModelLocator.userJobInfoAC[jobIndex] as Job;
				var oneVmInfo:VM = new VM();
				oneVmInfo = tmpJob.vmArrayInfo[vmIndex] as VM;
				
				var retArr:ArrayCollection = new ArrayCollection();
				var strVmNetStatus:String = VMNetStatus.getChineseNetStatus(oneVmInfo.iNetStatus);
				retArr.addItem({strVmName:oneVmInfo.strVmName, vm_status:"正在运行", cpu_status:5, strJobName:tmpJob.strJobName,
					strVmId:oneVmInfo.strVmId,strZoneLAN_Ip:tmpJob.strZoneLAN_Ip,strZoneSchoolIp:tmpJob.strZoneSchoolIp,
					strZonePublicIp:tmpJob.strZonePublicIp,strVmIp:oneVmInfo.strVmIp,iImageId:oneVmInfo.iImageId
					,iVmJobId:oneVmInfo.iVmJobId,strZoneName:tmpJob.strZoneName,strVmNetStatus:strVmNetStatus});
				dcModelLocator.selectedVmInfoAC  = retArr;
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}