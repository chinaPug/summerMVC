package top.zhixingege.framework.helper;

import top.zhixingege.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class BeanHelper {
    private static final Map<Class<?>, Object> BEAN_MAP=new HashMap<>();
    static {
        Set<Class<?>> beanClassSet=ClassHelper.getBeanClassSet();
        for (Class<?> aClass : beanClassSet) {
            BEAN_MAP.put(aClass, ReflectionUtil.newInstance(aClass));
        }
    }

    /**
     * 获取BEAN_MAP
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取BEAN实例
     * @param cls
     * @return
     */
    //@SuppressWarnings注解的出现主要是为了解决在编译Java代码时，由于各种原因可能产生的编译器警告，而这些警告在某些情况下是可以被明确地认为是不会影响程序执行的。
    //代码兼容性：版本升级，使用旧代码会出现新的警告以及性能优化或者强制代码规范
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class:"+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置BEAN实例
     * @param cls
     * @param obj
     */
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}
