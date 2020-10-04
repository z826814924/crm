package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

@Component
public class SysInitListener implements ServletContextListener {


    //监听上下文域对象,上下文域对象创建完毕后，马上执行该方法
    //该参数能取得监听的对象,监听的对象是什么就能取得什么,现在是监听上下文对象
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文域对象创建了");
  //     ServletContext application= event.getServletContext();
        //取数据字典
        //应该管业务层要7个List,可以打包成为一个map
        //业务层：map.put("appellationList"dvList1);
        //map.put("appellationList"dvList2); ...
         //这样保存数据的

/*       Map<String, List<DicValue>> map=ds.getAll();
        //将map解析为上下文域对象中保存的键值对
      Set<String> set =map.keySet();
        for (String key:set
             ) {
            application.setAttribute(key,map.get(key));
        }*/

//处理stage2Possibility.properties文件
        //解析文件将属性文件中的键值对变成java中的键值对

        Map<String,String> pMap= new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("conf.Stage2Possibility");
         Enumeration<String> e =rb.getKeys();
         while (e.hasMoreElements()){
            //阶段
             String key = e.nextElement();
            //可能性
             String value =rb.getString(key);

             pMap.put(key,value);
         }
         //将pMap保存到服务器缓存中
        ServletContext application= event.getServletContext();
         application.setAttribute("pMap",pMap);


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
