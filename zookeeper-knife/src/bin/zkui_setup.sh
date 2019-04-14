#!/usr/bin/env bash

:<<!
=============================================================
Install zkui: https://github.com/DeemOpen/zkui

Default web page: http://localhost:9090
!

wget -O zkui.zip https://github.com/DeemOpen/zkui/archive/master.zip
unzip zkui.zip && cd zkui-master
mvn clean package

# update `zkServer` in config.cfg, multiple zk instances are coma separated.
# eg: zkServer=server1:2181,server2:2181. First server should always be the leader.

nohup java -jar target/zkui-2.0-SNAPSHOT-jar-with-dependencies.jar &