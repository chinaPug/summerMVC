package top.zhixingege.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class ReflectionUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 根据类对象创建实例
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            //JDK9后弃用了Class.newInstance(),主要原因包括提高代码的清晰度和更透明地处理异常。
            //会传播由无参构造函数抛出的任何异常，包括检查异常。调用 newInstance() 的代码不能具体处理这些异常
            //因为 newInstance() 会将任何异常包装在一个 InvocationTargetException 中。这种行为使得异常处理变得复杂，代码难以理解。
            instance=cls.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 设置对象的成员变量值
     * @param aclass
     * @param field
     * @param value
     */
    public static void setField(Object aclass, Field field,Object value){
        try {
            //设置private变量的访问权
            field.setAccessible(true);
            field.set(aclass,value);

        } catch (Exception e) {
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }
}
