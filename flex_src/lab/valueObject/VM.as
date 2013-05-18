package lab.valueObject
{
	/*
	* @author  Bruce Yan
	* @version 1.0, 2012-4-9
	*/
	[RemoteClass(alias="com.cloud.entities.VM")]
	[Bindable]
	public class VM
	{
		public var strVmId:String;
		public var iVmJobId:int;
		public var iVmServerId:int;
		public var iVmCpuNum:int;
		public var iVmMemorySize:int;
		public var iVmStorageSize:int;
		public var iImageId:int;
		
		//以下几个变量名与AdvancedDataGrid中的数据标签绑定，不要随意更改！
		public var strVmName:String;
		public var vm_status:String;
		public var cpu_status:int;
		public var strJobName:String;
		public var strZoneLAN_Ip:String;
		public var strZoneSchoolIp:String;
		public var strZonePublicIp:String;
		public var strVmIp:String;
		public var iNetStatus:int; //0表示只能上内网；1表示可以上外网；2表示内网转为外网，3表示外网转为内网。
		
		public function VM()
		{
			vm_status = "正在运行";
			cpu_status = 5;
		}
	}
		
}