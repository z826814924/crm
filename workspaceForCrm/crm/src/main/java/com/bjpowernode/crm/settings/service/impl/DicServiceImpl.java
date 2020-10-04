package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;

import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

@Autowired
    private DicTypeDao dicTypeDao ;
@Autowired
    private DicValueDao dicValueDao;

    public void x(){
        System.out.println("DicTypeDao="+dicTypeDao);
    }

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();

        //将字典类型列表取出
        List<DicType> dList= dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for (DicType dt:dList){
            //取得每一种类型字典类型的编码
        String code = dt.getCode();
         //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList= dicValueDao.getListByCode(code);
            map.put(code+"List",dvList);

        }



        return map;
    }
}
