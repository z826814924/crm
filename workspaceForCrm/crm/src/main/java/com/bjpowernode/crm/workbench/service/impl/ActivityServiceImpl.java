package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.exception.ActivityDeleteException;
import com.bjpowernode.crm.exception.DeleteRemarkException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.ActivitySearch;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private PaginationVO vo;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;
    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int count= activityDao.save(activity);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(ActivitySearch activitySearch) {


            int num = activitySearch.getPageNo();
            int size = activitySearch.getPageSize();
            num = (num-1)*size;
            activitySearch.setPageNo(num);
        //取得total
        int total = activityDao.getTotalByCondition(activitySearch);

         List<Activity> dataList =activityDao.getActivityListByCondition(activitySearch);
        //创建一个vo对象 将total和dataList封装到vo中
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回
        return vo;
    }

    @Transactional
    @Override
    public boolean delete(String[] ids) throws ActivityDeleteException {
       // boolean flag = true;
        //查询出需要删除的备注的数量
        int count1= activityRemarkDao.getCountByAids(ids);

        //删除备注,返回受到影响的条数(实际删除的数量)
        int count2 = activityRemarkDao.deleteByAids(ids);

        if (count1!=count2){
            throw new ActivityDeleteException("查询出来的备注和实际删除的备注条数不符合");
        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if (count3!=ids.length){
            throw new ActivityDeleteException("查询出来的备注和实际删除的市场活动条数不符合");


        }


        return true;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList= userDao.getUserList();



        //取a

        Activity a = activityDao.getById(id);


        //将uList和a打包到map中
        Map<String,Object> map = new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);


        //返回map就可以了
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count= activityDao.update(activity);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a= activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
         List<ActivityRemark> arList= activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }

    @Override
    public boolean deleteRemark(String id) throws DeleteRemarkException {



        int count = activityRemarkDao.deleteById(id);

        if (count!=1){
            throw new DeleteRemarkException("删除条数不为1");
        }


        return true;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count!=1){
            flag = false;
        }
        return  flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag = false;
        }
        return  flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList= activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList =activityDao.getActivityListByNameAndNotByClueId(map);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList = activityDao.getActivityListByName(aname);
        return aList;
    }
}
