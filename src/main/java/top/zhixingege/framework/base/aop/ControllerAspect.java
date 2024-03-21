package top.zhixingege.framework.base.aop;

import top.zhixingege.framework.annotation.Aspect;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.proxy.AspectProxy;

import java.lang.reflect.Method;

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
