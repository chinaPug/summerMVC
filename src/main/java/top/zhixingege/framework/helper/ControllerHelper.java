package top.zhixingege.framework.helper;

import top.zhixingege.framework.annotation.Action;
import top.zhixingege.framework.annotation.Controller;
import top.zhixingege.framework.bean.Handler;
import top.zhixingege.framework.bean.Request;
import top.zhixingege.framework.utils.ArrayUtil;
import top.zhixingege.framework.utils.ClassUtil;
import top.zhixingege.framework.utils.CollectionUtil;
import top.zhixingege.framework.utils.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {
    private static final Map<Request, Handler> ACTION_MAP=new HashMap<>();
    static {
        Set<Class<?>> controllerClassSet= ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)){
            for (Class<?> aClass : controllerClassSet) {
                Method[] methods=aClass.getMethods();
                if (ArrayUtil.isNotEmpty(methods)){
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)){
                            Action action=method.getAnnotation(Action.class);
                            String mapping=action.value();
                            if (mapping.matches("\\w+:/\\w*")){
                                String[] array=mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(aClass,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static Handler getHandler(String requestMethod,String requestPath){
        return ACTION_MAP.get(new Request(requestMethod,requestPath));
    }
}
