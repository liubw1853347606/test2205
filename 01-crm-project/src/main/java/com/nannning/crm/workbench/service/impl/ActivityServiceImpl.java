package com.nannning.crm.workbench.service.impl;

import com.nannning.crm.workbench.domain.Activity;
import com.nannning.crm.workbench.mapper.ActivityMapper;
import com.nannning.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityById(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    /*批量导出*/
    @Override
    public List<Activity> queryAllActivity() {
        return activityMapper.selectAllActivity();
    }

    /*选择导出*/
    @Override
    public List<Activity> queryActivityXzIds(String[] id) {
        return activityMapper.selectActivityXzIds(id);
    }

    /*批量保存创建的市场活动*/
    @Override
    public int saveCreateActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }
}
