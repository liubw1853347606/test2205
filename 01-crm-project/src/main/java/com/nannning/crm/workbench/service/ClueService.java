package com.nannning.crm.workbench.service;

import com.nannning.crm.workbench.domain.Activity;
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

    /*根据id删除线索*/
    int deleteClueByIds(String[] ids);

    /**
     根据Id查询线索的信息*/
    Clue queryClueById(String id);

    /**
     保存修改的线索*/
    int saveEditClue(Clue clue);

    /**
     * 根据id查询线索的明细信息
     * @param id
     * @return
     */
    Clue queryClueForDetailById(String id);

    /*把线索转换成客户*/
    void saveConvertClue(Map<String,Object> map);
}
