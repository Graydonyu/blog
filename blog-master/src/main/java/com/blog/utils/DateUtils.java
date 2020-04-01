package com.blog.utils;

import java.util.Date;

public class DateUtils {

    public static String getDateDiff(Date date){
        long dateTimeStamp  = date.getTime();
        return getDateDiff(dateTimeStamp);
    }
    
    
    public static String getDateDiff(long dateTimeStamp){
        String result;

        long minute = 1000 * 60;
        long hour = minute * 60;
        long day = hour * 24;
        long halfamonth = day * 15;
        long month = day * 30;
        long now = new Date().getTime();
        long diffValue = now - dateTimeStamp;
        if(diffValue < 0){return "";}
        long monthC =diffValue/month;
        long weekC =diffValue/(7*day);
        long dayC =diffValue/day;
        long hourC =diffValue/hour;
        long minC =diffValue/minute;
        if(monthC>=1){
            result = monthC + "月前";
        }
        else if(weekC>=1){
            result = weekC + "周前";
        }
        else if(dayC>=1){
            result = dayC +"天前";
        }
        else if(hourC>=1){
            result = hourC +"小时前";
        }
        else if(minC>=1){
            result = minC +"分钟前";
        }else
            result="刚刚";
        return result;
    }
}
