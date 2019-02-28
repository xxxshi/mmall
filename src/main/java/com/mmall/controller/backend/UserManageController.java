package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardedRedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: mmall
 * @description: 后台管理的控制器
 * @author: xxxshi
 * @create: 2018-10-16 15:24
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {
    @Autowired
    private IUserService iUserService;

    /**
     * 后台管理登陆--控制层
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping( value = "login.do")
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username,password);

        if(response.isSuccess()){
            User user = response.getData();
            if(Const.Role.ROLE_ADMIN == user.getRole()){
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                ShardedRedisPoolUtil.setEx(session.getId(), JsonUtil.objectToString(response.getData()),Const.RedisCacheExtime
                        .REDIS_SESSION_EXTIME);
                return response;
            }else{
                return ServerResponse.createByErrorMsg("不是管理员，无法登陆");
            }
        }

        return response;
    }
}
