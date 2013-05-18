package lab.valueObject{
	import mx.collections.ArrayCollection;
	//import mx.rpc.remoting.RemoteObject;
	[RemoteClass(alias="com.cloud.resourceStatistic.StatisticModel")]
	
	[Bindable]
	public class StatisticModel
	{
		public var startTime:Date;
		public var endTime:Date;
		public var timeUnit:String;
		public var dataSourceFile:String;
		public var reserveTime1:int;
		public var reserveTime2:int;
	}
}
