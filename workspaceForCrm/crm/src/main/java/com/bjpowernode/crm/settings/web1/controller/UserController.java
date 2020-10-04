package com.bjpowernode.crm.settings.web1.controller;

import com.bjpowernode.crm.exception.DengLuException;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;

import com.bjpowernode.crm.exception.LoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Autowired
    DicService ds;

    @Autowired
    private UserService service;

    @ResponseBody
    @RequestMapping("/login.do")
    public Map service(HttpServletRequest request, HttpServletResponse response, String loginAct, String loginPwd) throws LoginException, ServletException, IOException, DengLuException {
        if (request.getServletContext().getAttribute("appellationList")==null) {
            System.out.println("服务器处理缓存字典开始");
            ServletContext application = request.getServletContext();
            Map<String, List<DicValue>> map = ds.getAll();
            //将map解析为上下文域对象中保存的键值对
            Set<String> set = map.keySet();
            for (String key : set
            ) {
                application.setAttribute(key, map.get(key));
            }

            System.out.println("服务器处理缓存字典结束");
        }

        ModelAndView mv = new ModelAndView();
        System.out.println("进入到用户控制器");
        Map<String, Object> map1 = (Map<String, Object>) request.getAttribute("map");
        //将密码的明文形式转换为MD5的密文形式
 /*
        if (session.getAttribute("user") != null) {
            System.out.println("有session");
            response.sendRedirect("/workbench/index.jsp");
        }*/
        HttpSession session = request.getSession();
        if (session.getAttribute("user")!=null){
            System.out.println("session不为空跳转");
            map1.put("success", true);
            map1.put("msg", "登陆成功");
            return map1;
        }
        System.out.println("没有session");
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("-------ip地址:" + ip);
        User user = service.login(loginAct, loginPwd, ip);


        map1.put("success", true);
        map1.put("msg", "登陆成功");
        request.getSession().setAttribute("user", user);


        // mv.setViewName("index");
        return map1;

    }


    @RequestMapping("/*.do")
    public ModelAndView service1(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        System.out.println("进入到用户控制器,不是xxx.do的地址");
        String path = request.getServletPath();
        if ("/settings/user/xxx.do".equals(path)) {


        } else {

        }
        return mv;
    }



}

