package dg.myadg
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import dg.AdvancedDataGrid;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.controls.listClasses.IListItemRenderer;
	
	public class CheckBoxHeaderRenderer extends CenterCheckBox implements IListItemRenderer//hx：复选框列头渲染器
	{
		private var _data:CheckboxColumn;//定义CheckBox列对象   
		public function CheckBoxHeaderRenderer(){  
			super();  
			this.addEventListener(Event.CHANGE,onChange);//CheckBox状态变化时触发此事件  
			this.toolTip = "全选";  
		}  
		
		public function get data():Object{  
			return _data;//返回的是CheckBox列的对象  
		}  
		
		public function set data(value:Object):void{  
			_data = value as CheckboxColumn;  
			//trace(_data.cloumnSelected);  
			selected = _data.cloumnSelected;  
		}  
		
		
		/** hx:这个方法的作用只有一个：当用户选中这个复选框时，则列全选，当用户取消选中时，则列全不选*/
		private function onChange(event:Event):void{      
			
			var dgg:AdvancedDataGrid = AdvancedDataGrid(this.owner);//获取DataGrid对象  
			var column:CheckboxColumn = dgg.columns[0];//获取整列的显示对象  
			
			var dgDataArr:ArrayCollection = dgg.dataProvider as  ArrayCollection;  
			
			column.cloumnSelected = this.selected;//更改列的全选状态 
			if(dgDataArr.length > 0 && dgDataArr[0] != "")
			{
				for(var i: int = 0; i < dgDataArr.length; i ++)
				{
					Object(dgDataArr[i]).dgSelected = this.selected;
				}
			}
			dgDataArr.refresh();//刷新数据源，达到强制更新页面显示效果的功能，防止在使用时没有在VO类中使用绑定而出现点击全选页面没有更改状态的错误 
			
			
			/*column.selectItems = new Array();//重新初始化用于保存选中列的对象  
			
			if(this.selected){//如果为全部选中的化就将数据源赋值给column.selectItems，不是就不管他，上一步已经初始化为空  
			column.selectItems = (dg.dataProvider as ArrayCollection).toArray();  
			}  
			if(dgDataArr.length>0){  
			if(dgDataArr[0]!=""){  
			for(var i:int = 0; i < dgDataArr.length ; i++){  
			Object(dgDataArr[i]).dgSelected = this.selected;//更改没一行的选中状态  
			}   
			}    
			}   	
			dgDataArr.refresh();//刷新数据源，达到强制更新页面显示效果的功能，防止在使用时没有在VO类中使用绑定而出现点击全选页面没有更改状态的错误        
			*/
		}  
	}
}