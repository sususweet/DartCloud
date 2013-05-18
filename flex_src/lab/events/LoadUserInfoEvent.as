package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	public class LoadUserInfoEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "LoadUserInfo";
		public var iUserId:int;
		
		public function LoadUserInfoEvent(iUserId:int)
		{
			super(EVENT_ID);
			this.iUserId = iUserId;
		}
		
		override public function clone():Event
		{
			return new LoadUserInfoEvent(iUserId);
		} 
	}
}