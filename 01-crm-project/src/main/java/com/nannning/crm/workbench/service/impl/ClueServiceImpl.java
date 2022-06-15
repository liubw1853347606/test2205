package com.nannning.crm.workbench.service.impl;

import com.nannning.crm.commons.contants.Contants;
import com.nannning.crm.commons.utils.DateUtils;
import com.nannning.crm.commons.utils.UUIDUtils;
import com.nannning.crm.settings.domain.User;
import com.nannning.crm.workbench.domain.*;
import com.nannning.crm.workbench.mapper.*;
import com.nannning.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public int deleteClueByIds(String[] ids) {
        return clueMapper.deleteClueById(ids);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int saveEditClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    /*把线索转换成客户*/
    @Override
    public void saveConvertClue(Map<String, Object> map) {
        String clueId=(String) map.get("clueId");
        User user= (User) map.get(Contants.SESSION_USER);
        //获取客户是否选择创建交易
        String isCreateTran= (String) map.get("isCreateTran");
        //根据id查询线索
        Clue clue=clueMapper.selectClueById(clueId);
        //把线索中有关公司的信息转换到客户表中
        Customer c=new Customer();
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setCreateBy(user.getId());
        c.setCreateTime(DateUtils.formateDateTime(new Date()));
        c.setId(UUIDUtils.getUUID());
        c.setName(clue.getCompany());
        c.setNextContactTime(clue.getNextContactTime());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(c);

        //把线索中的有关个人的信息转化到联系人表中
        Contacts co=new Contacts();
        co.setAddress(clue.getAddress());
        co.setAppellation(clue.getAppellation());
        co.setContactSummary(clue.getContactSummary());
        co.setCreateBy(user.getId());
        co.setCreateTime(DateUtils.formateDateTime(new Date()));
        co.setCustomerId(c.getId());
        co.setDescription(clue.getDescription());
        co.setEmail(clue.getEmail());
        co.setFullname(clue.getFullname());
        co.setId(UUIDUtils.getUUID());
        co.setJob(clue.getJob());
        co.setMphone(clue.getMphone());
        co.setNextContactTime(clue.getNextContactTime());
        co.setOwner(user.getId());
        co.setSource(clue.getSource());
        contactsMapper.insertContacts(co);

        //根据clueId查询该线索下所有备注信息
        List<ClueRemark> crList=clueRemarkMapper.selectClueRemarkByClueId(clueId);

        //如果该线索下有备注，把该线索下所有的备注转换到客户备注表中一份,
        //把该线索下所有的备注转换到联系人备注表中一份
        if(crList!=null&&crList.size()>0){
            //遍历crList，封装客户备注
            CustomerRemark cur=null;
            ContactsRemark cor=null;
            List<CustomerRemark> curList=new ArrayList<>();
            List<ContactsRemark> corList=new ArrayList<>();
            for(ClueRemark cr:crList){
                cur=new CustomerRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(c.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditFlag(cr.getEditFlag());
                cur.setEditTime(cr.getEditTime());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                curList.add(cur);

                cor=new ContactsRemark();
                cor.setContactsId(co.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditBy(cr.getEditBy());
                cor.setEditFlag(cr.getEditFlag());
                cor.setEditTime(cr.getEditTime());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                corList.add(cor);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }
        //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> carList=clueActivityRelationMapper.selectClueActivityRelationById(clueId);
        //把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if(carList!=null&&crList.size()>0){
            ContactsActivityRelation coar=null;
            List<ContactsActivityRelation> coarList=new ArrayList<>();
            for (ClueActivityRelation car:carList){
                 coar=new ContactsActivityRelation();
                 //联系人和市场活动的关联关系表的id由uuid生产
                 coar.setId(UUIDUtils.getUUID());
                 //联系人的Id从联系人表中获取
                 coar.setContactsId(co.getId());
                 //市场活动Id从线索与市场活动表中获取
                 coar.setActivityId(car.getActivityId());

                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }
        /*保存创建交易,如果需要创建交易，则往交易表中添加一条记录,还需要把该线索下的备注转换到交易备注表中一份*/
        //判断客户是否创建交易
        if("ture".equals(isCreateTran)){
            //创建一个交易实体类封装参数
            Tran tran=new Tran();
            //市场活动ID，从前台传过来的map中获取
            tran.setActivityId((String) map.get("activityId"));
            //联系人的ID，从新创建的联系人表中获取
            tran.setContactsId(co.getId());
            //创建人的ID，当前用户的id
            tran.setCreateBy(user.getId());
            //当前创建交易的时间
            tran.setCreateTime(DateUtils.formateDateTime(new Date()));
            //客户的ID，从新创建的客户表中获取
            tran.setCustomerId(c.getId());
            //从前台传过来的map中获取参数
            tran.setExpectedDate((String) map.get("expectedDate"));
            //给交易记录生成一个uuid
            tran.setId(UUIDUtils.getUUID());
            //从前台传过来的map中获取参数
            tran.setMoney((String) map.get("money"));
            //从前台传过来的map中获取参数
            tran.setName((String) map.get("name"));
            //所有者，当前用户的id
            tran.setOwner(user.getId());
            tran.setStage((String) map.get("stage"));

            tranMapper.insertTran(tran);

            //把该线索下的备注转换到交易备注表中
            if(crList!=null&&crList.size()>0) {
                TranRemark tr = null;
                List<TranRemark> trList = new ArrayList<>();
                for (ClueRemark cr : crList) {
                    tr = new TranRemark();
                    tr.setCreateBy(cr.getCreateBy());
                    tr.setCreateTime(cr.getCreateTime());
                    tr.setEditBy(cr.getEditBy());
                    tr.setEditFlag(cr.getEditFlag());
                    tr.setEditTime(cr.getEditTime());
                    tr.setId(UUIDUtils.getUUID());
                    tr.setNoteContent(cr.getNoteContent());
                    tr.setTranId(tran.getId());
                    trList.add(tr);
                }
                tranRemarkMapper.insertTranRemarkByList(trList);
            }
        }
    }
}
