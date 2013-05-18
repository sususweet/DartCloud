package com.cloud;

//import org.apache.log4j.Logger;

public class Foo {

//	private static final Logger log = Logger.getLogger(Foo.class);  
	  
    String name="failure";  
  
    String age="500";  
  
    public void init(String name, String age)  
    {  
        this.name=name;  
        this.age=age;  
        System.out.println(this.name+"---"+this.age);  
    }  
    public String getName()  
    {  
        System.out.println("getName:"+name);  
        return name;  
    }  
  
    public void setName(String name)  
    {  
        this.name = name;  
    }  
  
    public String getAge()  
    {  
        System.out.println("getAge:"+age);  
        return age;  
    }  
  
    public void setAge(String age)  
    {  
        this.age = age;  
    }  
}
