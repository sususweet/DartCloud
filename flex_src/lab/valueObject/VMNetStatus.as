package lab.valueObject
{
	public class VMNetStatus
	{
		public static const LOCAL:int = 0; //只能访问局域网
		public static const PUBLIC:int = 1; //可访问外网
		public static const lOCAL2PUBLIC:int = 2; //申请开通外网访问
		public static const PUBLIC2LOCAL:int = 3; //申请关闭外网访问
		public function VMNetStatus()
		{
		}
		
		public static function getChineseNetStatus(iNetStatus:int):String
		{
			var  strRet:String = "";
			if(iNetStatus == LOCAL)
				strRet = "只能访问局域网";
			else if(iNetStatus == PUBLIC)
				strRet = "可访问外网";
			else
				strRet = "管理员审批中";
			return strRet;
		}
		
	}
}