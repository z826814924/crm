package com.bjpowernode.crm.settings.exceptionhandler;


import com.bjpowernode.crm.exception.ActivityDeleteException;
import com.bjpowernode.crm.exception.DeleteRemarkException;
import com.bjpowernode.crm.exception.DengLuException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {
    //定义方法，处理发生的异常
    //处理异常的方法和控制器中方法的定义一样，有各种类型的返回值
@ExceptionHandler(value = DengLuException.class)
@ResponseBody
public Map doLoginException(Exception exception, HttpServletRequest request){
        Map<String,Object> map= (Map<String, Object>) request.getAttribute("map");
        map.put("success",false);
        map.put("msg",exception.getMessage());
        return map;
    }


    @ExceptionHandler(value = ActivityDeleteException.class)
    @ResponseBody
    public Map doActivityDeleteException(Exception exception,HttpServletRequest request){
        Map<String,Object> map= (Map<String, Object>) request.getAttribute("map");
        map.put("success",false);
        map.put("msg",exception.getMessage());
        return map;
    }

    @ExceptionHandler(value = DeleteRemarkException.class)
    @ResponseBody
    public Map doDeleteRemarkException(Exception exception,HttpServletRequest request){
        Map<String,Object> map= (Map<String, Object>) request.getAttribute("map");
        map.put("success",false);
        map.put("msg",exception.getMessage());
        return map;
    }


}
