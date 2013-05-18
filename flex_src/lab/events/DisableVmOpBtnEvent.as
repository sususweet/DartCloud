package lab.events
{
	import flash.events.Event;
	
	/**
	 * 当对虚拟机执行关机操作之后，若操作成功，则发出一个此事件，将对应的虚拟机的关机、vnc、webssh状态置为未激活状态；
	 */
	public class DisableVmOpBtnEvent extends Event
	{
		public static const EVENT_ID:String = "DisableVmOpBtn";
		public var iVmId:int;
		public function DisableVmOpBtnEvent(iVmId:int)
		{
			super(EVENT_ID);
			this.iVmId = iVmId;
		}
	}
}