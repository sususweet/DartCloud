package lab.events
{
	import flash.events.Event;
	
	/**
	 * 删除ArrayCollection中的一个元素，iIndex
	 */
	public class DeleteAnItemInArrEvent extends Event
	{
		public static const EVENT_ID:String = "DeleteAnItemInArr";
		public var iIndex:int;
		public function DeleteAnItemInArrEvent(iIndex:int)
		{
			super(EVENT_ID);
			this.iIndex = iIndex;
		}
	}
}