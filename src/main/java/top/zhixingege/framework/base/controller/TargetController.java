package top.zhixingege.framework.base.controller;

import top.zhixingege.framework.annotation.Action;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.bean.Data;
import top.zhixingege.framework.bean.Param;

@Controller
public class TargetController {
    @Action("get:/test")
    public Data action(Param param){
        System.out.println("this is target class!");
        return new Data(true);
    }
}
