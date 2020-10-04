package com.bjpowernode.settings.test;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
/*        //失效时间
        String expireTime = "2020-10-10 10:10:10";
        //当前系统时间
        String currentTime = DateTimeUtil.getSysTime();
        int count =expireTime.compareTo(currentTime);
        System.out.println(count);
  */
        String pwd ="123";
        String pwd1=MD5Util.getMD5(pwd);
        System.out.println(pwd1+" "+pwd1.length());
        System.out.println("的撒啊");
    }


}
