package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import lab.valueObject.Job;
	
	import mx.rpc.IResponder;
	
	public class CreateOneJobDelegate
	{
		private var service:Object;
		private var response:IResponder;
		
		public function CreateOneJobDelegate(response:IResponder)
		{
			this.service = ServiceLocator.getInstance().getRemoteObject("JobOperationRO");
			this.response = response;
		}
		
		public function callServer(job:Job):void
		{
			var call:Object = service.createOneJob(job);//调用Java方法
			call.addResponder(response);
		}
		
	}
}