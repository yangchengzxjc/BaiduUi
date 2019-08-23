echo "我要开始执行shell脚本了"
time=$1
scriptName=$2
java -version
/opt/jmeter/apache-jmeter-3.2/bin/jmeter.sh -n -t $scriptName -l jtl/HLY-loadTest-$time.jtl -e -o LoadTestReport/HLY-loadTest-$time
cd LoadTestReport
zip -r  HLY-PerformanceTestingReport-$time.zip   HLY-loadTest-$time
echo "压缩文件成功"
