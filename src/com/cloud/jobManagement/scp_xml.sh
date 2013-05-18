#!/bin/sh

scp /usr/local/tomcat/webapps/DartCloud/hadoopData/masters root@$1:/root/hadoop/hadoop-1.0.4/conf
scp /usr/local/tomcat/webapps/DartCloud/hadoopData/slaves root@$1:/root/hadoop/hadoop-1.0.4/conf
scp /usr/local/tomcat/webapps/DartCloud/hadoopData/core-site.xml root@$1:/root/hadoop/hadoop-1.0.4/conf
scp /usr/local/tomcat/webapps/DartCloud/hadoopData/hdfs-site.xml root@$1:/root/hadoop/hadoop-1.0.4/conf
scp /usr/local/tomcat/webapps/DartCloud/hadoopData/mapred-site.xml root@$1:/root/hadoop/hadoop-1.0.4/conf
scp -r /root/.ssh root@$1:/root/
