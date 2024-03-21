package top.zhixingege.framework.helper;



import org.apache.commons.lang3.ArrayUtils;
import top.zhixingege.framework.annotation.Inject;
import top.zhixingege.framework.utils.ArrayUtil;
import top.zhixingege.framework.utils.CollectionUtil;
import top.zhixingege.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class IocHelper {
    static {
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>,Object> entry:beanMap.entrySet()){
                Class<?> beanClass=entry.getKey();
                Object beanInstance=entry.getValue();
                Field[] fields=beanClass.getFields();
                if (ArrayUtil.isNotEmpty(fields)){
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(Inject.class)&& Objects.isNull(beanMap.get(field.getClass()))){
                            ReflectionUtil.setField(beanInstance,field,beanMap.get(field.getClass()));
                        }
                    }
                }
            }
        }
    }
}
