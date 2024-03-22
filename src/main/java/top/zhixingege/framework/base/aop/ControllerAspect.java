package top.zhixingege.framework.base.aop;

import top.zhixingege.framework.annotation.Aspect;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.annotation.Inject;
import top.zhixingege.framework.annotation.Service;
import top.zhixingege.framework.base.component.A;
import top.zhixingege.framework.proxy.AspectProxy;
import top.zhixingege.framework.utils.ArrayUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    @Override
    public void begin() {
        System.out.println("this is begin");
    }

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        System.out.println("this is before");
    }
}
