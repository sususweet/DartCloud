package lab.valueObject
{
	//与远程Java类进行绑定。
	[RemoteClass(alias="com.cloud.entities.User")]
	[Bindable]
	public class User {   
		//iUserId在数据库中是自动增长的字段。
		public var iUserId:int; 
		public var iUserRole:int;
		public var iUserZoneId:int;
		public var iUserStatus:int;//当前状态
		public var iAccountBalance:int;//余额
		public var strUsername:String; 
		public var strPassword:String;
		public var strUserDep:String;//用户所在单位
		public var strUserEmail:String;
		public var strLoginIp:String;//上次登录时的ip
		public var strChineseName:String; //用户的真实名字
		public var strIdentityId:String;  //身份证号
		public var dateBirthday:Date; //出生日期
		public var iGender:int;  //性别
		public var strPhoneNumber:String; //手机号码
		public var strAddress:String; //地址
		public var strOfficePhoneNumber:String; //办公室电话
		public var strUserZjuSsoUid:String; //用户的浙大统一认证系统账号
		
		public function User()
		{
			iUserId = 0;
			dateBirthday = new Date('1988/08/09')
		}
		
	}
	
}