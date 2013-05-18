package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import mx.rpc.IResponder;

	public class LoadUserInfoDelegate
	{
		private var service:Object;
		private var response:IResponder;
		
		public function LoadUserInfoDelegate(response:IResponder)
		{
			//此处的JobServiceRO要和Service.mxml中定义的RemoteObject的ID要相同。
			this.service = ServiceLocator.getInstance().getRemoteObject("UserServiceRO");
			this.response = response;
		}
		
		public function callServer(iUserId:int):void
		{
			var call:Object = service.getUserInfo(iUserId);//调用Java方法
			call.addResponder(response);
		}
		
	}
	
}

