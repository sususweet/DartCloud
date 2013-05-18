package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import lab.business.delegates.DeleteOneVmDelegate;
	import lab.events.DeleteOneVmEvent;
	import lab.events.LoadUserInfoEvent;
	import lab.model.DCModelLocator;
	
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	
	public class DeleteOneVmCommand implements ICommand, IResponder 
	{
		private var dcModelLocator:DCModelLocator = DCModelLocator.getInstance();
		public function DeleteOneVmCommand() {}
		
		public function execute(event:CairngormEvent):void
		{
			var strVmId:String = (event as DeleteOneVmEvent)._strVmId;
			var userDelegate:DeleteOneVmDelegate = new DeleteOneVmDelegate(this);
			userDelegate.callServer(strVmId);
		}
		
		public function result(data:Object):void
		{
			var ret:Boolean = (data as ResultEvent).result as  Boolean;
			
			if(true == ret)
			{
				//Alert.show("虚拟机删除成功！",'提示');
				//删除成功后，更新用户信息。
				//可能是以下这段代码，导致了删除的时候，虚拟机的状态发生变化。
//				var loadUserInfoEvent:LoadUserInfoEvent = new LoadUserInfoEvent(dcModelLocator.user);
//				loadUserInfoEvent.dispatch();
			}
			else
			{
				Alert.show("虚拟机删除失败！",'提示');
			}
		}
		
		public function fault(info:Object):void
		{
			trace("fault in DelteOneJobCommand!");
		}
		
		
	}
}