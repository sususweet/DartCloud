package lab.commands
{
	import com.adobe.cairngorm.business.ServiceLocator;
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.commands.SequenceCommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.system.Security;
	
	import lab.business.delegates.LoadConfigureDelegate;
	import lab.events.LoadConfReadyEvent;
	import lab.model.DCModelLocator;
	import lab.util.ErrorFunc;
	
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;

	public class LoadConfigureCommand implements ICommand,IResponder
	{
		private var modelLocator:DCModelLocator = DCModelLocator.getInstance();	
		private var serviceLocator:ServiceLocator = ServiceLocator.getInstance();
		
		public function execute(event:CairngormEvent):void
		{			
			var delegate:LoadConfigureDelegate = new LoadConfigureDelegate(this);
			delegate.loadConfigure();
		}
		
		public function result(data:Object):void
		{
			if (data.result != null)
			{
				if (data.result is XML)
				{
					var result:XML = data.result as XML;
					var errorMessage:XMLList = result.child("errorMessage");
					if (errorMessage.toString() != "")
					{
						ErrorFunc.dealErrorMessage(errorMessage, "LoadConfigureCommand");
						return;
					}
					
					for each(var item:XML in result.children())
					{
						var type:String = item.name().localName;
						if (type == "HTTPService")
						{
//							var httpService:HTTPService = serviceLocator.getHTTPService(item.attribute("id").toString());
//							httpService.url = item.attribute("url");
						}
						else if (type == "WebService")
						{
							/* TODO */
						}
						else if (type == "URL")
						{
							modelLocator[item.attribute("id").toString()] = item.attribute("url");
						}
						else if (type == "Crossdomain")
						{
							modelLocator.crossdomain = item.attribute("url");
						}
					}
					
					//加载安全策略文件，解决安全沙箱问题。
					Security.loadPolicyFile(modelLocator.crossdomain);
					
					modelLocator.dispatchEvent(new LoadConfReadyEvent());
				}
			}
		}
		
		public function fault(info:Object):void
		{
			ErrorFunc.alertErrorMessage("LoadConfigureCommand:\n" + info.fault.message);
		}
		
	}
}