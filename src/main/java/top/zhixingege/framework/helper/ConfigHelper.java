package top.zhixingege.framework.helper;

import top.zhixingege.framework.ConfigConstant;
import top.zhixingege.framework.utils.PropsUtils;

import java.util.Properties;

/**
 * 配置文件助手类：
 *  功能：用于获取配置文件中用户的配置值
 */
public final class ConfigHelper {
    private static final Properties CONFIG_PROPS= PropsUtils.loadProps(ConfigConstant.CONFIG_FILE);
    /**
     * 获取JDBC驱动
     */
    public static String getJdbcDriver(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }
    /**
     * 获取JDBC路径
     */
    public static String getJdbcUrl(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }
    /**
     * 获得JDBC用户名
     */
    public static String getJdbcUsername(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }
    /**
     * 获得JDBC密码
     */
    public static String getJdbcPassword(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }
    /**
     * 获得应用基础包
     */
    public static String getAppBasePackage(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }
    /**
     * 获得应用JSP路径 hasDefault
     */
    public static String getAppJspPath(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }
    /**
     * 获得静态资源路径 hasDefault
     */
    public static String getAssetPath(){
        return PropsUtils.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/assert/");
    }
}
