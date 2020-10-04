package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.exception.ActivityDeleteException;
import com.bjpowernode.crm.exception.DeleteRemarkException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.ActivitySearch;
import com.bjpowernode.crm.workbench.service.ActivityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/work/activity")
public class ActivityController {

    @Autowired
    private UserService us;
    @Autowired
    private ActivityService as;
    @Autowired
    HttpServletRequest rs;


    @ResponseBody
    @RequestMapping("/getUserList.do")
    public List<User> service(HttpServletRequest request, HttpServletResponse response, String loginAct, String loginPwd) throws ServletException, IOException {
        System.out.println("取得用户信息列表");
        List<User> ulist = us.getUserList();
        return ulist;


    }

    @ResponseBody
    @RequestMapping("/save.do")
    public Map service1(HttpServletRequest request, HttpServletResponse response, Activity activity) {
        System.out.println("执行市场活动的添加操作");
         String id = UUIDUtil.getUUID();
/*         String owner = activity.getOwner();
         String name = activity.getName();
         String startDate = activity.getStartDate();
         String endDate = activity.getEndDate();
         String cost = activity.getCost();
         String description = activity.getDescription();*/
         //创建时间,当前的系统时间
         String createTime = DateTimeUtil.getSysTime();
         //创建人：当前登陆的用户
         String createBy = ((User) request.getSession().getAttribute("user")).getName();
         activity.setId(id);
         activity.setCreateTime(createTime);
         activity.setCreateBy(createBy);
        boolean flag= as.save(activity);
        System.out.println(flag);
        Map map= (Map) request.getAttribute("map");
        map.put("success",flag);
        return map;

    }
    @ResponseBody
    @RequestMapping("/pageList.do")
    public PaginationVO<Activity> service2(HttpServletRequest request, HttpServletResponse response, ActivitySearch activitySearch){
        System.out.println("进入到查询市场活动信息列表中的操作(结合条件查询+分页查询)");
     /*
        计算出略过的记录数,pagehelp不用
        前端需要：市场活动信息列表  查询的总条数
        业务层拿到了以上两项信息之后,如何做返回呢
        map
        map.put("dataList" : dataList)
        map.put("total" : total)

        vo
        PaginationVO<T>
        private int total;
        private list<T> dataList;

        paginationVO
               private  int total;
               private List<T> dataList;

               将来分页查询,每个模块都有,所以我们选择使用一个通用vo,操作起来比较方便

               PaginationVO<Activity> vo = new paginationVO<>;
                vo.setTotal(total);
                vo.setDataList(dataList);
                return vo
        */

         PaginationVO<Activity> vo = as.pageList(activitySearch);
         return  vo;


    }

    @ResponseBody
    @RequestMapping("/delete.do")
    public Map service3(HttpServletRequest request, HttpServletResponse response) throws ActivityDeleteException {
        System.out.println("执行市场活动的删除操作");
        String[] ids = request.getParameterValues("id");
        boolean flag=  as.delete(ids);
        Map map = (Map) request.getAttribute("map");
        map.put("success",flag);
        return  map;



    }

    @ResponseBody
    @RequestMapping("/getUserListAndActivity.do")
    public Map service4(HttpServletRequest request, HttpServletResponse response,String id) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");

        /*
        总结:
        controller调用service的方法,返回值应该是是什么
        你得想一想前端要什么,就要从service层取什么

        前端需要的,管业务层取要

       uList
       a

      以上两项信息,复用率不高,我们选择用map打包两项信息即可
       map
        */

        Map<String,Object> map = as.getUserListAndActivity(id);

        return  map;



    }



    @ResponseBody
    @RequestMapping("/update.do")
    public Map service5(HttpServletRequest request, HttpServletResponse response, Activity activity) {
        System.out.println("执行市场活动修改操作");

        String id = activity.getId();
/*         String owner = activity.getOwner();
         String name = activity.getName();
         String startDate = activity.getStartDate();
         String endDate = activity.getEndDate();
         String cost = activity.getCost();
         String description = activity.getDescription();*/
        //修改时间,当前的系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登陆的用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        activity.setId(id);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);
        boolean flag= as.update(activity);
        System.out.println(flag);
        Map map= (Map) request.getAttribute("map");
        map.put("success",flag);
        return map;


    }


    @RequestMapping("/detail.do")
    public ModelAndView service6(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("进入到跳转详细信息页的操作");
        String id = request.getParameter("id");
        ModelAndView mv = new ModelAndView();
        Activity a= as.detail(id);
        mv.addObject("a",a);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;

    }
    @ResponseBody
    @RequestMapping("/getRemarkListByAid.do")
    public List<ActivityRemark> service7(HttpServletRequest request,HttpServletResponse response,String activityId) {

        System.out.println("根据市场活动id,取得备注信息列表");

        List<ActivityRemark> arList= as.getRemarkListByAid(activityId);
        System.out.println(arList.size());
        return  arList;

    }


    @ResponseBody
    @RequestMapping("/deleteRemark.do")
    public Map<String,Object> service8(HttpServletRequest request,HttpServletResponse response,String id) throws DeleteRemarkException {

        System.out.println("删除备注操作");
        boolean flag= as.deleteRemark(id);

        Map<String,Object> map= (Map<String,Object>) request.getAttribute("map");
        map.put("success",flag);


        return  map;

    }

    @ResponseBody
    @RequestMapping("/saveRemark.do")
    public Map<String,Object> service9(HttpServletRequest request,HttpServletResponse response,ActivityRemark ar) throws DeleteRemarkException {

        System.out.println("执行添加备注的操作");
       String id = UUIDUtil.getUUID();
//创建时间,当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
//创建人：当前登陆的用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
       ar.setId(id);
       ar.setCreateBy(createBy);
       ar.setCreateTime(createTime);
       ar.setEditFlag(editFlag);

       boolean flag = as.saveRemark(ar);
        Map<String,Object> map= (Map<String,Object>) request.getAttribute("map");
        map.put("success",flag);
        map.put("ar",ar);


        return  map;

    }


    @ResponseBody
    @RequestMapping("/updateRemark.do")
    public Map<String,Object> service10(HttpServletRequest request,HttpServletResponse response,ActivityRemark ar) throws DeleteRemarkException {

        System.out.println("执行修改备注的操作");
//创建时间,当前的系统时间
        String editTime = DateTimeUtil.getSysTime();
//创建人：当前登陆的用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);

        boolean flag = as.updateRemark(ar);
        Map<String,Object> map= (Map<String,Object>) request.getAttribute("map");
        map.put("success",flag);
        map.put("ar",ar);


        return  map;

    }
}


