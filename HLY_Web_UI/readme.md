# 一、启动appium服务
## 1.1 针对Android（在windows中启动）
```bash
node %HOMEPATH%\AppData\Local\Programs\Appium\resources\app\node_modules\appium\build\lib\main.js --address <ip> --port <appiumport> --bootstrap-port <bootstrap-port>
```
* ip: appium服务所在ip地址
* appiumport: appium服务的端口
* bootstrap-port: bootstrap-port的端口
## 1.2 针对IOS（在mac或者linux启动）
```bash
# 编译WebdriverAgent
xcodebuild -project /usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination id=<udid> test >/dev/null &
# 启动webkit服务
ios_webkit_debug_proxy -c <udid>:<iwdaport> >/dev/null &
# 启动appium服务
appium -a <ip> -p <appiumport> --webkit-debug-proxy-port <iwdaport> --webdriveragent-port <wdaport> >/dev/null &
```
* ip: appium服务所在ip地址
* appiumport: appium服务的端口
* iwdaport: webkit-debug-proxy服务的端口
* wdaport: 手机上启动的WebdriverAgent的端口
> 注意：每连接一个手机，上述命令都需要执行一次，并且区分开各个服务的端口
# 二、执行脚本
```python
cd zhishinet-stuAPPUI
```
## 2.1 只运行脚本，不卸载及安装以及启动appium服务
```python
cd zhishinet-stuAPPUI
python atc_StuZhiShiNet.py -a <ip> -p <port> -u <udid> -env <env> -index <index> --package <package>
```
参数解释：
* port: appium服务所在ip地址
* ip: appium服务的端口
* udid: 测试机的唯一标示
* env: 测试环境。例如：test，或者staging
* package: 安装包名称，例如：xxx.apk，或者xxx.ipa
* index: 手机序号，从1开始。
## 2.2 多手机并行，支持卸载，安装，启动appium服务，自动下载安装包
### 2.2.1 Android
```python
cd zhishinet-stuAPPUI
python start_app.py --oppo_pwd <oppo_pwd> --vivo_pwd <vivo_pwd> --install <install> --uninstall <uninstall> -env <env>
```
参数解释：
* oppo_pwd: oppo账号密码
* vivo_pwd: vivo账号密码
* install: 本次是否安装apk包，取值yes/no
* uninstall： 只有在install为yes，该参数才有效。 取值yes/no
* env: 测试环境。例如：test，或者staging
* package: 安装包名称，例如：xxx.apk，或者xxx.ipa
* index: 手机序号，从1开始。
> 注意：由于脚本通过aapt命令获取apk包的信息，所以需要将aapt命令，配置到环境变量中。通常在%ANDROID_HOME%\build-tools\XY.M.N 目录下。（XY.M.N为android api的版本）
### 2.2.2 IOS
```python
cd zhishinet-stuAPPUI
chmod +x start_ios_services.sh
./start_ios_services.sh <ip> <env> <type> <package_type>
while true; do ps -ef|grep "atc_StuZhiShiNet.py"|grep -v grep > /dev/null||break; sleep 15; done
```
* ip: appium服务器ip地址
* env: 测试环境。例如：test，或者staging
* type: 测试类型，例如stu，tec
* package_type： 安装包类型，例如ipa, apk
> 注意：IOS设备执行的时候，指定的appiumport和udid，要和启动appium服务的命令对应

# 三、备份测试报告
```python
cd zhishinet-stuAPPUI
```
## 3.1 Linux or Mac
```python
python -c "import glob, os, shutil, datetime;report_dir = 'report_backup/%s' % datetime.datetime.now().strftime('%Y.%m.%d %H%M%S');os.makedirs(report_dir, exist_ok=True);[shutil.move(f,report_dir) for f in glob.iglob('*.html')]"
```
## 3.2 windows
```python
python -c "import glob, os, shutil, datetime;report_dir = 'report_backup/%%s' %% datetime.datetime.now().strftime('%%Y.%%m.%%d %%H%%M%%S');os.makedirs(report_dir, exist_ok=True);[shutil.move(f,report_dir) for f in glob.iglob('*.html')]"
```

