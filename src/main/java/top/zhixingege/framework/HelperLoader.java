package top.zhixingege.framework;

import top.zhixingege.framework.helper.*;
import top.zhixingege.framework.utils.ClassUtil;

public final class HelperLoader {
    public static void init(){
        Class<?>[] classes={
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> aClass : classes) {
            ClassUtil.loadClass(aClass.getName(),true);
        }
    }
}
