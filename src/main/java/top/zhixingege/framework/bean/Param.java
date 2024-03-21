package top.zhixingege.framework.bean;

import top.zhixingege.framework.utils.CastUtil;

import java.util.Map;
import java.util.Objects;

public class Param {
    private Map<String, Object> paramMap;
    public Param(Map<String,Object> paramMap){
        this.paramMap=paramMap;
    }
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }
    public Map<String,Object> getMap(){
        return paramMap;
    }
}
