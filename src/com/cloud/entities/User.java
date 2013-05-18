package com.cloud.entities;

import java.util.Date;

public class User {
	
	private int iUserId; //iUserId在数据库中是自动增长的字段。
	private int iUserRole;
	private int iUserZoneId;
	private int iUserStatus;//用户当前的状态
	private int iAccountBalance;//用户当前的余额。
	private String strUsername;
	private String strPassword;
	private String strUserDep;//用户所在单位
	private String strUserEmail;
	private String strLoginIp;//上次登录时的ip
	private String strChineseName;//用户的真实名字
	private Date dateBirthday; //出生日期
	private int  iGender;  //性别
	private String strPhoneNumber; //手机号码
	private String strAddress; //地址
	private String strOfficePhoneNumber; //办公室电话
	private String strUserZjuSsoUid;//用户的浙大统一认证系统账号
	

	public String getStrUserZjuSsoUid() {
		return strUserZjuSsoUid;
	}

	public void setStrUserZjuSsoUid(String strUserZjuSsoUid) {
		this.strUserZjuSsoUid = strUserZjuSsoUid;
	}

	public String getStrChineseName() {
		return strChineseName;
	}

	public void setStrChineseName(String strChineseName) {
		this.strChineseName = strChineseName;
	}


	public Date getDateBirthday() {
		return dateBirthday;
	}

	public void setDateBirthday(Date dateBirthday) {
		this.dateBirthday = dateBirthday;
	}


	public int getiGender() {
		return iGender;
	}

	public void setiGender(int iGender) {
		this.iGender = iGender;
	}

	public String getStrPhoneNumber() {
		return strPhoneNumber;
	}

	public void setStrPhoneNumber(String strPhoneNumber) {
		this.strPhoneNumber = strPhoneNumber;
	}

	public String getStrAddress() {
		return strAddress;
	}

	public void setStrAddress(String strAddress) {
		this.strAddress = strAddress;
	}

	public String getStrOfficePhoneNumber() {
		return strOfficePhoneNumber;
	}

	public void setStrOfficePhoneNumber(String strOfficePhoneNumber) {
		this.strOfficePhoneNumber = strOfficePhoneNumber;
	}

	public int getiUserRole() {
		return iUserRole;
	}

	public void setiUserRole(int iUserRole) {
		this.iUserRole = iUserRole;
	}

	public int getiUserStatus() {
		return iUserStatus;
	}

	public void setiUserStatus(int iUserStatus) {
		this.iUserStatus = iUserStatus;
	}

	public int getiAccountBalance() {
		return iAccountBalance;
	}

	public void setiAccountBalance(int iAccountBalance) {
		this.iAccountBalance = iAccountBalance;
	}

	public String getStrUserDep() {
		return strUserDep;
	}

	public void setStrUserDep(String strUserDep) {
		this.strUserDep = strUserDep;
	}

	public String getStrUserEmail() {
		return strUserEmail;
	}

	public void setStrUserEmail(String strUserEmail) {
		this.strUserEmail = strUserEmail;
	}

	public String getStrLoginIp() {
		return strLoginIp;
	}

	public void setStrLoginIp(String strLoginIp) {
		this.strLoginIp = strLoginIp;
	}

	public User()
	{
		
	}

	public int getiUserId() {
		return iUserId;
	}

	public void setiUserId(int iUserId) {
		this.iUserId = iUserId;
	}

	public int getiUserType() {
		return iUserRole;
	}

	public void setiUserType(int iUserType) {
		this.iUserRole = iUserType;
	}

	public int getiUserZoneId() {
		return iUserZoneId;
	}

	public void setiUserZoneId(int iUserZoneId) {
		this.iUserZoneId = iUserZoneId;
	}

	public String getStrUsername() {
		return strUsername;
	}

	public void setStrUsername(String strUsername) {
		this.strUsername = strUsername;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}
	

}
