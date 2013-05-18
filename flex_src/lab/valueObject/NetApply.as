package lab.valueObject
{
	//与远程Java类进行绑定。
	[RemoteClass(alias="com.cloud.entities.NetApply")]
	[Bindable]
	public class NetApply
	{
		public function NetApply()
		{
		}
		public var strVmId:String;
		public var strVmName:String;
		public var strJobName:String;
		public var strZoneName:String;
		public var strUsrName:String;
		public var strVmIp:String;
		public var iVmNetStatus:int;
	}
}