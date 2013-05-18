package lab.view.FileCenter
{
	import flash.net.FileReference;
	[Bindable]
	public class SingleFile
	{
		//文件句柄
		public var file:FileReference;
		
		public var fileName:String;
		public var fileSize:String;
		
		public var fileCategory:String = "其它";
		public var fileDescription:String = "";
		//public var fileType:String = "";//public_to_all, public_not_to_all;
		
		//进度
		public var progress:int;
		
		public function getFileName():void
		{
			fileName = file.name;
		}
		
		public function getFileSize():void
		{
			if(file.size >= 0 && file.size <= 1024)
			{
				fileSize = file.size.toString() + "B";
			}
			else if(file.size <= 1048576)
			{
				fileSize = new Number(file.size / 1024).toFixed(2) + "KB";
			}
			else
			{
				fileSize= new Number(file.size / 1048576).toFixed(2) + "MB";
			}
			
		}
	}
}