package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import mx.rpc.IResponder;
	
	public class LoadJobInfoDelegate
	{
		private var service:Object;
		private var response:IResponder;
		
		public function LoadJobInfoDelegate(response:IResponder)
		{
			//此处的JobServiceRO要和Service.mxml中定义的RemoteObject的ID要相同。
			this.service = ServiceLocator.getInstance().getRemoteObject("JobServiceRO");
			this.response = response;
		}
		
		public function callServer(iUserId:int):void
		{
			var call:Object = service.getUserJobs(iUserId);//调用Java方法
			call.addResponder(response);
		}
		
	}
	
	
}