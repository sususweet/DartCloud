package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	
	public class DeleteOneVmEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "DeleteOneVm";
		public var _strVmId:String;
		
		public function DeleteOneVmEvent(strVmId:String)
		{
			super(EVENT_ID);
			this._strVmId = strVmId;
		}
		
		override public function clone():Event
		{
			return new DeleteOneVmEvent(_strVmId);
		} 
		
	}
}