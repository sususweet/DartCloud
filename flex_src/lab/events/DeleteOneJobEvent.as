package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	import flash.events.Event;
	
	import lab.valueObject.Job;
	
	public class DeleteOneJobEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "DeleteOneJob";
		public var _job:Job;
		
		public function DeleteOneJobEvent(job:Job)
		{
			super(EVENT_ID);
			this._job = job;
		}
		
		override public function clone():Event
		{
			return new DeleteOneJobEvent(_job);
		} 
		
	}
}