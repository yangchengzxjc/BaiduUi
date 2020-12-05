//package com.hand.utils;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.*;
//
//public class CsvData implements Iterator<Object[]> {
//
//    private BufferedReader br = null;
//    // row number
//    private int rowNum = 0;
//    // 获取次数
//    private int curRowNo = 0;
//    // column number
//    private int colNum = 0;
//    // column name
//    private String[] colName;
//    // csv all data
//    private List<String> csvAllList;
//    // csv need data
//    private List<String> csvList;
//
//    /**
//     * testNG中由@DataPrivider修饰的方法取csv时，调用此构造方法
//     *
//     * @param className  类名作为测试数据csv文件名
//     * @param methodName 方法名作为测试数据第一列
//     */
//    public CsvData(String className, String methodName) {
//        try {
//            File csv = new File("src\\test\\resources\\data\\" + className + ".csv");
//            br = new BufferedReader(new FileReader((csv)));
//            csvAllList = new ArrayList<String>();
//            while (br.ready()) {
//                csvAllList.add(br.readLine());
//                this.rowNum++;
//            }
//            String[] stringValues = csvAllList.get(0).split(",");
//            this.colNum = stringValues.length;
//            colName = new String[this.colNum];
//            for (int i = 0; i < this.colNum; i++) {
//                colName[i] = stringValue[i].toString();
//            }
//            this.curRowNo++;
//            csvList = new ArrayList<String>();
////            csvList.add(csvAllList.get(1).split(","));
//            for (int i = 1; i < rowNum; i++) {
//                String[] values = csvAllList.get(i).split(",");
//                if (methodName.equals(values[0])) {
//                    csvList.add(csvAllList.get(i));
//                }
//            }
//            // 只取一行数据
//            this.rowNum = 2;
//        } catch (Exception e) {
//            log.error("getCsvData error: {} ", e);
//        }
//    }
//
//    @Override
//    public boolean hasNext() {
//        if (this.rowNum == 0 || this.curRowNo >= this.rowNum) {
//            try {
//                br.close();
//            } catch (Exception e) {
//                log.error("close file error: {}", e);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public Object[] next() {
//        Map<String, String> strMap = new TreeMap<String, String>();
//        String[] csvCell = csvList.get(0).split(",");
//        for (int i = 0; i < this.colNum; i++) {
//            String temp = "";
//            try {
//                temp = csvCell[i].toString();
//            } catch (ArrayIndexOutOfBoundsException e) {
//                log.error(" {} ", e);
//                temp = "";
//            }
//            strMap.put(this.colName[i], temp);
//        }
//        Object[] data = new Object[1];
//        data[0] = strMap;
//        this.curRowNo++;
//        return data;
//    }
//
//    public void remove() {
//        throw new UnsupportedOperationException("remove unsupported......");
//    }
//}
