package top.zhixingege.framework.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SummerThreadLocal<T> {
    private Map<Thread,T> threadLocalMap= Collections.synchronizedMap(new HashMap<>());
    public void set(T value){
        threadLocalMap.put(Thread.currentThread(),value);
    }
    public T get(){
        Thread key= Thread.currentThread();
        T value=threadLocalMap.get(key);
        if (Objects.isNull(value)&&!threadLocalMap.containsKey(key)){
            value=initialValue();
            threadLocalMap.put(key,value);
        }
        return value;
    }
    public void remove(){
        threadLocalMap.remove(Thread.currentThread());
    }
    public T initialValue(){
        return null;
    }
}
