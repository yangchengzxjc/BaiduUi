package com.hand.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author peng.zhang
 * @Date 2020/8/14
 * @Version 1.0
 **/
@Slf4j
public class DocumnetUtil {

    /**
     * 读取文件
     * @param fileName   文件名称
     * @param dataLenth  需要获取的数据的行数
     * @return
     */
    public static ArrayList fileReader(String fileName, int dataLenth) {

        FileReader reader = null;
        BufferedReader input=null;
        int i=0;
        ArrayList<String> result = new ArrayList<>();
        //先判断读取的文件是否存在
        File file =new File(fileName);
        if(file.exists()){
            try {
                reader = new FileReader(fileName);
                input =new BufferedReader(reader);
                String text=input.readLine();
                if(dataLenth>=0){
                    while (text!=null){
                        i++;
                        if(i<=dataLenth){
                            result.add(text);
                        }
                        text =input.readLine();
                    }
                }else {
                    throw new RuntimeException("请输入有效的数据行数");
                }
                if(i<dataLenth){
                    throw new RuntimeException("所需的的数据已经超过数据总数");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(reader!=null){
                        reader.close();
                    }
                    if(input!=null){
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new RuntimeException("文件不存在");
        }
        return result;
    }

    /**
     * 读取文件  重载的方法   直接读取整个文件内容
     * @param fileName   文件名称
     * @return
     */
    public static String fileReader(String fileName){

        FileReader reader = null;
        BufferedReader input=null;
        //单线程下这里是应用StringBuilder 效率更高
        StringBuilder result = new StringBuilder();
        //先判断读取的文件是否存在
        File file =new File(fileName);
        if(file.exists()){
            try {
                reader = new FileReader(fileName);
                input =new BufferedReader(reader);
                String text = input.readLine();
                while (text!=null){
                    result.append(text);
                    text =input.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(reader!=null){
                        reader.close();
                    }
                    if(input!=null){
                        input.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            throw new RuntimeException("文件不存在");
        }
        return result.toString();
    }

    /**
     * 写文件操作
     * @param fileName
     * @param data   需要写的数据
     */
    public static void writeFile(String fileName,StringBuilder data){
        File file =new File(fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data.toString());
            bw.flush();
            fw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件删除
     * @param fileName
     */
    public static void deleteFile(String fileName){
        File file =new File(fileName);
        if(file.exists()){
            file.delete();
        }
    }


}
