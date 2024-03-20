package top.zhixingege.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zhixingege.framework.annotation.Aspect;
import top.zhixingege.framework.proxy.AspectProxy;
import top.zhixingege.framework.proxy.Proxy;
import top.zhixingege.framework.proxy.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;


public final class AopHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            Map<Class<?>,Set<Class<?>>> proxyMap=createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap=createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass=targetEntry.getKey();
                List<Proxy> proxyList=targetEntry.getValue();
                Object proxy= ProxyManager.createProxy(targetClass,proxyList);
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            LOGGER.error("aop failure",e);
        }
    }
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap=new HashMap<>();
        Set<Class<?>> proxyClassSet=ClassHelper.getClassSetBySuper(AspectProxy.class);
        for (Class<?> cls : proxyClassSet) {
            if (cls.isAnnotationPresent(Aspect.class)){
                Aspect aspect=cls.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet=createTargetClassSet(aspect);
                proxyMap.put(cls,targetClassSet);
            }
        }
        return proxyMap;
    }
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClassSet=new HashSet<>();
        Class<? extends Annotation> annotation=aspect.value();
        if (!Objects.isNull(annotation)&&!annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }
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
