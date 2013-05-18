package test;

import org.opennebula.client.Client;
import org.opennebula.client.template.*;
import org.opennebula.client.OneResponse;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.image.ImagePool;
import org.opennebula.client.vm.VirtualMachinePool;

import com.cloud.config.DataCenterInfo;
import com.cloud.jobManagement.VmOperation;

public class OpenNebulaTest {
//	private Client oneClient;
////	private VirtualMachinePool vmPool;
//	
//	public OpenNebulaTest(){
//		
//		//获取配置信息
//		DataCenterInfo dataCenterInfo = DataCenterInfo.getInstance();
//		
//		try {
//			oneClient = null;
//			oneClient = new Client(dataCenterInfo.getSecret(), dataCenterInfo.getEndpoint());
//			
////			vmPool = new VirtualMachinePool(oneClient);
//		} catch (ClientConfigurationException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String args[])
	{
//		try {
//			DataCenterInfo dataCenterInfo = DataCenterInfo.getInstance();
//			Client oneClient = new Client(dataCenterInfo.getSecret(), dataCenterInfo.getEndpoint());
//			
//			OneResponse rc =  TemplatePool.infoAll(oneClient);
//			System.out.println(rc.getMessage());
//			
//		} catch (ClientConfigurationException e) {
//			e.printStackTrace();
//		}
		
		
		String template = "CPU=1 \n" 
		+ "MEMORY  = 1024 \n"
		+"DISK = [\n"
		+"    source = \"/image-pool/imagePool/vm01/disk.img\"," + "\n"
		+"    target = xvda2,\n"
		+"    driver = \"file:\",\n"
		+"    clone  = \"no\",\n"
		+"    readonly = \"no\"\n"
		+"]\n"
		
		+"OS  = [\n"
		+"    BOOTLOADER=/usr/bin/pygrub,\n"
		+"    ROOT    = \"xvda2\"\n"
		+"    ]\n"
		
		+"NIC = [\n"
		+"    NETWORK_ID = 1\n"
		+"    ]\n"
	
		+"CONTEXT = [\n"
		+"    hostname   = \"$NAME\",\n"
		+"    ip         = \"$NIC[IP]\",\n"
		+"    files      = \"/srv/cloud/templates/vm/init.sh /srv/cloud/templates/vm/id_rsa.pub\",\n"
	    +"    target     = \"xvda3\"\n"
	    +"]\n";
		
		
		for(int i=0; i < 2; i++){
	        
	        String tmp = template.replace("disk.img", "disk"+ (i+1) +".img");
	        System.out.println(tmp);
		}
		
		
		
		
		
	}
	
}
