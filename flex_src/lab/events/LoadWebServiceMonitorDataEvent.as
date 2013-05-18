package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	public class LoadWebServiceMonitorDataEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "LoadWebServiceData";
		public function LoadWebServiceMonitorDataEvent()
		{
			super(EVENT_ID);
		}
		
		override public function clone():Event
		{
			return new LoadWebServiceMonitorDataEvent();
		} 

	}
}