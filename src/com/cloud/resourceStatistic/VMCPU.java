/*
 * 封装虚拟机的CPU信息
 * 包括 cpu_idle
 *    cpu_user
 *    cpu_system
 * */

package com.cloud.resourceStatistic;

public class VMCPU 
{
	public String cpu_TimeStamp;
	public double cpu_Idle = 0;
	public double cpu_User = 0;
	public double cpu_System = 0;
}
