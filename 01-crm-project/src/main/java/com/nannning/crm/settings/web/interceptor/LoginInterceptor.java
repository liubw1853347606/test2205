package com.nannning.crm.settings.web.interceptor;

import com.nannning.crm.commons.contants.Contants;
import com.nannning.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//拦截器
public class LoginInterceptor implements HandlerInterceptor {
    @Override //在响应前拦截
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //如果用户没有登录成功,则跳转到登录页面，判断以前是否登入过，查询session有没有参数
        HttpSession session=httpServletRequest.getSession();
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        if (user==null){//如果session为空，则拦截请求，返回等登入页面
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());//重定向时，url必须加项目的名称
            return false;
        }

        return true;
    }

    @Override//在响应后拦截
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override//在返回时拦截
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
