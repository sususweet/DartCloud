package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import com.adobe.cairngorm.model.ModelLocator;
	
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import lab.business.delegates.LoadUserInfoDelegate;
	import lab.events.LoadUserInfoEvent;
	import lab.model.DCModelLocator;
	import lab.util.UtilFunc;
	import lab.valueObject.Job;
	import lab.valueObject.User;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	

	public class LoadUserInfoCommand implements ICommand, IResponder
	{
		private var modelLocator:DCModelLocator = DCModelLocator.getInstance();
		
		public function LoadUserInfoCommand(){}
		
		public function execute(event:CairngormEvent):void
		{
			var iUserId:int = (event as LoadUserInfoEvent).iUserId;
			var userDelegate:LoadUserInfoDelegate = new LoadUserInfoDelegate(this);
			userDelegate.callServer(iUserId);
		}
		public function result(data:Object):void
		{
			modelLocator.m_user = (data as ResultEvent).result as User;
			trace("加载用户信息成功！");
			
			
//			var strRes:String = "";
//			var user:User = new User();
//			user = (data as ResultEvent).result as User;
//			strRes = "strZjuSsoUid:" + user.strUserZjuSsoUid + ", Email:" + user.strUserEmail;
//			Alert.show("Result: " + strRes);
		}
		
		public function fault(data:Object):void
		{
//			var strCurWebServer:String = modelLocator.webServerIp;
//			if(strCurWebServer == "localhost")
//				strCurWebServer = strCurWebServer + ":8080";
			
			var request:URLRequest = new URLRequest("http://" + modelLocator.strDomainName +"/DartCloud/DartCloud.html" );  
			navigateToURL(request,"_self");
			trace("fault at LoadUserInfoCommand:加载用户信息失败 !" );
		}
		
	}
}