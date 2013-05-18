
package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	import flash.events.Event;
	
	import lab.valueObject.Job;
	
	public class CreateOneJobEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "CreateOneJob";
		public var _job:Job;
		
		public function CreateOneJobEvent(job:Job)
		{
			super(EVENT_ID);
			this._job = job;
		}
		
		override public function clone():Event
		{
			return new CreateOneJobEvent(_job);
		} 
		
	}
}