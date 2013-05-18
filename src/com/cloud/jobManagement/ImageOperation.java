/*
 * JobOperation
 * @version 1.0,
 * @Date 2012-3-14
 * @author  Frank
*/
package com.cloud.jobManagement;

import org.opennebula.client.*;
import org.opennebula.client.datastore.*;
import org.opennebula.client.image.*;
import org.opennebula.client.OneResponse;
import com.cloud.config.DataCenterInfo;
import com.cloud.entities.OsType;
import com.cloud.entities.ImageTempfileName;
import com.cloud.utilFun.LogWriter;
import com.cloud.config.TemplateInfo;

import java.util.*;

/*
 * JobOpertion类主要提供和业务相关的一些操作，具体包括：新建、修改、删除、查询；
 */
public class ImageOperation {
	private ImagePool imagePool;
	private Client client;
	private Iterator<Image> imageList;
	private static LogWriter logWriter = LogWriter.getLogWriter();
	public ImageOperation(Client oneClient){
		try{
			client = oneClient;
			imagePool = new ImagePool(client);
		}catch(Exception e){
			logWriter.log("ImageOperation：实例化错误：" + e);
		}
	}
	
	private  void getImageList(){
		OneResponse rs = imagePool.infoAll();
		//logWriter.log("ImageOperation/getImageList:" + rs.getErrorMessage() + "*****************");
		//logWriter.log("ImageOperation/getImageList:" + rs.getMessage() + "*****************");
		imageList = imagePool.iterator();
	}
	
	private String getStrOsType(int osType){
		if(osType == OsType.LINUX_DEBIAN){
			return "debian";
		}
		if(osType == OsType.LINUX_UBUNTU){    //这边改成106的原因是已经创建了一个101的镜像库，在磁盘阵列到来之前
			return "ubuntu-106";
		}
		if(osType == OsType.LINUX_CENTOS){
			return "centos";
		}
		if(osType == OsType.LINUX_FEDORA){
			return "fedora";
		}
		if(osType == OsType.WINXP){
			return "winxp";
		}
		if(osType == OsType.WIN7){
			return "win7";
		}
		logWriter.log("虚拟机操作系统类型iOsType出错！");
		return null;
	}
	
	public static String getImageTemplateFileName(int osTypeId)
	{
		if(osTypeId == OsType.LINUX_DEBIAN)
		{
			return ImageTempfileName.LINUX_DEBIAN;
		}
		else if(osTypeId == OsType.LINUX_UBUNTU)
		{
			return ImageTempfileName.LINUX_UBUNTU;
		}
		else if(osTypeId == OsType.LINUX_CENTOS)
		{
			return ImageTempfileName.LINUX_CENTOS;
		}
		else if(osTypeId == OsType.LINUX_FEDORA)
		{
			return ImageTempfileName.LINUX_FEDORA;
		}
		else if(osTypeId == OsType.WIN7)
		{
			return ImageTempfileName.WIN7;
		}
		else if(osTypeId == OsType.WINXP)
		{
			return ImageTempfileName.WINXP;
		}
		else
		{
			logWriter.log("虚拟机操作系统类型iOsType出错！");
			return null;
		}
	}
	
	private int getDataStoreId(String dsName){
		int u = -1;
		DatastorePool dsPool = new DatastorePool(client);
		OneResponse rc = dsPool.info();
		//
		logWriter.log(rc.getMessage());
		
		
		Iterator<Datastore> dsList = dsPool.iterator();
		while(dsList.hasNext()){
			Datastore ds = dsList.next();
			logWriter.log(ds.xpath("NAME"));
			
			if(ds.xpath("NAME").equals(dsName)){
				String id = ds.getId();
				u = Integer.parseInt(id);
				break;
			}
		}
		return u;
	}
	
	public int getAvailableImage(int osType){
		int u = -1;
		String strOsType = getStrOsType(osType);
		
		if(imageList == null){
			getImageList();
		}
		//*************************************//
		logWriter.log("OsType是"+strOsType);
		//*************************************//
		if(imageList.hasNext()){
			logWriter.log("数据库中有镜像.");
		}
		else{
			logWriter.log("********没有镜像**********");
		}
		
		while(imageList.hasNext()){
			Image img = imageList.next();
			
			/*
			logWriter.log("image info: " + img.getName() + " state: " + img.state());
			logWriter.log("DATASTORE INFO: " + img.xpath("DATASTORE") + "contains " + strOsType
					+ img.xpath("DATASTORE").toLowerCase().contains(strOsType.toLowerCase()) );
					*/
			
			//虚拟机的DATASTORE中是否包含strOsType这个字符串。
			if(img.xpath("DATASTORE").toLowerCase().contains(strOsType.toLowerCase())
					&& img.state() == 1){
				u = Integer.parseInt(img.getId());
			}
		}
		//因为有不同的用户在取用镜像，所以要在每次获取之前，初始化一下镜像列表为Null
		imageList = null;
		return u;
	}
	
	/*
	public void createImage(int osType){
		String strOsType = getStrOsType(osType);
		int dataStoreId = getDataStoreId(strOsType);
		if(dataStoreId == -1){
			logWriter.log("无法获取DataStore，新建镜像失败！");
			return;
		}
		logWriter.log("dataStoreId: " + dataStoreId);
		
		String imageFile = getImageTemplateFileName(osType);
		logWriter.log(imageFile);
		
		TemplateInfo tinfo = new TemplateInfo(imageFile);
		String imageTemplate = tinfo.getStr_template();
		logWriter.log(imageTemplate);
		
		
		try{
			OneResponse rc = Image.allocate(client, imageTemplate, dataStoreId);
			logWriter.log(rc.getErrorMessage());
		}catch(Exception e){
			logWriter.log("新建镜像出错：" + e);
		}
	}
	*/
	
	
	public void deleteImage(int imageId){
		try{
			OneResponse rc = Image.delete(client, imageId);
			logWriter.log("The result of deleting an image: ");
			logWriter.log(rc.getErrorMessage()+ " " + rc.getMessage());
		}catch(Exception e){
			logWriter.log("镜像删除失败："+e);
		}
	}
	
	
	public static void main(String args[]){
		try {
			Client client = new Client("root:root", "http://192.168.100.250:2633/RPC2");
			ImageOperation io = new ImageOperation(client);
			io.deleteImage(255);
		} catch (ClientConfigurationException e) {
			e.printStackTrace();
		}
	}
	
}
