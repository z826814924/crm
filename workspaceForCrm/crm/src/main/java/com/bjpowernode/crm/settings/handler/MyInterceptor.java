package com.bjpowernode.crm.settings.handler;

import com.bjpowernode.crm.settings.domain.User;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

//拦截器类，拦截用户的请求
public class MyInterceptor implements HandlerInterceptor {
    private Map<String,Object> map = new HashMap<>();

    //验证用户的登录信息，正确return true,其他return false
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入到验证登陆操作111");
        request.setAttribute("map",map);
        String uri =request.getRequestURI();
        System.out.println(uri);
        HttpSession session =request.getSession();
        if (uri.indexOf("/login")>=0 ||
            uri.indexOf("/getUserList")>=0 ||
            uri.indexOf("/save")>=0 ||
            uri.indexOf("/pageList")>=0 ||
            uri.indexOf("/delete")>=0 ||
            uri.indexOf("/getUserListAndActivity")>=0 ||
            uri.indexOf("/update")>=0 ||
            uri.indexOf("/detail")>=0 ||
            uri.indexOf("/getRemarkListByAid")>=0 ||
            uri.indexOf("/deleteRemark")>=0 ||
            uri.indexOf("/saveRemark")>=0 ||
            uri.indexOf("/updateRemark")>=0 ||
            uri.indexOf("/getActivityListByClueId")>=0 ||
            uri.indexOf("/unbund")>=0 ||
            uri.indexOf("/getActivityListByName")>=0 ||
            uri.indexOf("/bund")>=0 ||
            uri.indexOf("jsp")>=0 ||
            uri.indexOf("/convert")>=0 ||
            uri.indexOf("/transaction")>=0)

        {

            return true;
        }


            if (session.getAttribute("user")!=null){
                System.out.println("session不为空跳转111");
                response.sendRedirect(request.getContextPath()+"/workbench/index.jsp");
                System.out.println("跳转执行后");
                return false;
        }
        System.out.println("session为空");
            response.sendRedirect(request.getContextPath()+"/login.jsp");
            return  false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
