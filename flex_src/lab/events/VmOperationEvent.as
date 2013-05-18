package lab.events
{
	import com.adobe.cairngorm.control.CairngormEvent;
	
	import flash.events.Event;
	
	import lab.valueObject.VM;

	public class VmOperationEvent extends CairngormEvent
	{
		public static const EVENT_ID:String = "VmOperation";
		public var _name:String;
		public var _param:Object;
		
		public function VmOperationEvent(name:String, param:Object)
		{
			super(EVENT_ID);
			this._name = name;
			this._param = param;
		}
		
		override public function clone():Event
		{
			return new VmOperationEvent(_name, _param);
		} 
		
	}
}