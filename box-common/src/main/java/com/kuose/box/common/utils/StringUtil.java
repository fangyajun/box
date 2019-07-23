package com.kuose.box.common.utils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static BigDecimal objToDecimal(Object obj) {
        return new BigDecimal(StringUtil.objToString(obj));
    }

    public static String objToString(Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    /**
     * true 空,false 不空.
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static Integer objToInteger(Object str) {
        Integer result = 0;
        try {
            result = Integer.parseInt(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long objToLong(Object str) {
        Long result = 0L;
        try {
            result = Long.parseLong(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static float objToFloat(Object str) {
        float result = 0F;
        try {
            result = Float.parseFloat(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Double objToDouble(Object str) {
        Double result = 0D;
        try {
            result = Double.parseDouble(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去掉换行符
     */
    public static String replaceBlank(String str) {
        if (str == null) {
            return "";
        }
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static String inputStreamToString(InputStream inputStream) {
        try (InputStream stringStream = inputStream) {
            byte[] bytes = new byte[stringStream.available()];
            stringStream.read(bytes);
            return new String(bytes, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
