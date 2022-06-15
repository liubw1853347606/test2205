package com.nannning.crm.workbench.web.controller;

import com.nannning.crm.commons.contants.Contants;
import com.nannning.crm.commons.domain.ReturnObject;
import com.nannning.crm.commons.utils.DateUtils;
import com.nannning.crm.commons.utils.UUIDUtils;
import com.nannning.crm.settings.domain.DicValue;
import com.nannning.crm.settings.domain.User;
import com.nannning.crm.settings.service.DicValueService;
import com.nannning.crm.settings.service.UserService;
import com.nannning.crm.workbench.domain.Activity;
import com.nannning.crm.workbench.domain.Clue;
import com.nannning.crm.workbench.domain.ClueActivityRelation;
import com.nannning.crm.workbench.domain.ClueRemark;
import com.nannning.crm.workbench.service.ActivityService;
import com.nannning.crm.workbench.service.ClueActivityRelationService;
import com.nannning.crm.workbench.service.ClueRemarkService;
import com.nannning.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;



    //创建线索获取用户下拉列表
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询所有的用户
        List<User> userList= userService.queryAllUser();
        //调用service层方法，查询数据字典值
        List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        //把数据保存到request中
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        //跳转页面
        return "workbench/clue/index";

    }

    //保存创建线索
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public @ResponseBody Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User) session.getAttribute(Contants.SESSION_USER);//从session中获取当期用户
        //收集参数
        clue.setId(UUIDUtils.getUUID());//生成一个用户id
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));//获取创建市场活动的当前时间
        clue.setCreateBy(user.getId());//封装创建市场活动的当前用户

        /*在往数据库写入数据时，有可能会发生错误，需要抛出异常信息*/
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存创建的市场活动
            int ret = clueService.saveCreateClue(clue);
            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");

        }
        return returnObject;
    }

    /*根据条件分页查询市场活动的列表*/
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    public @ResponseBody Object queryClueByConditionForPage(String fullname,String company,String phone,String mphone,
                                      String source,String owner,String state, int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("state",state);

        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //调用service层方法，查询数据
        List<Clue> clueList=clueService.queryClueByConditionForPage(map);
        int totalRows=clueService.queryCountOfClueByCondition(map);
        //根据查询结果结果，生成响应信息
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    /*根据id删除市场活动*/
    @RequestMapping("/workbench/clue/deleteClueIds.do")
    @ResponseBody
    public Object deleteClueIds(String[] id){

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除市场活动
            int ret = clueService.deleteClueByIds(id);
            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }

    /*根据Id查询市场活动*/
    @RequestMapping("/workbench/clue/selectClueById.do")
    @ResponseBody
    public Object selectClueById(String id){
        //调用service层方法，查询市场活动
        Clue clue=clueService.queryClueById(id);
        //返回响应信息
        return clue;
    }
    /**
     保存修改的市场活动*/
    @RequestMapping("/workbench/clue/saveEditClue.do")
    public @ResponseBody Object saveEditClue(Clue clue,HttpSession session){
        //封装参数
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        clue.setEditTime(DateUtils.formateDateTime(new Date()));//修改市场活动的时间
        clue.setEditBy(user.getId());//修改人
        //调用service层方法，保存修改的市场活动
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = clueService.saveEditClue(clue);
            if (ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }

    /*根据clueId查询该线索相关联的市场活动的明细信息*/
    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        //调用service层方法,查询数据
        Clue clue=clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);

        //保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        //请求转发
        return "workbench/clue/detail";
    }

    /*根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除*/
    @RequestMapping("/workbench/clue/queryActivityForDetailByClueNameId.do")
    public @ResponseBody Object queryActivityForDetailByClueNameId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法,查询数据
        List<Activity> activityList=activityService.queryActivityForDetailByClueNameId(map);
        //返回响应信息
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    public @ResponseBody Object saveBund(String[] activityId,String clueId){
        //封装参数
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for(String ai:activityId){
            car=new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service方法，批量保存线索和市场活动的关联关系
            int ret = clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList=activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }

    /*根据clueId和activityId删除线索和市场活动的关联关系*/
    @RequestMapping("/workbench/clue/saveUnbund.do")
    public @ResponseBody Object saveUnbund(ClueActivityRelation relation){
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除线索和市场活动的关联关系
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);

            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }

    /*查询线索的明细信息*/
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service层方法，查询线索的明细信息
        Clue clue=clueService.queryClueForDetailById(id);
        //查询数据字典值
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存到request域中
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/clue/convert";
    }

    /*市场活动源 根据name模糊查询市场活动，并且把已经跟clueId关联过的市场活动排除*/
    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法,查询数据
        List<Activity> activityList=activityService.queryActivityForConvertByNameClueId(map);
        //返回响应信息
        return activityList;
    }
    @RequestMapping("/workbench/clue/convertClue.do")
    public @ResponseBody Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存线索转换
            clueService.saveConvertClue(map);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }

        return returnObject;
    }

}
