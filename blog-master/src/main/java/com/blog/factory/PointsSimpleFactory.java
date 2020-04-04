package com.blog.factory;

import com.blog.utils.Constant;

public class PointsSimpleFactory {

    /**
     * @Author graydon
     * @Date 2020-04-04 22:16
     * @Description 使用简单工厂模式获取对应积分
     **/
    public static int getPointsBySigninDays(Integer days){
        if(days < Constant.FIVE_DAY){
            return Constant.FIVE_POINTS;
        }else if(days >= Constant.FIVE_DAY && days < Constant.FIFTEEN_DAY){
            return Constant.TEN_POINTS;
        }else if(days >= Constant.FIFTEEN_DAY && days < Constant.THIRTY_DAY){
            return Constant.FIFTEEN_POINTS;
        }else if(days >= Constant.THIRTY_DAY && days < Constant.ONE_HUNDRED_DAY){
            return Constant.TWENTY_POINTS;
        }else if(days >= Constant.ONE_HUNDRED_DAY && days < Constant.THREE_HUNDRED_AND_SIXTY_FIVE_DAY){
            return Constant.THIRTY_POINTS;
        }else {
            return Constant.FIFTY_POINTS;
        }
    }
}
