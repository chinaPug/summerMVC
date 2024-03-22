package top.zhixingege.framework.base.controller;

import top.zhixingege.framework.annotation.Action;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.annotation.Inject;
import top.zhixingege.framework.base.component.A;
import top.zhixingege.framework.bean.Data;
import top.zhixingege.framework.bean.Param;
import top.zhixingege.framework.helper.BeanHelper;

import java.util.Map;
import java.util.Objects;

@Controller
public class TargetController {
    @Action("get:/test")
    public Data action(Param param){
        return new Data(true);
    }
}
