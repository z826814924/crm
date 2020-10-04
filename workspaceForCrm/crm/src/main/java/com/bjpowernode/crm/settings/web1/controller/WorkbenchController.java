package com.bjpowernode.crm.settings.web1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WorkbenchController {
    @RequestMapping("/workbench/**")
    public ModelAndView service(HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView();
        String path = request.getServletPath();
        String uri= request.getRequestURI();
        System.out.println("第二个控制器中");
        System.out.println(uri);
        mv.setViewName(uri);
        return mv;
    }
}
