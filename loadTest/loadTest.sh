#!/bin/bash -ilex
echo "我要开始执行shell脚本了"
time=$1
scriptName=$2
protocol=$3
serverUrl=$4
durationTime=$5
concurrentNumber=$6
TestEnv=$7


/opt/jmeter/apache-jmeter-3.2/bin/jmeter.sh -n -t $TestEnv/$scriptName -Jprotocol=$protocol -JserverUrl=$serverUrl -JdurationTime=$durationTime -JconcurrentNumber=$concurrentNumber -l $TestEnv/jtl/HLY-loadTest-$time.jtl -e -o $TestEnv/LoadTestReport/HLY-loadTest-$time


cd $TestEnv/LoadTestReport
zip -r -q  HLY-PerformanceTestingReport-$time.zip   HLY-loadTest-$time
echo "压缩文件成功"
