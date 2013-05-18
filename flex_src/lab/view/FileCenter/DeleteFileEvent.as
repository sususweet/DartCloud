/*
 * 移除文件事件，冒泡给父组件处理,易扩展功能，可以处理关于文件的操作事件，但是为了保持代码正确，该类名称决定不做修改，依然叫做DeleteFileEvent
 * */
package lab.view.FileCenter
{
	import flash.events.Event;
	
	public class DeleteFileEvent extends Event
	{
		public var _deleteFile:Object;
		
		public function DeleteFileEvent(type:String, deleteFile:Object = null)
		{
			super(type, true, true);
			this._deleteFile = deleteFile;
		}
	}
}