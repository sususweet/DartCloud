package com.cloud.resourceMonitor;

public class VMInfo{

    public double cpu_used;
    public double cpu_user;
    public double cpu_system;
    public double mem_used;
    public String ip;
    public String hostname;
    
    public VMInfo() {
        
    }
    
    public VMInfo(String hostname, String ip, double cpu_used, double cpu_user, double cpu_system, double mem_used) {
    	this.hostname = hostname;
    	this.ip = ip;
        this.cpu_used = cpu_used;
        this.cpu_user = cpu_user;
        this.cpu_system = cpu_system;
        this.mem_used = mem_used;
    }
   
}