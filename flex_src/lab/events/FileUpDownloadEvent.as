package lab.events
{
	import flash.events.Event;
	
	public class FileUpDownloadEvent extends Event
	{
		public static const ERROR:String = "FILE_UPLOAD_ERROR";
		public static const COMPLETED:String = "FILE_UPLOAD_COMPLETED";
		public static const CANCEL:String = "FILE_UPLOAD_CANCEL";
		private var _message:String;
		private var _data:Object;
		private var _name:String;
		
		public function FileUpDownloadEvent(type:String, name:String, message:String = null)
		{
			super(type, true);
			this._name = name;
			this._message = message;
		}
		
		public function get message():String{
			return _message;
		}
		
		public function set message(value:String):void{
			_message = value;
		}
	}
}