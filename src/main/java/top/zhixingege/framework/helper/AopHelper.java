package top.zhixingege.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zhixingege.framework.annotation.Aspect;
import top.zhixingege.framework.proxy.AspectProxy;
import top.zhixingege.framework.proxy.Proxy;
import top.zhixingege.framework.proxy.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * AOP助手类
 */
public final class AopHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            //获取proxy到target之间的一对多映射
            Map<Class<?>,Set<Class<?>>> proxyMap=createProxyMap();
            //反转，获取target到proxy的一对多映射
            Map<Class<?>, List<Proxy>> targetMap=createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass=targetEntry.getKey();
                List<Proxy> proxyList=targetEntry.getValue();
                //调用ProxyManager静态方法createProxy，
                Object proxy= ProxyManager.createProxy(targetClass,proxyList);
                //将代理对象设置到beanMap中
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop failure",e);
        }
    }

    /**
     * 获取proxy到target的一对多映射
     * @return
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() {
        Map<Class<?>,Set<Class<?>>> proxyMap=new HashMap<>();
        //proxy类通过有无继承AspectProxy抽象类来判断
        Set<Class<?>> proxyClassSet=ClassHelper.getClassSetBySuper(AspectProxy.class);
        //遍历proxy类集合
        for (Class<?> cls : proxyClassSet) {
            //如果有Aspect注解，则是切面
            if (cls.isAnnotationPresent(Aspect.class)){
                Aspect aspect=cls.getAnnotation(Aspect.class);
                //根据aspect注解里的标注获取所有切点类
                Set<Class<?>> targetClassSet=createTargetClassSet(aspect);
                //将proxy和target类一对多映射
                proxyMap.put(cls,targetClassSet);
            }
        }
        return proxyMap;
    }
    //获取所有标注切点注解的类
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> targetClassSet=new HashSet<>();
        Class<? extends Annotation> annotation=aspect.value();
        if (!Objects.isNull(annotation)&&!annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }
    //将proxy和target类一对多映射转换成target和proxy一对多映射
    private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>,List<Proxy>> targetMap=new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            Class<?> proxyClass=proxyEntry.getKey();
            Set<Class<?>> targetClassSet=proxyEntry.getValue();
            for (Class<?> cls : targetClassSet) {
                Proxy proxy=(Proxy) proxyClass.getDeclaredConstructor().newInstance();
                if (targetMap.containsKey(cls)){
                    targetMap.get(cls).add(proxy);
                }
                else {
                    List<Proxy> proxyList=new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(cls,proxyList);
                }
            }
        }
        return targetMap;
    }
}
