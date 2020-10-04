package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Autowired
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran t, String customerName) {

        //交易添加业务 t里面没有customerId
        //根据客户名称在客户表精确查询 有酒封装到t 没有新建一个客户 取出id
        //添加交易完毕后 创建一条交易历史
        boolean flag = true;

        Customer cus= customerDao.getCustomerByName(customerName);
        if (cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1 =customerDao.save(cus);
            if (count1!=1){
                flag =false;
            }

        }
        //-----------------客户已经有了 id就有了
        t.setCustomerId(cus.getId());

        //添加交易
         int count2= tranDao.save(t);
         if (count2!=1){
             flag=false;
         }


         //添加交易历史

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(t.getCreateTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setTranId(t.getId());
        //添加交易历史
        int count3 = tranHistoryDao.save(th);
        if (count3!=1){

            flag=false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

         List<TranHistory> thList =tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag =true;

        //改变交易阶段
        int count1= tranDao.changeStage(t);
        if (count1!=1){
            flag=false;
        }

        //交易阶段改变后,生成一条交易历史
        TranHistory th =new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setPossibility(t.getPossibility());
        //添加交易历史
        int count2= tranHistoryDao.save(th);
        if (count2!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
int total = tranDao.getTotal();
        //取得dataList


        List<Map<String,Object>> dataList= tranDao.getCharts();
        //将total和dataList保存到Map中
Map<String,Object> map = new HashMap<>();
map.put("total",total);
map.put("dataList",dataList);

        //返回Map
        return map;
    }
}
