一.常用的几个Linux服务器目录：
1.tomcat：  /usr/local/tomcat/
2.openNebula: /srv/cloud/one
3.image-pool: /image-pool/
4.xen的目录:/var/log/xen/

二. 如果西溪数据中心的主服务器(目前是192.168.100.250)发生改变，将需要修改以下配置文件：
/src/conf/dataCenter.properties

三. 如果mySql数据库服务器（目前是192.168.100.250）的相关配置（如mySqL的root密码）发生变化，则需要修改以下配置文件：
/src/conf/dataBase.properties

四. 如何在系统中增加新的虚拟机操作系统类型？由于目前只支持ubuntu操作系统，若需要增加，则将配置文件设置成
/src/conf/vm_ubuntu.txt的格式，一些主要的OS类型已经在该目录下列出。
注意：千万不能该目录下的文件名，否则系统运行会出错！

五. 如何使系统能以http://dartcloud.zju.edu.cn/的形式访问？
mv $CATALINA_HOME/webapps/ROOT $CATALINA_HOME/webapps/___ROOT
ln -s $CATALINA_HOME/webapps/your_project $CATALINA_HOME/webapps/ROOT

六. 添加一个新的域时，需要修改的地方有：
1. 在数据库中添加相应域的配置信息，主要是IP；
2. flex_src/data/ZoneConfig.xml中的<zone></zone>添加一条记录；
注意该文件的<data>的值表示域的IP，一定要和数据库中的域对应起来！

