package com.blog.utils;

/**
 * Created by yuguidong on 2019/4/29.
 */
public class Constant {

    public static String uploadDir = "D:\\opt\\apache-tomcat-8.5.31\\webapps\\upload\\";

    public static String uploadUrl = "http://localhost:10080/upload/";

    /**
     * 消息类型
     */
    public enum MsgType{
        SYSTEM,//系统消息

        POST,//评论文章消息

        COMMENT//评论你的评论消息
    }

    public static final int NORMAL_STATUS = 0;
    public static final int END_STATUS = 1;

    public static final String EDIT_HTML_MODEL= "html";
    public static final String EDIT_MD_MODEL= "Markdown";

    //签到天数
    public static final int ONE_DAY = 1;
    public static final int FIVE_DAY = 5;
    public static final int FIFTEEN_DAY = 15;
    public static final int THIRTY_DAY = 30;
    public static final int ONE_HUNDRED_DAY = 100;
    public static final int THREE_HUNDRED_AND_SIXTY_FIVE_DAY = 365;

    //积分(飞吻)数
    public static final int FIVE_POINTS = 5;
    public static final int TEN_POINTS = 10;
    public static final int FIFTEEN_POINTS = 15;
    public static final int TWENTY_POINTS = 20;
    public static final int THIRTY_POINTS = 30;
    public static final int FIFTY_POINTS = 50;
}
