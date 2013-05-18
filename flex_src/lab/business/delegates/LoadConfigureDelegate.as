package lab.business.delegates
{
	import com.adobe.cairngorm.business.ServiceLocator;

	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.http.HTTPService;
	
	public class LoadConfigureDelegate
	{
		private var serviceLocator:ServiceLocator = ServiceLocator.getInstance();
		private var _service:HTTPService;
		private var _responder:IResponder;
		
		public function LoadConfigureDelegate(responder:IResponder)
		{
			_service = serviceLocator.getHTTPService("loadConfigureService");
			_responder = responder;
		}
		
		public function loadConfigure():void
		{
			var token:AsyncToken = _service.send();
			token.addResponder(_responder);
		}
		
	}
}