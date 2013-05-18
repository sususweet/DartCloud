package com.cloud.login;

public class Login {
	
	private static final long serialVersionUID = 1L;
	String SESSIONURL = "http://zuinfo.zju.edu.cn:8080/AMWebService/Session";
	String USERURL = "http://zuinfo.zju.edu.cn:8080/AMWebService/UserProfile";
	
	/**
	 * 验证前台输入的用户名和密码是否能够通过浙大网络中心的统一认证系统。
	 * @param strUsername 
	 * @param strPass
	 * @return 
	 */
	public boolean login_check(String strUsername, String strPass)
	{
		return true;
	}
	
	public Login()
	{
	}

}





