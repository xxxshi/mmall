package com.mmall.controller.common.interceptor;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardedRedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.output.StringBuilderWriter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 史成成 on 2019/2/27.
 * 自定义拦截器类
 */
@Slf4j
public class AuthrityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();
        //获得请求信息
        StringBuffer requestParam = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapValue = entry.getValue().toString();
        }

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if(org.apache.commons.lang.StringUtils.isNotBlank(loginToken)){
            String userJsonStr = ShardedRedisPoolUtil.get(loginToken);
            user = JsonUtil.stringToObject(userJsonStr,User.class);
        }

        if (user == null || (user.getRole() != Const.Role.ROLE_ADMIN)) {
            //无权限，return false不执行Controller
            //重置response
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            if (user == null) {
                //除了mvc的xml里面配置外，另一种从拦截器里过滤某个接口方法
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtextImgUpload")) {
                    Map returnMap = new HashMap();
                    returnMap.put("success", false);
                    returnMap.put("msg", "请登陆管理员");
                    out.print(JsonUtil.objectToString(returnMap));
                }else{
                    out.print(JsonUtil.objectToString(ServerResponse.createByErrorMsg("用户未登录")));
                }
            }else {
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtextImgUpload")) {
                    Map returnMap = new HashMap();
                    returnMap.put("success", false);
                    returnMap.put("msg", "该用户无权限");
                    out.print(JsonUtil.objectToString(returnMap));
                }else{
                    out.print(JsonUtil.objectToString(ServerResponse.createByErrorMsg("该用户无权限")));
                }
            }
            out.flush();
            out.close();
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
