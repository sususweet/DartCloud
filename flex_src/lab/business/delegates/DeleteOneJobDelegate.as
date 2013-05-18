package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import lab.valueObject.Job;
	
	import mx.rpc.IResponder;

	public class DeleteOneJobDelegate
	{
		private var service:Object;
		private var response:IResponder;
		
		public function DeleteOneJobDelegate(response:IResponder)
		{
			this.service = ServiceLocator.getInstance().getRemoteObject("deleteJobRo");
			this.response = response;
		}
		
		public function callServer(job:Job):void
		{
			var call:Object = service.deleteOneJob(job.iJobId);//调用Java方法
			call.addResponder(response);
		}
	}
}