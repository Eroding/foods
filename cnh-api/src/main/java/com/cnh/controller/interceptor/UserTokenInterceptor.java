package com.cnh.controller.interceptor;


import com.cnh.com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller层的拦截器，用来拦截未登陆的用户
 */
public class UserTokenInterceptor implements HandlerInterceptor {
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //  System.out.println("被拦截了");

        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)) {
            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(uniqueToken)) {
                System.out.println("请登录...");
                // returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
                return false;
            } else {
                if (!uniqueToken.equals(userToken)) {
                    System.out.println("账号在异地登录...");
                    //   returnErrorResponse(response, IMOOCJSONResult.errorMsg("账号在异地登录..."));
                    return false;
                }
            }
        } else {
            System.out.println("请登录...");
            //   returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
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
