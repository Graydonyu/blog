package com.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.blog.entity.core.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Getter
@Setter
@ToString
@TableName("user")
public class User extends Entity {

        /**
     * 昵称
     */
         private String username;

        /**
     * 密码
     */
         private String password;

        /**
     * 邮件
     */
         private String email;

        /**
     * 手机电话
     */
         private String mobile;

        /**
     * 积分
     */
         private Integer point;

        /**
     * 性别
     */
         private String gender;

        /**
     * 微信号
     */
         private String wechat;

        /**
     * 生日
     */
         private LocalDateTime birthday;

        /**
     * 头像
     */
         private String avatar;

        /**
     * 内容数量
     */
         private Integer postCount;

        /**
     * 评论数量
     */
         private Integer commentCount;

        /**
     * 状态
     */
         private Integer status;

        /**
     * 最后的登陆时间
     */
         private Date lasted;
}
