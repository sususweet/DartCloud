package lab.view.FileCenter
{
	[RemoteClass(alias="com.cloud.BusinessManagement.UploadFileInfo_Java")]
	[Bindable]
	public class UploadFileInfo_Flex
	{
		public var file_OwnerZone:String = "";
		public var file_Owner:String = "";
		public var file_Name:String = "";
		public var file_Size:String = "";
		public var file_Time:String = "";
		public var file_Type:String = "";
		public var file_Category:String = "";
		public var file_Description:String = "";
		public var dgSelected:Boolean = false;
	}
}