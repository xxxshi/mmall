package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUerService")
public class UserService implements IUserService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 登陆--业务层
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        System.out.println(username);
        int resultSum = userMapper.checkUsername(username);
        if(resultSum==0){
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        // to do passwordMD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User loginUser = userMapper.selectLogin( username, md5Password);
        // loginUser.setPassword(StringUtils.EMPTY);
        if(loginUser==null){
            return ServerResponse.createByErrorMsg("密码错误！");
        }
        return ServerResponse.createBySuccess("登陆成功",loginUser);
    }

    /**
     * 注册--业务层
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMsg("用户名已存在！");
//        }
//        resultCount = userMapper.checkEmail(user.getEmail());
//        if(resultCount>0){
//            return ServerResponse.createByErrorMsg("邮箱已存在！");
//        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createByErrorMsg("注册失败！");
        }
        return ServerResponse.createSuccessByMeg("注册成功！");
    }

    /**
     * 实时校验用户名，邮箱   --业务层
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在！");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("邮箱已存在！");
                }
            }
        }else{
            return ServerResponse.createByErrorMsg("参数错误");
        }

        return ServerResponse.createSuccessByMeg("校验通过");
    }

    /**
     * 获得修改密码的问题 --业务层
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> response = checkValid(username,Const.USERNAME);
        if(response==null){
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        String resultQuestion = userMapper.selectQuestion(username);
        if(StringUtils.isNotBlank(resultQuestion)){
            return ServerResponse.createBySuccess(resultQuestion);
        }
        return ServerResponse.createByErrorMsg("问题为空");
    }

    /**
     * 检查问题的答案是否匹配 --业务层
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createByErrorMsg(forgetToken);
        }
        return ServerResponse.createByErrorMsg("验证密码错误");
    }

    /**
     * 忘记密码，修改用户密码--业务层
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        //用户名不存在
        ServerResponse<String> response = checkValid(username,Const.USERNAME);
        if(response==null){
            return ServerResponse.createByErrorMsg("用户名不存在！");
        }
        //forgetToken为空
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMsg("forgetToken为空");
        }
        //forgetToken失效或不存在
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMsg("Token 失效或不存在");
        }
        //验证信息匹配，进行密码修改
        if(StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int updateCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(updateCount==0){
                return ServerResponse.createByErrorMsg("修改密码失败");
            }else {
                return ServerResponse.createSuccessByMeg("修改密码成功");
            }
        }

        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    /**
     * 登陆时修改密码--业务层
     * @param user
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPasswordById(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createSuccessByMeg("修改密码成功");
        }
        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    /**
     * 修改用户信息--业务层
     * @param user
     * @return
     */
    @Override
    public ServerResponse<User> updateInformation(User user) {
        //验证邮箱
        int resultCount = userMapper.checkEmailById(user.getId(),user.getEmail());
        if(resultCount>0){
            return ServerResponse.createByErrorMsg("此邮箱已被使用");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerResponse.createSuccessByMeg("更新用户信息成功");
        }
        return ServerResponse.createByErrorMsg("更新用户信息失败");
    }

    /**
     * 获得用户信息--业务层
     * @param userId
     * @return
     */
    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User selectUser = userMapper.selectByPrimaryKey(userId);
        if(selectUser!=null){
            return ServerResponse.createBySuccess(selectUser);
        }

        return ServerResponse.createByErrorMsg("获得用户信息失败");
    }
}
