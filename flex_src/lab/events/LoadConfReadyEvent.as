package lab.events
{
	import flash.events.Event;

	public class LoadConfReadyEvent extends Event
	{
		public static const EVENT_ID:String = "LoadConfigureReady";
		public function LoadConfReadyEvent()
		{
			super(EVENT_ID);
		}
		
		override public function clone():Event
		{
			return new LoadConfReadyEvent();
		}
	}
}