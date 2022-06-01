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
import com.nannning.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {

    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;

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
}
