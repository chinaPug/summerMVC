package top.zhixingege.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 */
public abstract class AspectProxy implements Proxy{
    private static final Logger LOGGER= LoggerFactory.getLogger(AspectProxy.class);

    /**
     * 执行代理
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Class<?> cls=proxyChain.getTargetClass();
        Method method=proxyChain.getTargetMethod();
        Object[] params=proxyChain.getMethodParams();
        begin();
        try {
            //intercept 拦截
            if (intercept(cls,method,params)){
                before(cls,method,params);
                result=proxyChain.doProxyChain();
                after(cls,method,params,result);
            }else {
                result=proxyChain.doProxyChain();
            }
        }catch (Exception e){
            LOGGER.error("do proxy failure",e);
            error(cls,method,params,e);
            throw e;
        }finally {
            end();
        }
        return result;
    }
    public void begin(){}

    public boolean intercept(Class<?>cls,Method method,Object[] params)throws Throwable{
        return true;
    }
    public void before(Class<?> cls,Method method,Object[] params)throws Throwable{}

    public void after(Class<?> cls,Method method,Object[] params,Object result)throws Throwable{}

    /**
     * AfterThrowing，抛出增强
     * @param cls
     * @param method
     * @param params
     * @param throwable
     */
    public void error(Class<?> cls,Method method,Object[] params,Throwable throwable){}

    /**
     * 返回后增强，可以理解为Finally增强
     */
    public void end(){}
}
