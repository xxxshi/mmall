package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import org.springframework.stereotype.Service;



public interface IUserService {
    /**
     * 登陆--业务层
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);


    /**
     * 注册--业务层
     * @param user
     * @return
     */
    ServerResponse<String> register(User user);

    /**
     * 实时校验用户名，邮箱--业务层
     * @param str
     * @param type
     * @return
     */
    ServerResponse<String> checkValid(String str,String type);

    /**
     * 获得修改密码的问题 -业务层-
     * @param username
     * @return
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 检查问题的答案是否匹配 --业务层
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<String> checkAnswer(String username,String question,String answer);

    /**
     * 忘记密码，修改用户密码--业务层
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    /**
     * 登陆时修改密码--业务层
     * @param user
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    /**
     * 修改用户信息--业务层
     * @param user
     * @return
     */
    ServerResponse<User> updateInformation(User user);

    /**
     * 获得用户信息--业务层
     * @param userId
     * @return
     */
    ServerResponse<User> getInformation(Integer userId);

}
