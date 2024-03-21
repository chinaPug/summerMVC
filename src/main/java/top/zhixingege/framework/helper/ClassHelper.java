package top.zhixingege.framework.helper;

import com.fasterxml.jackson.databind.introspect.Annotated;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.annotation.Service;
import top.zhixingege.framework.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 * 功能：获取应用包名下的所有Bean，即带有Controller、Service注解的类
 */
public final class ClassHelper {
    /**
     * 定义类集合，用于存放所加载的类
     */
    private static final Set<Class<?>> CLASS_SET;

    /**
     * 静态代码块:类被实例化多次，静态代码块也仅在类加载时执行一次
     * 静态代码块的执行事件是在类加载过程的初始化阶段执行的：加载->验证->准备->解析->初始化
     * 静态代码块>代码块>构造函数
     * 执行规律：
     *  1.先静态后非静态
     *  2.先声明后赋值
     *  3.先属性后方法：限制性属性定义的初始化，再执行方法的初始化，即构造块（就是代码块）优先构造函数
     *  4.先父类后子类
     */
    static {
        //获取基础包名
        String basePackage =ConfigHelper.getAppBasePackage();
        //给静态属性赋值，即获取基础包下所有的类集合
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     * @return 应用包名下的所有类
     */
    public static Set<Class<?>> getAppBasePackageClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有Service类
     * @return 应用包名下所有Service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if (aClass.isAnnotationPresent(Service.class)){
                classSet.add(aClass);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下所有Controller类
     * @return 应用包名下所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for (Class<?> aClass : CLASS_SET) {
            if (aClass.isAnnotationPresent(Controller.class)){
                classSet.add(aClass);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有Bean，即包括Controller和Service
     * @return 应用包名下所有Bean
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        classSet.addAll(getControllerClassSet());
        classSet.addAll(getServiceClassSet());
        return classSet;
    }

    /**
     * 获取应用包名下某父类（或者接口）的所有子类（或者实现类）
     * @param superClass
     * @return
     */
    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> classSet=new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            //Class.isAssignableFrom()判断cls是否是superClass的子类或者实现类
            if (superClass.isAssignableFrom(cls)&&!superClass.equals(cls)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应包名下带有指定注解的所有类
     * @param annotation
     * @return
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation){
        Set<Class<?>> classSet=new HashSet<>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(annotation)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
