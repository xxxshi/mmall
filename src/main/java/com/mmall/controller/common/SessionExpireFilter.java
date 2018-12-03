package com.mmall.controller.common;

import com.mmall.common.Const;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: mmall
 * @description: 实时更新session有效期
 * @author: xxxshi
 * @create: 2018-12-03 10:12
 * @Version: 1.0
 **/

public class SessionExpireFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String loginCookId = CookieUtil.readLoginToken((HttpServletRequest) request);
        if (StringUtils.isEmpty(loginCookId)) {
            //cookId存在
            String jsonStr = RedisPoolUtil.get(loginCookId);
            User user = JsonUtil.stringToObject(jsonStr, User.class);
            if (user != null) {
                //Redis中User字符串没有过期,重置时间
                RedisPoolUtil.expire(loginCookId, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
