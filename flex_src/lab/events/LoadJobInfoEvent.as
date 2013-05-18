package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	import lab.valueObject.User;
	
	public class LoadJobInfoEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "LoadUserJobInfo";
		public var iUserId:int;
		
		public function LoadJobInfoEvent(iUserId:int)
		{
			super(EVENT_ID);
			this.iUserId = iUserId;
		}
		
		override public function clone():Event
		{
			return new LoadJobInfoEvent(iUserId);
		} 
	}
}