package com.hand.baseMethod;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public  class Base {
    private   static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public String GetUUid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public JsonObject jsonObject;
//    public JSONObject jsonObject;


    /**
     * 构造方法
     */
    public Base() {

    }

    /**
     * @param obj
     * @param jsonParth
     * @return
     */
    public List<String> JsonExtractor(Object obj, String jsonParth) {
        String str = obj.toString();
        List<String> titless = null;
        try {
            titless = JsonPath.read(str, jsonParth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titless;
    }

    /**
     * 获得文件指定行数的内容
     * @param path
     * @param number
     * @return
     */
    public static String  getFileLineContent(String path, int number) {
        String content = null;
        File file = new File(path);
        try {
            FileReader fileReader = new FileReader(file);
            LineNumberReader reader = new LineNumberReader(fileReader);
            String txt = "";
            int lines = 0;
            while (txt != null) {
                lines++;
                txt = reader.readLine();
                if (lines == number) {
//                    System.out.println("第" + reader.getLineNumber() + "的内容是：" + txt + "\n");
                    content=txt;
//                    System.exit(0);
                }
            }
            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    /**
     * 获得文件行数
     * @param path
     * @return
     * @throws IOException
     */
    public static int  getFileRows(String path)  throws IOException {
        File file = new File(path);
        int count=0;
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(fis);
        while(scanner.hasNextLine()){
            scanner.nextLine();
            count++;
        }
        return  count;
    }


    /**
     * 获得文件指定个数的随机行数内容
     * @param path
     * @param num
     * @return
     * @throws IOException
     */
    public static List<String> getTextRandomContent(String path, int num)  throws IOException {
        int a[]=randomArray(1,getFileRows(path),num);
        List<String> count=new ArrayList<String>();
        for (int k=0;k<a.length;k++)
            count.add(getFileLineContent(path,a[k]));
        return  count;
    }

    /**
     * 获取指定行的文件内容
     * @param path
     * @param num
     * @return
     * @throws IOException
     */
    public static List<String> getTextSpecifyContent(String path, int  num)  throws IOException {
        List<String> count=new ArrayList<String>();
        count.add(getFileLineContent(path,num));
        return  count;
    }


    /**
     * 获得整个文件的全部内容
     * @param path
     * @return
     * @throws IOException
     */
    public static List<String> getTextAllContent(String path)  throws IOException {
        List<String> count=new ArrayList<String>();
        int lines=getFileRows(path);
        log.info("文件总行数:"+lines);
        for (int k=1;k<=lines;k++)
            count.add(getFileLineContent(path,k));
        return  count;
    }



    /**
     * 随机指定范围内N个不重复的数
     * 在初始化的无重复待选数组中随机产生一个数放入结果中，
     * 将待选数组被随机到的数，用待选数组(len-1)下标对应的数替换
     * 然后从len-2里随机产生下一个随机数，如此类推
     * @param max  指定范围最大值
     * @param min  指定范围最小值
     * @param n  随机数个数
     * @return int[] 随机数结果集
     */
    public static int[] randomArray(int min,int max,int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }



    /**
     * @param obj,jsonParth
     * @return
     */


    /**
     * 获取UTC时间
     * yyyy-MM-dd'T'HH:mm:ss'Z'
     *
     * @param day 便宜量
     * @return
     */
    public String getUTCDate(int day) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.HOUR_OF_DAY, day);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String localTime = formatter.format(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = null;
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis = localDate.getTime();
/** long时间转换成Calendar */
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(localTimeInMillis);
/** 取得时间偏移量 */
        int zoneOffset = calendar2.get(java.util.Calendar.ZONE_OFFSET);
/** 取得夏令时差 */
        int dstOffset = calendar2.get(java.util.Calendar.DST_OFFSET);
/** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar2.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
/** 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar2.getTimeInMillis());
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startDate = formatter2.format(utcDate);
        return startDate;
    }

    /**
     *
     * @param day
     * @return
     */

    public static String getUTCDate(int day, int Hour) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,day);
        date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        String localTime = formatter.format(date)+String.format("%d:00:00",Hour);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = null;
        try {
            localDate = sdf.parse(localTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long localTimeInMillis = localDate.getTime();
/** long时间转换成Calendar */
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(localTimeInMillis);
/** 取得时间偏移量 */
        int zoneOffset = calendar2.get(java.util.Calendar.ZONE_OFFSET);
/** 取得夏令时差 */
        int dstOffset = calendar2.get(java.util.Calendar.DST_OFFSET);
/** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar2.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
/** 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar2.getTimeInMillis());
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String startDate = formatter2.format(utcDate);
        return startDate;
    }
    /**
     * 返回日期 yyyy-MM-dd HH:mm:ss
     *
     * @param day
     * @return
     */
    public String getDate(int day, int type) {
        Date date = new Date();
        String localTime = "";

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        if (type == 1) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            localTime = formatter.format(date);
        }
        if (type == 2) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            localTime = formatter.format(date);
        }
        return localTime;
    }

    /**
     * 正则提取器
     *
     * @param key     输入要提取的对象的key
     * @param jsonStr json转化为String
     */
    public static List<String> regularExpressionExtractor(String key, String jsonStr) {
        List<String> regularList = new ArrayList<String>();
        try {
            String regex = "" + key + "\":(\"(.*?)\"|(\\d*))";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(jsonStr);
            while (matcher.find()) {
                if (StringUtils.isNotEmpty(matcher.group().trim())) {
                    String value = matcher.group().split("\\:")[1].replace("\"", "").trim();
                    regularList.add(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return regularList;
    }

    /**
     * 算消费税
     * @param decimalHandlingMethod
     * @param basicRate
     * @param expenseAmount
     * @return
     */
    public JsonObject taxCalculation(String decimalHandlingMethod, int basicRate, int expenseAmount) {
        List<Integer> ConsumptionData = new ArrayList<Integer>();
        JsonObject taxobj = new JsonObject();

        //消费税税率
        double consumptionBasicRate = Double.parseDouble(String.valueOf(basicRate));
        //消费税税额
        double consumptionTaxAmount = 0.0;
        consumptionTaxAmount = expenseAmount * (consumptionBasicRate * 0.01 / (1 + consumptionBasicRate * 0.01));
        //截尾法
        if (decimalHandlingMethod.equals("0")) {
            consumptionTaxAmount = (double) Math.floor(consumptionTaxAmount);
            //四舍五入法
        } else if (decimalHandlingMethod.equals("1")) {
            consumptionTaxAmount = (double) Math.round(consumptionTaxAmount);
            //进位法
        } else if (decimalHandlingMethod.equals("2")) {
            consumptionTaxAmount = (double) Math.ceil(consumptionTaxAmount);
        }
        //消费税税后额
        int consumptionAfterTaxAmount = (int) (expenseAmount - consumptionTaxAmount);
        ConsumptionData.add((int) consumptionTaxAmount);
        ConsumptionData.add((int) consumptionAfterTaxAmount);
        taxobj.addProperty("tax", (int)consumptionTaxAmount);
        taxobj.addProperty("excludingtax", (int)consumptionAfterTaxAmount);
        taxobj.addProperty("expenseAmount", expenseAmount);
        return taxobj;

    }
}
