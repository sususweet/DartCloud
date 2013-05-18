package lab.model
{
	import com.adobe.cairngorm.model.IModelLocator;
	
	import flash.events.EventDispatcher;
	
	import lab.valueObject.Job;
	import lab.valueObject.NetType;
	import lab.valueObject.User;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	
	
	[Bindable]
	public class DCModelLocator extends EventDispatcher implements IModelLocator
	{
		private static var _instance:DCModelLocator;
		
		public function DCModelLocator()
		{
		}
		
		public static function getInstance():DCModelLocator
		{
			if (_instance == null)
			{
				_instance = new DCModelLocator();
			}
			return _instance;
		}
		
		public var m_user:User = new User();
		
		//初始化的时候最好要new一个对象，否则会报null错误。
		//当前用户的业务信息
		public var userJobInfoAC:ArrayCollection = new ArrayCollection();
		
		//当前用户的业务信息，XMLList格式，目前只包含业务名称。
		public var JobInfoXMLList:XMLListCollection = new XMLListCollection();
		
		//某一业务下的所有虚拟机信息，用于前台显示。
		//当用户在左边树形目录点击某一具体业务时，需要显示该业务下的所有虚拟机信息。
		public var selectedVmInfoAC:ArrayCollection = new ArrayCollection();
		
		//用于解决Adobe安全沙箱问题的策略文件。加了此策略文件之后，程序可以加载不在同一域内的文件。
		public var crossdomain:String;
		public var curUsername:String = new String();
		
//		public var vncUrl:String = "http://dartcloud.zju.edu.cn:5801/192.168.0.154:5801/";
//		public var websshUrl:String = "http://dartcloud.zju.edu.cn:5801/192.168.0.154:5801/";
		//当选择了网络环境后虽然修改了这个变量，但是那个对象实体很快消失，导致下载文件时new了一个新的实例，所以IP地址依然是外网地址
		public var webServerIp:String = "dartcloud.zju.edu.cn";
		
//		public var webServerIp:String = "localhost:8080";
//		public static var webIp:String = "dartcloud.zju.edu.cn";//用static声明，保证其唯一性，且不需要实例化即可得到
//		public var proxyServerIp:String = "dartcloud.zju.edu.cn";
		
		public const strDomainName:String = "dartcloud.zju.edu.cn"; //当前系统的域名
		
		public var curNetType:int = NetType.PUBLIC_IP; //用户选择的网络环境。
		
		public var curSelectedJob:Job = null;//当前选中的业务。
		
	}
}

