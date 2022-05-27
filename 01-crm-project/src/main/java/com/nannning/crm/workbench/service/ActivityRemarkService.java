package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    /*根据市场活动id查询市场活动下所以备注的明细信息*/
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    /*添加市场活动备注*/
    int saveCreateActivityRemark(ActivityRemark remark);

    /*删除市场活动备注*/
    int deleteActivityRemarkById(String id);

    /*修改市场活动备注*/
    int updateActivityRemark(ActivityRemark remark);
}
