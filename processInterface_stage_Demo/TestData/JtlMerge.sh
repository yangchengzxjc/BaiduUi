#!/bin/bash
# get all filename in specified path
path=*.jtl
files=$(ls $path)
echo '<?xml version="1.0" encoding="UTF-8"?>' >> $1.jtl
echo '<testResults version="1.2">' >> $1.jtl
for filename in $files
do
countnum=`cat $filename | wc -l`
countnum=`expr $countnum - 1`
text="3,${countnum}p"
sed -n ${text} $filename   >> $1.jtl
done
echo "</testResults>" >> $1.jtl
