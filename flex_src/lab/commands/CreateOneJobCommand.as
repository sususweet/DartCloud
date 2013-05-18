package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import lab.business.delegates.CreateOneJobDelegate;
	import lab.business.delegates.DeleteOneJobDelegate;
	import lab.events.CreateOneJobEvent;
	import lab.events.LoadJobInfoEvent;
	import lab.model.DCModelLocator;
	import lab.valueObject.Job;
	
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	public class CreateOneJobCommand implements ICommand, IResponder 
	{
		private var dcModelLocator:DCModelLocator = DCModelLocator.getInstance();
		
		public function CreateOneJobCommand() {}
		
		public function execute(event:CairngormEvent):void
		{
			var job:Job = (event as CreateOneJobEvent)._job;
			var userDelegate:CreateOneJobDelegate = new CreateOneJobDelegate(this);
			userDelegate.callServer(job);
		}
		
		public function result(data:Object):void
		{
			var ret:Boolean = (data as ResultEvent).result as  Boolean;
			
			if(true == ret)
			{
				Alert.show("新建业务成功！虚拟机开启需要一定时间，请耐心等待......",'提示');
				
			}
			else
			{
				//可能部分创建成功。
				Alert.show("新建业务失败！",'提示');
			}
			
			//更新用户的业务信息。
			var loadJobInfoEvent:LoadJobInfoEvent = new LoadJobInfoEvent(dcModelLocator.m_user.iUserId);
			loadJobInfoEvent.dispatch();
		}
		
		public function fault(info:Object):void
		{
			trace("fault in DelteOneJobCommand!");
			Alert.show("新建业务失败！" + (info as FaultEvent).fault,'提示');
		}
		
		
	}
}