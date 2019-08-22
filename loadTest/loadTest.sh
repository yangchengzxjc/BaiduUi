#!/bin/bash
echo "我要开始执行shell脚本了"
time=`date +%Y%m%d%H%M%S`
java -version
/opt/jmeter/apache-jmeter-3.2/bin/jmeter -n -t 0806.jmx -l jtl/HLY-loadTest-$time.jtl -e -o report/HLY-loadTest-$time
cd report
zip -r  HLY-PerformanceTestingReport-$time.zip   HLY-loadTest-$time
echo "压缩文件成功"