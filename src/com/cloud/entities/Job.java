/*
 * Job
 * @version 1.0,
 * @Date 2012-3-16
 * @author  Frank
*/
package com.cloud.entities;

import java.util.Date;
import java.util.List;

public class Job {
	private int iJobId;
	private int iCpuNum;
	private int iMemSize;
	private int iStorageSize;
	private int iVmNum;
	private int iUserId;
	private int iJobStatus;
	private int iApplyHour;
	private int iOSType; //一个业务中可能有不同的OS，所以这个字段可能没有用。
	private int iZoneId;
	private String strJobName;
	private String strJobDescription;
	private String strZoneName;
	private String strZoneHost; //openNebula调用时所用到的IP。
	private String strZoneLAN_Ip;
	private String strZoneSchoolIp;
	private String strZonePublicIp;
	private String strVmPass;
	private Date dateJobStartTime;
	private List<VM> vmArrayInfo;
	private int iNetStatus; //0表示只能上内网；1表示可以上外网；2表示内网转为外网，3表示外网转为内网。
	
	

	public int getiNetStatus() {
		return iNetStatus;
	}

	public void setiNetStatus(int iNetStatus) {
		this.iNetStatus = iNetStatus;
	}

	public String getStrZoneHost() {
		return strZoneHost;
	}

	public void setStrZoneHost(String strZoneHost) {
		this.strZoneHost = strZoneHost;
	}

	
	public String getStrZoneName() {
		return strZoneName;
	}

	public void setStrZoneName(String strZoneName) {
		this.strZoneName = strZoneName;
	}

	public int getiApplyHour() {
		return iApplyHour;
	}

	public void setiApplyHour(int iApplyHour) {
		this.iApplyHour = iApplyHour;
	}

	public int getiOSType() {
		return iOSType;
	}

	public void setiOSType(int iOSType) {
		this.iOSType = iOSType;
	}
	
	
	public List<VM> getVmArrayInfo() {
		return vmArrayInfo;
	}

	public void setVmArrayInfo(List<VM> vmArrayInfo) {
		this.vmArrayInfo = vmArrayInfo;
	}

	public Job(){
	}
	
	public String getStrJobName() {
		return strJobName;
	}

	public void setStrJobName(String strJobName) {
		this.strJobName = strJobName;
	}

	public String getStrJobDescription() {
		return strJobDescription;
	}

	public void setStrJobDescription(String strJobDescription) {
		this.strJobDescription = strJobDescription;
	}

	public int getiJobId() {
		return iJobId;
	}

	public void setiJobId(int iJobId) {
		this.iJobId = iJobId;
	}

	public int getiCpuNum() {
		return iCpuNum;
	}

	public void setiCpuNum(int iCpuNum) {
		this.iCpuNum = iCpuNum;
	}

	public int getiMemSize() {
		return iMemSize;
	}

	public void setiMemSize(int iMemSize) {
		this.iMemSize = iMemSize;
	}

	public int getiStorageSize() {
		return iStorageSize;
	}

	public void setiStorageSize(int iStorageSize) {
		this.iStorageSize = iStorageSize;
	}

	public int getiVmNum() {
		return iVmNum;
	}

	public void setiVmNum(int iVmNum) {
		this.iVmNum = iVmNum;
	}

	public int getiUserId() {
		return iUserId;
	}

	public void setiUserId(int iUserId) {
		this.iUserId = iUserId;
	}

	public int getiJobStatus() {
		return iJobStatus;
	}

	public void setiJobStatus(int iJobStatus) {
		this.iJobStatus = iJobStatus;
	}	
	public Date getDateJobStartTime() {
		return dateJobStartTime;
	}

	public void setDateJobStartTime(Date dateJobStartTime) {
		this.dateJobStartTime = dateJobStartTime;
	}
		
	public String getstrVmPass() {
		return strVmPass;
	}

	public void setstrVmPass(String strVmPass) {
		this.strVmPass = strVmPass;
	}

	public int getiZoneId() {
		return iZoneId;
	}

	public void setiZoneId(int iZoneId) {
		this.iZoneId = iZoneId;
	}


	
	
	public String getStrZoneLAN_Ip() {
		return strZoneLAN_Ip;
	}

	public void setStrZoneLAN_Ip(String strZoneLANIp) {
		strZoneLAN_Ip = strZoneLANIp;
	}

	public String getStrZoneSchoolIp() {
		return strZoneSchoolIp;
	}

	public void setStrZoneSchoolIp(String strZoneSchoolIp) {
		this.strZoneSchoolIp = strZoneSchoolIp;
	}

	public String getStrZonePublicIp() {
		return strZonePublicIp;
	}

	public void setStrZonePublicIp(String strZonePublicIp) {
		this.strZonePublicIp = strZonePublicIp;
	}

	public String getStrVmPass() {
		return strVmPass;
	}

	public void setStrVmPass(String strVmPass) {
		this.strVmPass = strVmPass;
	}

	public Job(String strJobName, String strJobDescription,int iJobId, int iCpuNum,
			int iMemSize, int iStorageSize, int iVmNum, int iUserId,
			int iJobStatus, Date dateStartTime) {
		super();
		this.strJobName = strJobName;
		this.strJobDescription = strJobDescription;
		this.iJobId = iJobId;
		this.iCpuNum = iCpuNum;
		this.iMemSize = iMemSize;
		this.iStorageSize = iStorageSize;
		this.iVmNum = iVmNum;
		this.iUserId = iUserId;
		this.iJobStatus = iJobStatus;
		this.dateJobStartTime = dateStartTime;
	}
	
}
