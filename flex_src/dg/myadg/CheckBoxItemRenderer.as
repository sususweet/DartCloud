package dg.myadg
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import dg.AdvancedDataGrid;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.controls.listClasses.IListItemRenderer;
	
	public class CheckBoxItemRenderer extends CenterCheckBox implements IListItemRenderer//hx：复选框列的单个复选框渲染器
	{
		private var currentData:Object; //保存当前一行值的对象  
		private var _data:Object;
		
		public function CheckBoxItemRenderer(){  
			super();  
			this.addEventListener(Event.CHANGE,onClickCheckBox);  
			this.toolTip = "选择";  
		}  
		
		public function set data(value:Object):void{
			if(null != value){
				this.selected = value.dgSelected;  
				this.currentData = value; //保存整行的引用  
			}
		}  
		
		/**点击check box时，根据状况向selectedItems array中添加当前行的引用，或者从array中移除 
		 * hx:上面的原作者的话已失效，这个方法的作用只有一个：当用户取消这个复选框的选中状态时，如果全选处于勾选状态，则取消勾选；
		 * 如果当用户选中这个复选框，则什么也不干*/ 
		private function onClickCheckBox(e:Event):void{   
			var dgg:AdvancedDataGrid = AdvancedDataGrid(this.owner);//获取DataGrid对象  
			var column:CheckboxColumn = dgg.columns[0];//获取整列的显示对象 
			var dgDataArr:ArrayCollection = dgg.dataProvider as  ArrayCollection;
			this.currentData.dgSelected = this.selected;//根据是否选中的状态，更改数据源中选中的标记
			if(!(this.selected) && column.cloumnSelected)//如果不选中
			{
				column.cloumnSelected = false;
				dgDataArr.refresh();
			}
			/*var selectItems:Array = column.selectItems;
			/this.currentData.dgSelected = this.selected;//根据是否选中的状态，更改数据源中选中的标记  
			if(this.selected){  
			selectItems.push(this.currentData);  
			}else{  
			for(var i:int = 0; i<selectItems.length; i++){  
			if(selectItems[i] == this.currentData){  
			selectItems.splice(i,1)  
			}  
			}  
			}*/  
		}  
		
		public function get data():Object
		{
			return _data;
		}
		
	}
}