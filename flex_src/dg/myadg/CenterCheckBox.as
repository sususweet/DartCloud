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
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void    
		{    
			super.updateDisplayList(unscaledWidth, unscaledHeight);    
			var n:int = numChildren;    
			for(var i:int = 0; i < n; i++)    
			{    
				var c:DisplayObject = getChildAt(i);    
				if( !(c is TextField))    
				{    
					c.x = Math.round((unscaledWidth - c.width) / 2); 
					//c.x = 6;
					c.y = Math.round((unscaledHeight - c.height) /2 );    
				}    
			}    
		}    
	}
}