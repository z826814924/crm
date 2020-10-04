package com.bjpowernode.crm.workbench.web.controller;



import com.bjpowernode.crm.settings.domain.User;

import com.bjpowernode.crm.settings.service.UserService;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

@Autowired
private  UserService us;
@Autowired
private CustomerService cs;
@Autowired
private TranService ts;


    @RequestMapping("/add.do")
    public ModelAndView service1(){
        ModelAndView mv = new ModelAndView();
        System.out.println("进入到跳转到交易添加页的操作");
        List<User> uList=us.getUserList();
        mv.addObject("uList",uList);
        mv.setViewName("/workbench/transaction/save.jsp");
        return mv;


    }

    @ResponseBody
    @RequestMapping("/getCustomerName.do")
    public List<String>service2(HttpServletRequest request,String name){
        System.out.println("取得客户名称列表，按照名称进行模糊查询");

        List<String> sList= cs.getCustomerName(name);
        return sList;


    }

    @RequestMapping("/save.do")
    public ModelAndView service3(HttpServletRequest request, Tran t,String customerName){
        ModelAndView mv=null;
        System.out.println("执行添加交易的操作");
        String id = UUIDUtil.getUUID();
        //创建时间,当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
//创建人：当前登陆的用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        t.setId(id);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
         boolean flag = ts.save(t,customerName);
         if (flag){
             return new ModelAndView("redirect:/workbench/transaction/index.jsp");
         }


return mv;
    }

    @RequestMapping("/detail.do")
    public ModelAndView service4(HttpServletRequest request,String id){
        ModelAndView mv = new ModelAndView();
        System.out.println("跳转到交易详细信息页");
         Tran t  = ts.detail(id);
         mv.setViewName("/workbench/transaction/detail.jsp");
         //处理可能性
        String stage = t.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);
        mv.addObject("t",t);
        return mv;


    }

    @ResponseBody
    @RequestMapping("/getHistoryListByTranId.do")
    public List<TranHistory>service5(HttpServletRequest request,String tranId){
        System.out.println("根据交易的id取得相应的历史列表");

        List<TranHistory> thList= ts.getHistoryListByTranId(tranId);
        //交易历史列表遍历
        //阶段和可能性的对于关系
                  ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        for(TranHistory th : thList){

            //根据每一条交易历史取出每一个阶段
            String  stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);



        }

        return thList;


    }


    @ResponseBody
    @RequestMapping("/changeStage.do")
    public Map<String, Object> service6(HttpServletRequest request, Tran t){
        System.out.println("改变阶段");

        t.setEditTime(DateTimeUtil.getSysTime());
        t.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        boolean flag= ts.changeStage(t);
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        t.setPossibility(pMap.get(t.getStage()));
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("t",t);

        return map;


    }

    @ResponseBody
    @RequestMapping("/getCharts.do")
    public Map<String,Object> service7(HttpServletRequest request){
        System.out.println("取得交易阶段数量统计图表的数据");
        //业务层应该返回total dataList 通过Map打包这两项
            Map<String,Object>  map=ts.getCharts();
        return map;


    }

}


