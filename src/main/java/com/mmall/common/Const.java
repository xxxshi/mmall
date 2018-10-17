package com.mmall.common;

/**
 * @program: mmall
 * @description: 常量类
 * @author: xxxshi
 * @create: 2018-10-15 20:08
 * @Version: 1.0
 **/

public class Const {
    public static final String CURRENT_USER ="currentuser";
    public static final String USERNAME ="username";
    public static final String EMAIL ="email";

    public interface Role{
        int ROLE_CUSTOMER = 0;
        //普通用户
        int ROLE_ADMIN = 1;
        //管理员
    }
}
