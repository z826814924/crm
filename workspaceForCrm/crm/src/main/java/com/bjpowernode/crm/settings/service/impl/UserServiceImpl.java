package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.DengLuException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    //相当于private UserDao userDao = SqlSessionUtil.getSqlSession.getMapper(User.class)
    //自动创建代理类对象
    @Resource
    private UserDao userDao;


    @Override
    public User login(String loginAct, String loginPwd, String ip) throws  DengLuException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        System.out.println(loginAct);
        System.out.println(loginPwd);
        System.out.println(userDao);

        User user =userDao.login(map);
        System.out.println(user);
        if (user==null){
            System.out.println("user为空");
            throw new DengLuException("账号密码错误");
        }
//如果程序能够成功的执行到改行，说明账号密码正确
        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new DengLuException("账号已失效");
        }
        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new DengLuException("账号已锁定");
        }
        //判断ip地址
        String allowIps=user.getAllowIps();

        if (!allowIps.contains(ip)){
            throw new DengLuException("ip地址受限");

        }
        return user;
    }

    @Override
    public List<User> getUserList() {
            List<User> uList= userDao.getUserList();
        return uList;
    }
}
