package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
public class ClueServiceImpl implements ClueService {
//线索相关的表
@Autowired
    private ClueDao clueDao;
@Autowired
private ClueActivityRelationDao clueActivityRelationDao;
@Autowired
private ClueRemarkDao clueRemarkDao;
//客户相关的表
@Autowired
private CustomerDao customerDao;
@Autowired
private CustomerRemarkDao customerRemarkDao;
//联系人相关表
@Autowired
private ContactsDao contactsDao;
@Autowired
private ContactsRemarkDao contactsRemarkDao;
@Autowired
private ContactsActivityRelationDao contactsActivityRelationDao;
//交易相关表
@Autowired
private TranDao tranDao;
@Autowired
private TranHistoryDao tranHistoryDao;

    @Override
    public boolean save(Clue c) {

        boolean flag = true;
        int count =clueDao.save(c);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue c= clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbund(String id) {
        boolean flag =true;
        int count = clueActivityRelationDao.unbund(id);
         if (count!=1){
            flag=false;
}
        return flag;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean flag=true;
        ClueActivityRelation car = new ClueActivityRelation();
        for (String aid:aids) {
            //取得每一个aid和cid做关联
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }
        }
return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {

          boolean  flag= true;
            String createTime = DateTimeUtil.getSysTime();
            //（1）通过线索id获取线索对象  （线索对象中封装了线索的信息）

            Clue c= clueDao.getById(clueId);

            //（2）通过线索提取客户信息,当客户不存在的时候,新建客户(根据公司名称精确匹配,判断该客户是否存在)

            String    company = c.getCompany();
            Customer cus= customerDao.getCustomerByName(company);
            //如果cus为null,说明以前没有这个客户,需要新建一个
            if (cus==null){

                cus = new Customer();
                cus.setId(UUIDUtil.getUUID());
                cus.setAddress(c.getAddress());
                cus.setWebsite(c.getWebsite());
                cus.setPhone(c.getPhone());
                cus.setOwner(c.getOwner());
                cus.setNextContactTime(c.getNextContactTime());
                cus.setName(company);
                cus.setDescription(c.getDescription());
                cus.setCreateBy(createBy);
                cus.setCreateTime(createTime);
                cus.setContactSummary(c.getContactSummary());
                //添加客户
                int count1 =customerDao.save(cus);
                if (count1!=1){
                    flag=false;

                }


            }
                //经过第二部处理后,客户的信息我们已经拥有了,将来在处理其他表的时候，如果要使用到客户的id
                //直接使用cus.getId();
                //----------------
                //(3)通过线索对象提取联系人信息,保存联系人
                Contacts con = new Contacts();
                con.setId(UUIDUtil.getUUID());
                con.setSource(c.getSource());
                con.setOwner(c.getOwner());
                con.setNextContactTime(c.getNextContactTime());
                con.setMphone(c.getMphone());
                con.setJob(c.getJob());
                con.setFullname(c.getFullname());
                con.setEmail(c.getEmail());
                con.setDescription(c.getDescription());
                con.setCustomerId(cus.getId());
                con.setCreateTime(createTime);
                con.setCreateBy(createBy);
                con.setContactSummary(c.getContactSummary());
                con.setAppellation(c.getAppellation());
                con.setAddress(c.getAddress());
                //添加联系人
                int count2 =contactsDao.save(con);
                if (count2!=1){
                    flag= false;

                }

                //-------------------------------
                //经过第三部处理后,联系人的信息我们已经拥有了,将来在处理其他表的时候，如果要使用到联系人的id
                //直接使用con.getId();
                //-------------------------------


            //(4)线索备注转换到客户备注以及联系人备注
            //查询出与该线索相关的备注信息列表
            List<ClueRemark> clueRemarkList= clueRemarkDao.getListByClueId(clueId);
                for (ClueRemark clueRemark:clueRemarkList){

                    //取出每一条线索的备注信息（主要转换到客户备注和联系人备注就是这个信息）
                    String noteContent = clueRemark.getNoteContent();

                    //创建客户备注对象,添加客户备注

                    CustomerRemark customerRemark = new CustomerRemark();
                    customerRemark.setId(UUIDUtil.getUUID());
                    customerRemark.setCreateBy(createBy);
                    customerRemark.setCreateTime(createTime);
                    customerRemark.setCustomerId(cus.getId());
                    customerRemark.setEditFlag("0");
                    customerRemark.setNoteContent(noteContent);
                    int count3= customerRemarkDao.save(customerRemark);
                    if (count3!=1){
                        flag=false;
                    }


                    //创建联系人备注对象,添加联系人
                    ContactsRemark contactsRemark = new ContactsRemark();
                    contactsRemark.setId(UUIDUtil.getUUID());
                    contactsRemark.setCreateBy(createBy);
                    contactsRemark.setCreateTime(createTime);
                    contactsRemark.setContactsId(con.getId());
                    contactsRemark.setEditFlag("0");
                    contactsRemark.setNoteContent(noteContent);
                    int count4= contactsRemarkDao.save(contactsRemark);
                    if (count4!=1){
                        flag=false;
                    }




                }

                //(5)"线索和市场活动"的关联关系转换到"联系人和市场活动的关联关系"
                //查询出与该条线索关联的市场活动,查询与市场活动的关联关系列表
                List<ClueActivityRelation>clueActivityRelationList=  clueActivityRelationDao.getListByClueId(clueId);
                //遍历每一条与市场活动关联的关联关系记录
               for(ClueActivityRelation clueActivityRelation:clueActivityRelationList) {

                   //从每一条遍历出来的记录中取出关联市场活动的Id
                   String activityId = clueActivityRelation.getActivityId();
                   //创建联系人与市场活动的关联关系对象 让第三步生成的联系人与市场活动做关联
                   ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                   contactsActivityRelation.setId(UUIDUtil.getUUID());
                   contactsActivityRelation.setActivityId(activityId);
                   contactsActivityRelation.setContactsId(con.getId());
                   //添加联系人与市场活动的关联关系
                   int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
                   if (count5 != 1) {

                       flag = false;

                   }
               }
                   //（6）如果有创建交易的需求,创建一条交易
                   if (t!=null){

                       /*

                      t对象在controller里面已经封装好的信息如下：
                      id,money,name,expectedDate,stage,activityId,createBy,createTime

                      接下来可以通过第一步生成的c对象,取出一些信息,继续完善对t对象的封装


                        */
                       t.setSource(c.getSource());
                       t.setOwner(c.getOwner());
                       t.setNextContactTime(c.getNextContactTime());
                       t.setDescription(c.getDescription());
                       t.setCustomerId(cus.getId());
                       t.setContactSummary(c.getContactSummary());
                       t.setContactsId(con.getId());
                       //添加交易
                       int count6 =  tranDao.save(t);
                       if (count6!=1){
                           flag=false;

                       }

                       //(7)如果创建了交易,则创建一条该交易下的交易历史
                       TranHistory th = new TranHistory();
                       th.setId(UUIDUtil.getUUID());
                       th.setCreateBy(createBy);
                       th.setCreateTime(createTime);
                       th.setExpectedDate(t.getExpectedDate());
                       th.setMoney(t.getMoney());
                       th.setStage(t.getStage());
                       th.setTranId(t.getId());
                       //添加交易历史
                       int count7 = tranHistoryDao.save(th);
                       if (count7!=1){

                           flag=false;
                       }

                   }







               //（8）删除线索备注

        for (ClueRemark clueRemark:clueRemarkList){

            int count8= clueRemarkDao.delete(clueRemark);
            if (count8!=1){

                flag=false;

            }

        }

        //（9）删除线索和市场活动的关联关系

        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){

            int count9= clueActivityRelationDao.delete(clueActivityRelation);

            if (count9!=1){
                flag=false;
            }

        }
        //（10） 删除线索

        int count10= clueDao.delete(clueId);
        if (count10!=1){
            flag=false;
        }

            return flag;
    }


}
