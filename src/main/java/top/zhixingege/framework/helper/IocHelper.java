package top.zhixingege.framework.helper;



import top.zhixingege.framework.annotation.Inject;
import top.zhixingege.framework.utils.ArrayUtil;
import top.zhixingege.framework.utils.CollectionUtil;
import top.zhixingege.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {
    static {
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>,Object> entry:beanMap.entrySet()){
                Class<?> beanClass=entry.getKey();
                Object beanInstance=entry.getValue();
                Field[] fields=beanClass.getFields();
                System.out.println(beanClass.getName());
                if (ArrayUtil.isNotEmpty(fields)){
                    for (Field field : fields) {
                        System.out.println("----------------");
                        System.out.println(field.getName());
                        System.out.println(field.isAnnotationPresent(Inject.class));
                        if (field.isAnnotationPresent(Inject.class)){
                            Class<?> beanFieldClass=field.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            if (beanFieldInstance!=null){
                                ReflectionUtil.setField(beanInstance,field,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
        Map<Class<?>, Object> a=BeanHelper.getBeanMap();
        System.out.println("scacsacsa");
    }
}
