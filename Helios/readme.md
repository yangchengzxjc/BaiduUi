## git操作
### 把更新的代码merge到fork分支项目
- 把被fork项目添加到远程列表
```
git remote add team https://code.huilianyi.com/auto-test/helios-cn.git
```
- 拉取被fork项目更新并合并
```$xslt
git fetch team
git merge team/master
// 或者直接pull
git pull team master
```
### 提交代码到被fork分支
```$xslt
git push team master:master
```

## testNG
### 用例组织
### 数据驱动


