package lab.valueObject
{
	public class VmStatus
	{
		public static const DONE:String = "done";
		public static const RUNN:String = "runn";
		public static const PEND:String = "pend";
		public static const PROL:String = "prol";
		public static const SAVE:String = "save";
		public static const BOOT:String = "boot";
		public static const SHUT:String = "shut";
		public static const POFF:String = "poff"; //关机状态
		public static const FAIL:String = "fail";
		public static const HOLD:String = "hold";
		public static const MIGR:String = "migr";
		public static const SUSP:String = "susp";
		public static const ERROR:String = "error";
		public static const CLEA:String = "clea";
		public static const UNKN:String = "unkn";
		
		public function VmStatus()
		{
		}
		
		//将openNebula返回的虚拟机状态转换为用户能够理解的中文。
		public static function getChineseVmStatus(strVmStatus:String):String
		{
			var strRet:String =strVmStatus;
			if(DONE == strVmStatus)
			{
				strRet = "已停止";
			}
			else if(RUNN == strVmStatus)
			{
				strRet = "正在运行";
			}
			else if(PEND == strVmStatus)
			{
				strRet = "就绪";
			}
			else if(PROL == strVmStatus)
			{
				strRet = "正在准备镜像";
			}
			else if(SAVE == strVmStatus)
			{
				strRet = "保留";
			}
			else if(BOOT == strVmStatus)
			{
				strRet = "正在启动";
			}
			else if(SHUT == strVmStatus)
			{
				strRet = "停机";
			}
			else if(POFF == strVmStatus)
			{
				strRet = "关机";
			}
			else if(FAIL == strVmStatus)
			{
				strRet = "虚拟机操作失败";
			}
			else if(HOLD == strVmStatus)
			{
				strRet = "控制";
			}
			else if(MIGR == strVmStatus)
			{
				strRet = "迁移";
			}
			else if(ERROR == strVmStatus)
			{
				strRet = "出错";
			}
			else if(SUSP == strVmStatus)
			{
				strRet = "挂起";
			}
			else if(UNKN == strVmStatus)
			{
				strRet = "未知状态";
			}
			return strRet;
		}
		
		
	}
}