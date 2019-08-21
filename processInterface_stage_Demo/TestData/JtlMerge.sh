#!/bin/bash
# get all filename in specified path
path=*.jtl
files=$(ls $path)
echo '<?xml version="1.0" encoding="UTF-8"?>' >>bb.jtl
echo '<testResults version="1.2">' >>bb.jtl
for filename in $files
do
countnum=`cat $filename | wc -l`
countnum=`expr $countnum - 1`
text="3,${countnum}p"
sed -n ${text} $filename   >>bb.jtl
done
echo "</testResults>" >>bb.jtl
