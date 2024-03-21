package top.zhixingege.framework.utils;

import java.util.Objects;

public final class ArrayUtil {
    public static boolean isEmpty(Object[] objects){
        return Objects.isNull(objects)||objects.length==0;
    }
    public static boolean isNotEmpty(Object[] objects){
        return !isEmpty(objects);
    }
}
