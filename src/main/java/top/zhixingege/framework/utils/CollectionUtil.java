package top.zhixingege.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

public final class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection){
        return collection.isEmpty();
    }
    public static boolean isEmpty(Map<?,?> map){
        return map.isEmpty();
    }
    public  static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }
    public  static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}
