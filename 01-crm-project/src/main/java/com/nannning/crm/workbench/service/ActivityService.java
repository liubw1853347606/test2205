package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int saveEditActivity(Activity activity);

    /*批量导出*/
    List<Activity> queryAllActivity();

    /*选择导出*/
    List<Activity> queryActivityXzIds(String[] id);

    /*批量保存创建的市场活动*/
    int saveCreateActivityByList(List<Activity> activityList);

    /*根据id来查询市场活动的明细*/
    Activity queryActivityForDetailById(String id);

    /*根据clueId查询该线索相关联的市场活动的明细信息*/
    List<Activity> queryActivityForDetailByClueId(String clueId);

    /*根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除*/
    List<Activity> queryActivityForDetailByClueNameId(Map<String,Object> map);

    /*根据ids查询市场活动的明细信息*/
    List<Activity> queryActivityForDetailByIds(String[] ids);

    /*市场活动源 根据name模糊查询市场活动，并且查询那些跟clueId关联过的市场活动*/
    List<Activity> queryActivityForConvertByNameClueId(Map<String,Object> map);
}
