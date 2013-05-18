package com.cloud.entities;

/**
 * 本类是外网申请的前后台交互类。
 * @author frank Yan
 *
 */

public class NetApply {
	
	private String strVmId;
	private String strVmName;
	private String strJobName;
	private String strZoneName;
	private String strUsrName;
	private String strVmIp;
	private int iVmNetStatus;
	
	public String getStrVmId() {
		return strVmId;
	}
	public void setStrVmId(String strVmId) {
		this.strVmId = strVmId;
	}
	public String getStrVmName() {
		return strVmName;
	}
	public void setStrVmName(String strVmName) {
		this.strVmName = strVmName;
	}
	public String getStrJobName() {
		return strJobName;
	}
	public void setStrJobName(String strJobName) {
		this.strJobName = strJobName;
	}
	
	public String getStrZoneName() {
		return strZoneName;
	}
	public void setStrZoneName(String strZoneName) {
		this.strZoneName = strZoneName;
	}
	public String getStrUsrName() {
		return strUsrName;
	}
	public void setStrUsrName(String strUsrName) {
		this.strUsrName = strUsrName;
	}
	public String getStrVmIp() {
		return strVmIp;
	}
	public void setStrVmIp(String strVmIp) {
		this.strVmIp = strVmIp;
	}
	public int getiVmNetStatus() {
		return iVmNetStatus;
	}
	public void setiVmNetStatus(int iVmNetStatus) {
		this.iVmNetStatus = iVmNetStatus;
	}

}
