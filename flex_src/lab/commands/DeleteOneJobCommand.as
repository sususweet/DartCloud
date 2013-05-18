package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import lab.business.delegates.DeleteOneJobDelegate;
	import lab.events.DeleteOneJobEvent;
	import lab.events.LoadJobInfoEvent;
	import lab.model.DCModelLocator;
	import lab.valueObject.Job;
	
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	
	public class DeleteOneJobCommand implements ICommand, IResponder 
	{
		private var dcModelLocator:DCModelLocator = DCModelLocator.getInstance();
		
		public function DeleteOneJobCommand() {}
		
		public function execute(event:CairngormEvent):void
		{
			var job:Job = (event as DeleteOneJobEvent)._job;
			var userDelegate:DeleteOneJobDelegate = new DeleteOneJobDelegate(this);
			userDelegate.callServer(job);
		}
		
		public function result(data:Object):void
		{
			var ret:Boolean = (data as ResultEvent).result as  Boolean;
			
			if(true == ret)
			{
				Alert.show("业务删除成功！",'提示');
				
				var loadJobInfoEvent:LoadJobInfoEvent = new LoadJobInfoEvent(dcModelLocator.m_user.iUserId);
				loadJobInfoEvent.dispatch();
			}
			else
			{
				Alert.show("删除业务失败！",'提示');
			}
		}
		
		public function fault(info:Object):void
		{
			trace("fault in DelteOneJobCommand!");
		}
		
		
	}
}