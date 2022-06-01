package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {

    /*保存创建线索*/
    int saveCreateClue(Clue clue);

    /**
     根据条件分页查询线索的列表
     * @return
     */
    List<Clue> queryClueByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询线索的总条数
     * @param map
     * @return
     */
    int queryCountOfClueByCondition(Map<String,Object> map);
}
