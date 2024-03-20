package top.zhixingege.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器
 */
public class ProxyManager {
    /**
     * 根据目标类和代理列表创建代理
     * @param targetClass
     * @param proxyList
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
        return (T) Enhancer.create(targetClass, (MethodInterceptor) (o, method, objects, methodProxy) -> new ProxyChain(targetClass,o,method,methodProxy,objects,proxyList).doProxyChain());
    }
}
