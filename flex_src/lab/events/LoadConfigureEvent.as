package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	public class LoadConfigureEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "LoadConfigure";
		public function LoadConfigureEvent()
		{
			super(EVENT_ID);
		}
		
		override public function clone():Event
		{
			return new LoadConfigureEvent();
		}
		
	}
	
	
}