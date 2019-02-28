package com.mmall.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 史成成 on 2019/2/27.
 * 自定义的全局异常类
 */
@Slf4j
@Component
public class ExceptionResolver implements HandlerExceptionResolver{
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.error("{} Exception",request.getRequestURI(),ex);
        ModelAndView modelAndView = new ModelAndView(new MappingJacksonJsonView()); //redis1.9
        modelAndView.addObject("status",0);
        modelAndView.addObject("msg", "接口异常，请查看服务日志");
        modelAndView.addObject("data", ex);
        return modelAndView;

    }
}
