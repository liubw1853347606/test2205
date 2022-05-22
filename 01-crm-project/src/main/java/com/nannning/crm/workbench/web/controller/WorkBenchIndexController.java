package com.nannning.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//跳转主页面
@Controller
public class WorkBenchIndexController {
    @RequestMapping("/workbench/index.do")
    public String index(){
        return "workbench/index";
    }
}
