package lab.util
{
	public class ErrorFunc
	{
		import mx.controls.Alert;
		public function ErrorFunc()
		{
		}
		
		public static function dealErrorMessage(errorMessage:XMLList, from:String=null):void
		{
			trace("Error from " + from + "\n");
			trace(errorMessage);
		}
		
		public static function alertErrorMessage(errorMessage:String):void
		{
			Alert.show(errorMessage);
		}
	}
}