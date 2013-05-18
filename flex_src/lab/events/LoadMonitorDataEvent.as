package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	public class LoadMonitorDataEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "LoadMonitorData";
		public function LoadMonitorDataEvent()
		{
			super(EVENT_ID);
		}
		
		override public function clone():Event
		{
			return new LoadMonitorDataEvent();
		} 

	}
}