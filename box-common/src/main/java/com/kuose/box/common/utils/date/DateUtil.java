package com.kuose.box.common.utils.date;

import java.text.SimpleDateFormat;

/**
 * @author fangyajun
 * @description
 * @since 2019/10/28
 */
public class DateUtil {

    /**
     * 将时间戳转成 yyyy-MM-dd HH:mm:ss 格式的日期字符串
     * @return
     */
    public static String timestampToStringTime(Long timestamp) {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }


//    public static void main(String[] args) {
//        Long l = 1572232160712L;
//
//        String time = timestampToStringTime(l);
//        System.out.println("time = " + time);
//    }

}
