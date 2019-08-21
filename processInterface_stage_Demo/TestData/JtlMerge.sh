#!/bin/bash
# get all filename in specified path
JtlName=$2
path=*.jtl
cd $1
files=$(ls $path)
echo $(ls)
echo '<?xml version="1.0" encoding="UTF-8"?>' >> ${JtlName}
echo '<testResults version="1.2">' >> ${JtlName}
for filename in $files
do
echo ”开始合并文件=================${filename}“
countnum=`cat $filename | wc -l`
countnum=`expr $countnum - 1`
text="3,${countnum}p"
sed -n ${text} $filename   >> ${JtlName}
done
echo "</testResults>" >> ${JtlName}