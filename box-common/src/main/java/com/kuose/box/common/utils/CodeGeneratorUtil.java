package com.kuose.box.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CodeGeneratorUtil {

    /**
     * 商品编码生成(由编号前缀+6位日期+时间戳后4位+4位随机数组成)
     * @return
     */
    public static String getGoodsCode(String prefix){
        if (prefix.length() < 3) {
            prefix = prefix + "IFEC".substring(0,3 - prefix.length());
        } else {
            prefix = prefix.substring(0,3);
        }
        //格式化日期为"yymmdd"
        DateFormat format = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix.toUpperCase());
        buffer.append(format.format(date));
        buffer.append((date.getTime() + "").substring(9));
        buffer.append(getRandNum(4));
        return buffer.toString();
    }

    /**
     * sku编码生成(由编号前缀+6位日期+时间戳后4位+4位随机数组成)
     * @return
     */
    public static String getSkuCode(String prefix){
        //格式化日期为"yymmdd"
        DateFormat format = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        buffer.append(format.format(date));
        buffer.append((date.getTime() + "").substring(9));
        buffer.append(getRandNum(4));
        return buffer.toString();
    }

    /**
     * 订单编码生成(当前系统时间戳精确到毫秒+三位随机数)
     * @return
     */
    public static String getOrderCode(){
        //当前系统时间戳精确到毫秒
        Long simple=System.currentTimeMillis();
        //三位随机数
        int random=new Random().nextInt(900)+100;//为变量赋随机值100-999;
        return simple.toString()+random;
    }

    /**
     * 随机数
     * @param leng  随机数长度
     * @return
     */
    private static String getRandNum(int leng){
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < leng; i++) {
            result.append(random.nextInt(10));
        }
        if(result.length()>0){
            return result.toString();
        }
        return "";
    }
}
