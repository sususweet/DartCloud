package lab.valueObject
{
	public class VmInfo
	{
		public var iVmId:int;
		public var strVmName:String;
		public var uName:String;
		public var group:String;
		public var osVersion:String;
		public var ip:String;
		public var cpuNum:int;
		public var memSize:int;
		
		public function VmInfo(strVmInfo:String)
		{
			//获取IP
			var startIndex:int = strVmInfo.indexOf("<IP><![CDATA[") + 13;
			var endIndex:int = strVmInfo.indexOf("\]\]\>\<\/IP\>");
			ip= strVmInfo.substring(startIndex,endIndex);
			
			//获取CPU
			var startIndex:int = strVmInfo.indexOf("<CPU><![CDATA[") + 14;
			var endIndex:int = startIndex+2；
			ip= strVmInfo.substring(startIndex,endIndex);
			
			//获取内存
			var startIndex:int = strVmInfo.indexOf("<IP><![CDATA[") + 13;
			var endIndex:int = strVmInfo.indexOf("\]\]\>\<\/IP\>");
			ip= strVmInfo.substring(startIndex,endIndex);
			
			
			
		}
	}
}