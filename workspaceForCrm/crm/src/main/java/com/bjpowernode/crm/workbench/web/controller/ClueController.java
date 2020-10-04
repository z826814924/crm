package com.bjpowernode.crm.workbench.web.controller;



import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping("/clues/activity")
public class ClueController {


    @Autowired
    private DicService ds;
    private  @Autowired
    UserService us;
    @Autowired
    private ClueService cs;
    @Autowired
    private ActivityService as;

    @ResponseBody
    @RequestMapping("/getUserList.do")
    public List<User> service1(){

        System.out.println("取得用户信息列表");
        List<User> uList= us.getUserList();
        return uList;



    }



    @ResponseBody
    @RequestMapping("/save.do")
    public Map<String,Object> service2(HttpServletRequest request,Clue c){

        System.out.println("进入到存储线索信息列表");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();
        c.setId(id);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
         boolean flag= cs.save(c);
        Map<String,Object> map= (Map<String, Object>)request.getAttribute("map");
        map.put("success",flag);
        return map;




    }





    @RequestMapping("/detail.do")
    public ModelAndView service3(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        System.out.println("跳转到线索详细信息页");
        String id= request.getParameter("id");
        Clue c = cs.detail(id);
        mv.addObject("c",c);
        mv.setViewName("/workbench/clue/detail.jsp");
        return mv;



    }

    @ResponseBody
    @RequestMapping("/getActivityListByClueId.do")
    public List<Activity> service4(String clueId){

        System.out.println("根据线索id查询关联的市场活动列表");
        List<Activity> aList= as.getActivityListByClueId(clueId);

        return aList;



    }

    @ResponseBody
    @RequestMapping("/unbund.do")
    public Map service5(HttpServletRequest request,String id){

        System.out.println("执行解除关联操作");
         boolean flag= cs.unbund(id);
        Map map= (Map) request.getAttribute("map");
        map.put("sucess",flag);


        return map;



    }

    @ResponseBody
    @RequestMapping("/getActivityListByNameAndNotByClueId.do")
    public List<Activity> service6(String aname,String clueId){

        System.out.println("查询市场活动列表(根据名称模糊查询+排除掉已经关联指定的线索表)");
        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> aList= as.getActivityListByNameAndNotByClueId(map);

        return aList;



    }


    @ResponseBody
    @RequestMapping("/bund.do")
    public Map service7(HttpServletRequest request,String cid,String[] aid){
        System.out.println("进入绑定关联市场活动操作");

           boolean flag=  cs.bund(cid,aid);


        Map map= (Map) request.getAttribute("map");
        map.put("success",flag);
        return map;



    }


    @ResponseBody
    @RequestMapping("/getActivityListByName.do")
    public List<Activity> service8(HttpServletRequest request,String aname){
        System.out.println("进入查询市场活动列表操作(根据名称模糊查)");

        List<Activity> aList= as.getActivityListByName(aname);

        return aList;



    }



    @RequestMapping("/convert.do")
    public ModelAndView service9(HttpServletRequest request,String clueId,Tran t,String flag){
        System.out.println("执行线索转换的操作");
        ModelAndView mv =null;
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        //接收是否需要创建交易的标记
        if ("a".equals(flag)){


            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

            //接受交易表单中的参数


        }else {

            t=null;
        }
        /*

            为业务层传递的参数
            1,必须传递的参数clueId,有了这个clueId之后我们才知道要转换那条记录
            2,必须传递的参数t,因为在线索转换的过程中,有可能会临时创建一笔交易（业务层接收的t也有可能是个null）


         */


         boolean flag1 = cs.convert(clueId,t,createBy);


        if (flag1){

          mv = new ModelAndView("redirect:/workbench/clue/index.jsp");

        }

        return mv;

    }

}


