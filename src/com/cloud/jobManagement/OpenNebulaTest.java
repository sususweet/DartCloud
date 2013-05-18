package com.cloud.jobManagement;

import org.opennebula.client.user.User;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;

/**
 * OpenNebula类主要用于测试和OpenNebula相关的一些功能；
 */
public class OpenNebulaTest {

	public OpenNebulaTest(){
		
	}
	
	public void createAnUser(int begin, int end){
//		Client oneClient = null;
//		
//		System.out.println("申请用户id：");
//		try {
//			oneClient = new Client("frank:123", "http://192.168.0.154:2634/RPC2");
//		} catch (ClientConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		 //OneResponse rc = User.allocate(oneClient, "bruce", "123");
		 //int newUserId = Integer.parseInt(rc.getMessage());
		 //System.out.println(newUserId);

//		VmOperation vm = new VmOperation("root:mypass", "http://192.168.0.154:2634/RPC2");
//		for(int i=begin; i<=end; i++){
//			try {
//				vm.deleteOneVm(i);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("excuting main function.");
		OpenNebulaTest one = new OpenNebulaTest();
		one.createAnUser(80,82);
	}

}
