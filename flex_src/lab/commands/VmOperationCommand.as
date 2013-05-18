package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import lab.business.delegates.VmOperationDelegate;
	import lab.events.VmOperationEvent;
	
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	
	public class VmOperationCommand implements IResponder, ICommand
	{
		public function VmOperationCommand()
		{
		}
		
		public function result(data:Object):void
		{
			trace(data.result as String);
		}
		
		public function fault(info:Object):void
		{
			Alert.show("虚拟机机操作出错，你可能对虚拟机进行了非法操作！");
			trace(info.fault as String);
		}
		
		public function execute(event:CairngormEvent):void
		{
			var param:Object = (event as VmOperationEvent)._param;
			var name:String =  (event as VmOperationEvent)._name;
			var vmOperationDelegate:VmOperationDelegate = new VmOperationDelegate(this);
			vmOperationDelegate.vmOperation(name, param);
		}
		
	}
}