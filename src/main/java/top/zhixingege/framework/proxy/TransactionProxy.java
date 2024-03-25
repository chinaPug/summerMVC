package top.zhixingege.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zhixingege.framework.annotation.Transaction;
import top.zhixingege.framework.collections.SummerThreadLocal;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);
    private static final SummerThreadLocal<Boolean> FLAG_HOLDER=new SummerThreadLocal<Boolean>(){
        @Override
        public Boolean initialValue() {
            return false;
        }
    };
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag=FLAG_HOLDER.get();
        Method method=proxyChain.getTargetMethod();
        if (!flag&&method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try {
                //开始事务
                LOGGER.debug("begin transaction");
                result=proxyChain.doProxyChain();
                //提交事务
                LOGGER.debug("commit transaction");
            }catch (Exception e){
                //写回滚方法
                LOGGER.error("rollback transaction",e);
                throw e;
            }finally {
                FLAG_HOLDER.remove();
            }
        }else {
            result=proxyChain.doProxyChain();
        }
        return result;
    }

}
