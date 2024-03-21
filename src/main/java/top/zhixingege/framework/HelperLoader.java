package top.zhixingege.framework;

import top.zhixingege.framework.helper.*;
import top.zhixingege.framework.utils.ClassUtil;

public final class HelperLoader {
    public static void init(){
        Class<?>[] classes={
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class,
                AopHelper.class
        };
        for (Class<?> aClass : classes) {
            System.out.println(aClass.getName());
            ClassUtil.loadClass(aClass.getName(),true);
        }
    }
}
