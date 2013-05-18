package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import lab.model.DCModelLocator;
	import lab.valueObject.Job;
	
	import mx.rpc.IResponder;
	import mx.skins.halo.ScrollTrackSkin;
	
	public class DeleteOneVmDelegate
	{
		private var dcModelLocator:DCModelLocator = DCModelLocator.getInstance();
		private var service:Object;
		private var response:IResponder;
		
		public function DeleteOneVmDelegate(response:IResponder)
		{
			this.service = ServiceLocator.getInstance().getRemoteObject("VmOperationRO");
			this.response = response;
		}
		
		public function callServer(strVmId:String):void
		{
			var call:Object = service.deleteOneVm(strVmId);//调用Java方法
			call.addResponder(response);
		}
		
	}
}