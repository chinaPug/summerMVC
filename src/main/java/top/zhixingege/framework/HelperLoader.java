package top.zhixingege.framework;

import top.zhixingege.framework.helper.BeanHelper;
import top.zhixingege.framework.helper.ClassHelper;
import top.zhixingege.framework.helper.ControllerHelper;
import top.zhixingege.framework.helper.IocHelper;
import top.zhixingege.framework.utils.ClassUtil;

public final class HelperLoader {
    public static void init(){
        Class<?>[] classes={
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> aClass : classes) {
            ClassUtil.loadClass(aClass.getName(),false);
        }
    }
}
