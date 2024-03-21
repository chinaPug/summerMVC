package top.zhixingege.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义代理链
 */
public class ProxyChain {
    //被代理的目标类
    private final Class<?> targetClass;
    //被代理的目标对象
    private final Object targetObject;
    //被代理的目标方法
    private final Method targetMethod;
    //代理方法
    private final MethodProxy methodProxy;
    //方法参数
    private final Object[] methodParams;
    //代理列表
    private List<Proxy> proxyList=new ArrayList<>();
    //代理索引
    private int proxyIndex=0;

    /**
     * 构造函数
     * @param targetClass
     * @param targetObject
     * @param targetMethod
     * @param methodProxy
     * @param methodParams
     * @param proxyList
     */
    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList=proxyList;
    }


    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    /**
     * 执行代理链
     * @return
     * @throws Throwable
     */
    public Object doProxyChain() throws Throwable{
        Object methodResult;
        //proxyIndex是代理计数器，未越界，就从List里取；否则，就调用methodProxy的invokeSuper方法
        if (proxyIndex<proxyList.size()){
            methodResult=proxyList.get(proxyIndex++).doProxy(this);
        }
        else {
            methodResult=methodProxy.invokeSuper(targetObject,methodParams);
        }
        return methodResult;
    }
}
