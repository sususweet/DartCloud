package dg.myadg
{
	import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
	
	public class CheckboxColumn extends AdvancedDataGridColumn
	{
		public var cloumnSelected:Boolean=false;//保存该列是否全选的属性（用户先点击全选后在手动的取消几行数据的选中状态时，这里的状态已经被完善为可以改变）            
		public var selectItems:Array = new Array();//保存用户选中的数据  
		public function CheckboxColumn(columnName:String=null)
		{
			super(columnName);
		}
	}
}