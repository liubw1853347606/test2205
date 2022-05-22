package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    /*根据市场活动id查询市场活动下所以备注的明细信息*/
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);
}
