package dg.myadg
{
	import flash.display.DisplayObject;
	import flash.text.TextField;
	
	import spark.components.CheckBox;
	import mx.controls.Alert;
	
	public class CenterCheckBox extends CheckBox//hx：这个类的作用是重写复选框，使其居中显示
	{
		public function CenterCheckBox()
		{
			super();
		}
		
		// 居中展现
		/**this指向复选框，但是this.parent指向整个表格，而不是单元格 ，*/
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void    
		{    
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			//this.setStyle("textAlign", "right");
			//this.x = this.parent.width / 2 - unscaledWidth / 2;//this.parent指向整个表格的对象
			//this.y = this.parent.height / 2 - unscaledHeight / 2;
			//Alert.show("宽度：  " + unscaledWidth + "---" + " 高度： " + unscaledHeight);
			//Alert.show("复选框宽度：" + this.width);
			//this.x = unscaledWidth / 2;
			//this.y = unscaledHeight / 2;
			//Alert.show(this.owner.toString() + "有多少孩子：" + numChildren);
			var n:int = numChildren;    
			for(var i:int = 0; i < n; i++)    
			{    
				var c:DisplayObject = getChildAt(i);    
				if( !(c is TextField))    
				{    
					//c.x = Math.round((unscaledWidth - c.width) / 2); 
					c.x = unscaledWidth / 2 - 7;
					if(c.x < 0)
					{
						c.x = 0;
					}
					//c.y = Math.round((unscaledHeight - c.height) /2 );    
				}    
			}
			//Alert.show("当前所在宽度位置： " + c.x);
		}
	}
}