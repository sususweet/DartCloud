package lab.util
{
	public class MenuImageClass
	{
		public function MenuImageClass()
		{
		}
		
		[Embed(source="images/icon/out.png")]
		[Bindable]
		public static var tuichu:Class; 
		
		
		[Embed(source="images/icon/shangyi.png")]
		[Bindable]
		public static var shangyi:Class; 
		
		
		[Embed(source="images/icon/baocun.png")]
		[Bindable]
		public static var baocun:Class; 
		
		[Embed(source="images/icon/file.png")]
		[Bindable]
		public static var file:Class; 
	}
}