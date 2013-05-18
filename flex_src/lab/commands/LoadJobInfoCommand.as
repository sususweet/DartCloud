package lab.commands
{
	import com.adobe.cairngorm.commands.ICommand;
	import com.adobe.cairngorm.control.CairngormEvent;
	import com.adobe.cairngorm.model.ModelLocator;
	
	import lab.business.delegates.LoadJobInfoDelegate;
	import lab.business.delegates.LoadUserInfoDelegate;
	import lab.events.LoadJobInfoEvent;
	import lab.model.DCModelLocator;
	import lab.util.UtilFunc;
	import lab.valueObject.Job;
	import lab.valueObject.User;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.rpc.IResponder;
	import mx.rpc.events.ResultEvent;
	
	
	public class LoadJobInfoCommand implements ICommand, IResponder
	{
		private var modelLocator:DCModelLocator = DCModelLocator.getInstance();
		
		public function LoadJobInfoCommand(){}
		
		public function execute(event:CairngormEvent):void
		{
			var iUserId:int = (event as LoadJobInfoEvent).iUserId;
			var jobDelegate:LoadJobInfoDelegate = new LoadJobInfoDelegate(this);
			jobDelegate.callServer(iUserId);
			
		}
		public function result(data:Object):void
		{
			var jobArr:ArrayCollection = (data as ResultEvent).result as ArrayCollection;
			
			if(jobArr)
			{
				//每次重新加载用户数据的时候，都是将原来的数据全部删除，再重新赋值一遍。有更好地实现方式吗？
				modelLocator.userJobInfoAC.removeAll();
				//获得用户的数据之后，需要初始化modelLocator中的数据。
				for(var i:int=0; i<jobArr.length; i++)
				{
					var srcJob:Job = jobArr[i] as Job;
					modelLocator.userJobInfoAC.addItem(srcJob);
					
				}
				initUserData(modelLocator.userJobInfoAC);
			}
		}
		
		public function fault(data:Object):void
		{
			trace("fault at LoadUserInfoCommand !" );
		}
		
		
		protected function initUserData(userJobInfo:ArrayCollection):Boolean
		{
			var _model:DCModelLocator =DCModelLocator.getInstance();
			
			modelLocator.JobInfoXMLList = new XMLListCollection(GetJobInfoXLC(userJobInfo));//在树形控件中显示所有信息
			
			_model.selectedVmInfoAC = UtilFunc.getAllVmInfo(userJobInfo); //在表格中显示所有的虚拟机信息。
			return true;
		}
		
		//将信息显示到业务这一层。
		private function GetJobInfoXLC(arrJobInfo:ArrayCollection):XMLList
		{ 
			var rootXml:XMLList = new XMLList("<dataCenter label=\"业务中心\" type=\"0\" />");
			if(arrJobInfo) //若arr不为空。
			{
				for(var i:int=0; i<arrJobInfo.length; i++)
				{ 
					var jobXml:XMLList = new XMLList("<cluster label=\"" +arrJobInfo[i].strJobName +"\"" + " id= \" " + i + 
						"\" type=\"1\" />"); 
					
					var vmInfoAC:ArrayCollection = arrJobInfo[i].vmArrayInfo;
					for(var j:int=0; j<vmInfoAC.length; j++)
					{
						var id:int = i * 1000 + j;
						var vmXml:XML = new XML("<node label=\"" + vmInfoAC[j].strVmName + "\"" + " id=\"" +
							id + "\" type=\"2\" />");
						jobXml.appendChild(vmXml);
					}
					
					rootXml.appendChild(jobXml); 
				}
				
			}
			return rootXml; 
		} 
		
		
		
		
	}
	
}