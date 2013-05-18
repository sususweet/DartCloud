package lab.control
{
	import com.adobe.cairngorm.control.FrontController;
	
	import lab.commands.*;
	import lab.events.*;
	
	public class DCController extends FrontController
	{
		public function DCController()
		{
			super();
			this.initialize();
		}
		
		public function initialize():void
		{
			addCommand(LoadConfigureEvent.EVENT_ID, LoadConfigureCommand);
			addCommand(LoadUserInfoEvent.EVENT_ID, LoadUserInfoCommand);
			addCommand(DeleteOneJobEvent.EVENT_ID, DeleteOneJobCommand);
			addCommand(VmOperationEvent.EVENT_ID, VmOperationCommand);
			addCommand(DeleteOneVmEvent.EVENT_ID, DeleteOneVmCommand);
			addCommand(CreateOneJobEvent.EVENT_ID, CreateOneJobCommand);
			addCommand(LoadJobInfoEvent.EVENT_ID, LoadJobInfoCommand);
//			addCommand(LoadResTreeEvent.EVENT_ID, LoadResTreeCommand);
//			addCommand(LoadMonitorDataEvent.EVENT_ID, LoadMonitorDataCommand);
//			addCommand(LoadWebServiceMonitorDataEvent.EVENT_ID, LoadWebServiceMonitorDataCommand);
//			addCommand(GetResourceTasksEvent.EVENT_ID, GetResourceTasksCommand);
//			addCommand(LoadOrderListEvent.EVENT_ID, LoadOrderListCommand);
//			addCommand(SelectedOrderChangedEvent.EVENT_ID, SelectedOrderChangedCommand);
//			addCommand(GetFlowResourceEvent.EVENT_ID, GetFlowResourceCommand);
//			addCommand(GetAllOrderProgressEvent.EVENT_ID, GetAllOrderProgressCommand);
//			addCommand(LoadFlowTemplatesInfoEvent.EVENT_ID, LoadFlowTemplatesInfoCommand);
//			addCommand(GetOrderFlowProgressEvent.EVENT_ID, GetOrderFlowProgressCommand);
//			addCommand(LoadNodesInfoEvent.EVENT_ID, LoadNodesInfoCommand);
//			addCommand(LoadResourcesInfoEvent.EVENT_ID, LoadResourcesInfoCommand);
//			addCommand(LoadMapColorEvent.EVENT_ID, LoadMapColorCommand);
//			addCommand(GetSystemTimeEvent.EVENT_ID, GetSystemTimeCommand);
		}
		
	}
}