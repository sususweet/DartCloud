package dg
{
	import mx.collections.ArrayCollection;
	import mx.controls.AdvancedDataGrid;
	import dg.myadg.*;
	
	public class AdvancedDataGrid extends mx.controls.AdvancedDataGrid
	{
		/** 设置grid是否有check框 */
		private var _hasCheck:Boolean;
		
		public function AdvancedDataGrid()
		{
			super();
		}
		
		public function get hasCheck():Boolean
		{
			return _hasCheck;
		}
		
		public function set hasCheck(value:Boolean):void
		{
			_hasCheck = value;
		}
		
		/** 取得选中的值 
		 * 	hx: 获取复选框选中情况的操作要推迟到最后时刻进行，不要通过捕获事件实时监听，容易发生错误
		 *  hx: 让使用这个组件的所有人只能通过这个方法访问selectItems，而不能直接访问，这样就不会发生数据与表现不一致的错误
		 *  hx: 这一点很重要*/
		public function getSelectedItem():Array
		{
			if(this.hasCheck)
			{
				var dgDataArr:ArrayCollection = this.dataProvider as ArrayCollection;
				var column:CheckboxColumn = this.columns[0];
				column.selectItems.splice(0);
				for(var i: int = 0; i < dgDataArr.length; i ++)
				{
					if(Object(dgDataArr[i]).dgSelected)
					{
						column.selectItems.push(dgDataArr[i]);
					}
				}
				return column.selectItems;
			}
			return null;
		} 
		
		
		
		/** 取消所有的item */
		public function cancelSelectedItem():void {
			this.columns[0].selectItems.splice(0);
		}
		
	}
}