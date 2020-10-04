package com.bjpowernode.crm.settings.handler;


import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
          HttpServletRequest request= (HttpServletRequest)servletRequest;
        System.out.println(request.getRequestURI()+"   ---------------");
          if(request.getRequestURI().indexOf("workbench")>=0){
              HttpSession session= request.getSession();
              User user = (User) session.getAttribute("user");
              if (user!=null){

              }else {
                  HttpServletResponse response= (HttpServletResponse)servletResponse;
                  response.sendRedirect(request.getContextPath()+"/login.jsp");
              }
          }
          filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
