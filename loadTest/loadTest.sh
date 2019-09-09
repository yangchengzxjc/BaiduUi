echo "我要开始执行shell脚本了"
time=$1
scriptName=$2
TestEnv=$7
/opt/jmeter/apache-jmeter-3.2/bin/jmeter.sh -n -t $TestEnv/$scriptName -l $TestEnv/jtl/HLY-loadTest-$time.jtl -e -o $TestEnv/LoadTestReport/HLY-loadTest-$time
cd $TestEnv/LoadTestReport
zip -r  HLY-PerformanceTestingReport-$time.zip   HLY-loadTest-$time
echo "压缩文件成功"
