package lab.events
{
	import flash.events.Event;
	
	public class UserLoginSuccess extends Event
	{
		public var userName:String;
		public function UserLoginSuccess(type:String, userName:String)
		{
			super(type);
			this.userName = userName;
		}
	}
}