package com.nannning.crm.workbench.mapper;

import com.nannning.crm.workbench.domain.ActivityRemark;

import java.util.List;

/*市场活动明细*/
public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Fri May 20 14:06:16 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /*根据市场活动id查询市场活动下所以备注的明细信息
    * 市场活动的备注可能不只一条，所以用list*/
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);
}