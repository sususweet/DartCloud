package com.cloud.entities;

/*
* @author  Frank
* @version 1.0, 2012-3-14
*/

public class VM {
	private String strVmId;
	private String strVmName;
	private int iVmJobId;
	private int iVmServerId;
	private int iVmCpuNum;
	private int iVmMemorySize;
	private int iVmStorageSize;
	private int iImageId;
	private String strVmPass;
	private String strVmIp;
	private int iNetStatus;
	
	
	public VM(){
	}
	public VM(String strVmId, String strVmName, int iVmJobId, int iVmServerId,
			int iVmCpuNum, int iVmMemorySize, int iVmStorageSize, String strVmPass,
			String strVmIp, int iNetStatus) {
		super();
		this.strVmId = strVmId;
		this.strVmName = strVmName;
		this.iVmJobId = iVmJobId;
		this.iVmServerId = iVmServerId;
		this.iVmCpuNum = iVmCpuNum;
		this.iVmMemorySize = iVmMemorySize;
		this.iVmStorageSize = iVmStorageSize;
		this.strVmPass = strVmPass;
		this.iNetStatus = iNetStatus;
		this.strVmIp = strVmIp;
	}
	
	public int getiNetStatus() {
		return iNetStatus;
	}
	public void setiNetStatus(int iNetStatus) {
		this.iNetStatus = iNetStatus;
	}
	public String getStrVmIp() {
		return strVmIp;
	}
	public void setStrVmIp(String strVmIp) {
		this.strVmIp = strVmIp;
	}
	public int getiVmServerId() {
		return iVmServerId;
	}
	public void setiVmServerId(int iVmServerId) {
		this.iVmServerId = iVmServerId;
	}
	public int getiVmCpuNum() {
		return iVmCpuNum;
	}
	public void setiVmCpuNum(int iVmCpuNum) {
		this.iVmCpuNum = iVmCpuNum;
	}
	public String getStrVmPass() {
		return strVmPass;
	}
	public void setStrVmPass(String strVmPass) {
		this.strVmPass = strVmPass;
	}
	public String getstrVmId() {
		return strVmId;
	}
	public void setstrVmId(String strVmId) {
		this.strVmId = strVmId;
	}
	public String getStrVmName() {
		return strVmName;
	}
	public void setStrVmName(String strVmName) {
		this.strVmName = strVmName;
	}
	public int getiVmJobId() {
		return iVmJobId;
	}
	public void setiVmJobId(int iVmJobId) {
		this.iVmJobId = iVmJobId;
	}
	public int getiServerId() {
		return iVmServerId;
	}
	public void setiServerId(int iVmServerId) {
		this.iVmServerId = iVmServerId;
	}
	public int getiVMCpuNum() {
		return iVmCpuNum;
	}
	public void setiVMCpuNum(int iVmCpuNum) {
		this.iVmCpuNum = iVmCpuNum;
	}
	public int getiVmMemorySize() {
		return iVmMemorySize;
	}
	public void setiVmMemorySize(int iVmMemorySize) {
		this.iVmMemorySize = iVmMemorySize;
	}
	public int getiVmStorageSize() {
		return iVmStorageSize;
	}
	public void setiVmStorageSize(int iVmStorageSize) {
		this.iVmStorageSize = iVmStorageSize;
	}

	public int getiImageId() {
		return iImageId;
	}
	public void setiImageId(int iImageId) {
		this.iImageId = iImageId;
	}
}
