package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;
	
	import lab.valueObject.VmActions;
	
	import mx.rpc.IResponder;

	public class VmOperationDelegate
	{
		private var service:Object;
		private var response:IResponder;
		
		public function VmOperationDelegate(response:IResponder)
		{
			this.service = ServiceLocator.getInstance().getRemoteObject("VmOperationRO");
			this.response = response;
		}
		
		public function vmOperation(opName:String, param:Object):void
		{
			//param一般传入为虚拟机的iVmid.
			var call:Object = new Object();
			call = service.vm_action(opName, param);
			call.addResponder(response);
			/*
			switch(opName)
			{
				case VmActions.START_UP:
					call = service.vm_action(VmActions.START_UP, param);
					break;
				case VmActions.SHUTDOWN:
					call = service.vm_action(VmActions.SHUTDOWN, param);
					break;
				case VmActions.SUSPEND:
					call = service.vm_action(VmActions.SUSPEND, param);
					break;
				case VmActions.RESUME:
					call = service.vm_action(VmActions.RESUME, param);
					break;
				case VmActions.DELETE:
					call = service.deleteOneVm(param);
					break;
				case VmActions.VNC_CONNECT:
					call = service.VNCConnect(param);
				default:
			}
			*/
		}
	}
}