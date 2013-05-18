package lab.valueObject
{
	import mx.collections.ArrayCollection;
	//import mx.rpc.remoting.RemoteObject;
	[RemoteClass(alias="com.cloud.entities.Job")]
	
	[Bindable]
	public class Job
	{
		public var iJobId:int;
		public var iCpuNum:int;
		public var iMemSize:int;
		public var iStorageSize:int;
		public var iVmNum:int;
		public var iUserId:int;
		public var iJobStatus:int;
		public var iApplyHour:int;
		public var iOSType:int;
		public var iZoneId:int;
		public var strJobName:String;
		public var strJobDescription:String;
		public var strZoneName:String;
		public var strZoneHost:String;
		public var strZoneLAN_Ip:String;
		public var strZoneSchoolIp:String;
		public var strZonePublicIp:String;
		public var strVmPass:String;
		public var strVmPassConfirm:String;
		public var dateJobStartTime:Date;
		public var vmArrayInfo:ArrayCollection;
		public var iNetStatus:int; //0表示只能上内网；1表示可以上外网；2表示内网转为外网，3表示外网转为内网。
		public function Job(){
			iJobId = 0; //默认的ID为0，代表无效的业务。
			iNetStatus = 0; //默认情况下只能上内网。
		}
	}
}
