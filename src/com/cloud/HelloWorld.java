package com.cloud;

import com.cloud.utilFun.LogWriter;
public class HelloWorld {

	public static void main(String args[])
	{
		String str = "this a test!";
		str = str.replaceAll("this", "thissdfsd");
		System.out.println(str);
	}
}
