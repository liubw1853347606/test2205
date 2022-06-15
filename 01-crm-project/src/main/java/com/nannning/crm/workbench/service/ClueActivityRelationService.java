package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    /*批量保存创建的线索和市场活动的关联关系*/
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    /*根据clueId和activityId删除线索和市场活动的关联关系*/
    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
