package com.cloud.cda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.utilFun.LogWriter;

import validate.CookieValidate;
import validate.CookieValidating;

/**
 * 用HTTP方式实现用户对统一身份认证系统的登陆、注销和获得用户ID的功能
 */
public class SessionClient extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String SESSIONURL = "http://zuinfo.zju.edu.cn:8080/AMWebService/Session";
	String USERURL = "http://zuinfo.zju.edu.cn:8080/AMWebService/UserProfile";

	String appUid = "publictest";
	String appPwd = "zjuinfo";
	
	private static boolean m_login_status = false;
	private LogWriter logWriter = LogWriter.getLogWriter();
	
	
	public SessionClient()
	{
	}
	
	/**
	 * 实现根据返回的SSOTokenID获取用户ID的功能,
	 * 用户ID通过request传递，属性名为ZJU_SSO_UID，不能成功获得的时候为null
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException, IOException 
	{
		try {
			String meth = request.getParameter("method");
			if (meth != null && meth.endsWith("delete")) {
				doDelete(request, response);
			} else {
				String uid = Get(request, response);
				if (uid == null || uid.trim().equals("")) {
					// 如果uid为空，则说明没登录，重定向到登录页面。
					response.sendRedirect("http://yj.zju.edu.cn:8080/LoginTest/login.jsp");
				} else {
					String name = getName(uid);
					request.setAttribute("ZJU_SSO_UID", uid);
					request.setAttribute("NAME", name);
					response.setHeader("Pragma", "No-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
					getServletConfig().getServletContext().getRequestDispatcher("/uid.jsp").forward(request,
									response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 实现用户对统一身份认证系统的登陆功能,
	 * SSOTokenID通过request传递，属性名为iPlanetDirectoryPro，若登陆不成功为null
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) 
	{
		String iPlanetDirectoryPro = Post(request, response);
//		System.out.println("parameter iPlanetDirectoryPro: " + iPlanetDirectoryPro);
		try {
			String uid = Get(iPlanetDirectoryPro);
			if (uid == null) {
				//response.sendRedirect("http://yj.zju.edu.cn:8080/LoginTest/login.jsp");
				//response.sendRedirect("http://localhost:8080/LoginTest/login.jsp");
				
				m_login_status = false;
				
				logWriter.log("login failure,uid=null");
				
				/***
				 * 以下是严海明增加的代码。
				 * 增加此代码的原因：前台界面在用户输入正确的密码的情况下士能够正常工作的，
				 * 但如果输入的密码不正确的话，前台的userlogin.mxml的sendLoginRequest
				 * 函数就不能正常截获Event.complete事件。
				 
				
				String name = "user_not_exits";
				request.setAttribute("ZJU_SSO_UID", uid);
				request.setAttribute("NAME", name);
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				
				//返回给前台的信息
				String strRest = "NAME=" + name + ";ZJU_SSO_UID=" + uid;
				response.getWriter().write(strRest);
				
				*/
				
			} else {
				
				m_login_status = true;
				
				logWriter.log("login success...uid="+uid);
				String name = getName(uid);
				
				request.setAttribute("ZJU_SSO_UID", uid);
				request.setAttribute("NAME", name);
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				
				//返回给前台的信息
				String strRest = "NAME=" + name + ";ZJU_SSO_UID=" + uid;
				response.getWriter().write(strRest);
				
				//难道这种只适用于jsp吗？如果要集成到Flex，需要将以下这段代码去掉。
				//getServletConfig().getServletContext().getRequestDispatcher(
				//"/uid.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 实现用户统一登出操作, 其中http：//zuinfo.zju.edu.cn路径为成功登出后的URL
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
	{
//		System.out.println("Calling doDelete...");
		String iPlanetDirectoryPro = null;
		Cookie[] cs = request.getCookies();
		if (cs == null) {
			// 如果cookie（iPlanetDirectoryPro）为空，则说明没登录，重新登录。
			response.sendRedirect("http://yj.zju.edu.cn:8080/LoginTest/login.jsp");
		} else {
			for (int i = 0; i < cs.length; i++) {
				if ("iPlanetDirectoryPro".equals(cs[i].getName())) {
					iPlanetDirectoryPro = cs[i].getValue();
					break;
				}
			}
			// 删除该用户的cookie验证信息
			CookieValidate cv = new CookieValidating();
			cv.deleteValidateData(iPlanetDirectoryPro);

			HttpURLConnection httpconn = null;
			try {
				URL myurl = new URL(SESSIONURL);
				URLConnection conn = myurl.openConnection();
				httpconn = (HttpURLConnection) conn;
				httpconn.setRequestMethod("DELETE");
				httpconn.addRequestProperty("iPlanetDirectoryPro",
						iPlanetDirectoryPro);
				httpconn.addRequestProperty("appUid", "publictest");
				httpconn.addRequestProperty("appPwd", "zjuinfo");
				httpconn.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						httpconn.getInputStream()));
				String a = httpconn.getHeaderField("status");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpconn != null)
					httpconn.disconnect();
			}
			// 删除cookie中的值
			for (int i = 0; i < cs.length; i++) {
				if ("iPlanetDirectoryPro".equals(cs[i].getName())) {
					cs[i].setMaxAge(0);
					cs[i].setPath("/");
					cs[i].setDomain(".zju.edu.cn");
					response.addCookie(cs[i]);
					break;
				}
			}
			response.sendRedirect("http://yj.zju.edu.cn:8080/LoginTest/login.jsp");
		}
	}

	// *************************************************************************************//
	/**
	 * 实现用户登陆
	 * 
	 * @param request
	 * @param response
	 * @return String 返回的是成功登陆后的SSOTokenID，如果登陆不成功该值为空
	 * @throws InterruptedException
	 */
	public String Post(HttpServletRequest request, HttpServletResponse response) 
	{
//		System.out.println("Calling Post...");
//		System.out.println("request info: " + request.toString());
		HttpURLConnection httpconn = null;
		String ssoid = null;
		try {
			String name = request.getParameter("name");
			String pwd = request.getParameter("password");
			String type = request.getParameter("type");
			String module = request.getParameter("module");
			module = "DataStore";
			//ystem.out.println("aname=" + name + ", password=" + pwd);
			
			// 对口令加密，并返回加密密钥给pwd
			if (type.equals("2")) {
				try {
					DESPlus des = new DESPlus(String.valueOf(System.currentTimeMillis())
							+ "6yhn*UHB");
					pwd = des.encrypt(pwd);
				} catch (Exception e) {
					e.printStackTrace();
					logWriter.log("Exception in Post: " + e.toString());
					return null;
				}
			}

			// 与服务器建立http连接，并将所需的参数添加到http header中
			URL myurl = new URL(SESSIONURL);
			URLConnection conn = myurl.openConnection();
			httpconn = (HttpURLConnection) conn;
			httpconn.setRequestMethod("POST");
			httpconn.addRequestProperty("name", name);
			httpconn.addRequestProperty("password", pwd);
			httpconn.addRequestProperty("module", module);
			httpconn.addRequestProperty("type", type);
			httpconn.addRequestProperty("appUid", "publictest");
			httpconn.addRequestProperty("appPwd", "zjuinfo");
			httpconn.setDoOutput(true);
			httpconn.connect();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
//			System.out.println("httpconn: " + httpconn);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				ssoid = inputLine;
			}
			in.close();
			
//			System.out.println("ssoid: " + ssoid);

			// 将登陆后获得的SSOTokenID添加到响应（Response）的cookie中
			Cookie sso_cookie = new Cookie("iPlanetDirectoryPro", ssoid);
			sso_cookie.setPath("/");
			sso_cookie.setDomain(".zju.edu.cn");
			response.addCookie(sso_cookie);
		} catch (Exception e) {
			e.printStackTrace();
			logWriter.log("Exception: " + e.toString());
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
			}
		}
		return ssoid;

	}

	/**
	 * 根据SSOTokenID返回用户ID
	 * 
	 * @param request
	 * @param response
	 * @return String 返回的是用户ID，如果未登陆则返回为空
	 * @throws IOException
	 */
	public String Get(HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
//		System.out.println("Calling Get...");
		String uid = null;
		String iPlanetDirectoryPro = null;
		Cookie[] cs = request.getCookies();
		if (cs == null) {
			return uid;
		}
		for (int i = 0; i < cs.length; i++) {
			if ("iPlanetDirectoryPro".equals(cs[i].getName())) {
				iPlanetDirectoryPro = cs[i].getValue();
				break;
			}
		}
		if (iPlanetDirectoryPro == null || iPlanetDirectoryPro.equals("")) {
			return uid;
		}
		// 蒋淑红：cookie验证
		CookieValidate cv = new CookieValidating();
		String status = cv.validate(request, response, iPlanetDirectoryPro);
		if (status.equalsIgnoreCase("1")) {
			HttpURLConnection httpconn = null;
			try {
				URL myurl = new URL(SESSIONURL);
				URLConnection conn = myurl.openConnection();
				httpconn = (HttpURLConnection) conn;
				httpconn.setRequestMethod("GET");
				httpconn.addRequestProperty("iPlanetDirectoryPro",
						iPlanetDirectoryPro);
				httpconn.addRequestProperty("appUid", "publictest");
				httpconn.addRequestProperty("appPwd", "zjuinfo");
				httpconn.connect();
				uid = httpconn.getHeaderField("ZJU_SSO_UID");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpconn != null)
					httpconn.disconnect();
			}
		}
		return uid;
	}

	/**
	 * 根据cookie中iPlanetDirectoryPro的值，验证用户是否处于登录状态。
	 * 
	 * @param iPlanetDirectoryPro
	 * @return String 若用户处于登录状态则返回用户ID，否则返回空。
	 * @throws IOException
	 */
	public String Get(String iPlanetDirectoryPro) throws IOException 
	{
//		System.out.println("Calling Get aaaa...");
//		System.out.println("parameter iPlanetDirectoryPro: " + iPlanetDirectoryPro);
		String uid = null;
		if (iPlanetDirectoryPro == null || iPlanetDirectoryPro.length() <= 0) {
			return uid;
		}
		HttpURLConnection httpconn = null;
		try {
			URL myurl = new URL(SESSIONURL);
			URLConnection conn = myurl.openConnection();
			httpconn = (HttpURLConnection) conn;
			httpconn.setRequestMethod("GET");
			httpconn.addRequestProperty("iPlanetDirectoryPro",
					iPlanetDirectoryPro);
			httpconn.addRequestProperty("appUid", "publictest");
			httpconn.addRequestProperty("appPwd", "zjuinfo");
			httpconn.connect();
			uid = httpconn.getHeaderField("ZJU_SSO_UID");	
//			System.out.println("httpconn: " + httpconn.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logWriter.log("Exception in get: " + e.toString());
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
			}
		}
		return uid;

	}

	/**
	 * 根据用户ID返回用户姓名
	 * 
	 * @param String
	 * @return String 返回的是用户姓名
	 */
	private String getName(String uid) 
	{
//		System.out.println("Calling getName: uid = " + uid);
		HttpURLConnection httpconn = null;
		String name = null;
		URL myurl;
		try {
			myurl = new URL(USERURL);
			URLConnection conn = myurl.openConnection();
			httpconn = (HttpURLConnection) conn;
			httpconn.setRequestMethod("GET");
			httpconn.addRequestProperty("id", uid);
			httpconn.addRequestProperty("type", "1");
			httpconn.addRequestProperty("appUid", "publictest");
			httpconn.addRequestProperty("appPwd", "zjuinfo");
			httpconn.setDoOutput(true);
			httpconn.connect();
			BufferedReader in2 = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
			name = httpconn.getHeaderField("name");
			name = new String((name.getBytes("ISO8859-1")), "gbk");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
			}
		}
		return name;
	}
	
	/**
	 * 返回用户登录是否成功。
	 * @return
	 */
	public boolean getLoginStatus()
	{
		return m_login_status;
	}
	

}
