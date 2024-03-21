package top.zhixingege.framework;
import java.lang.String;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 定义配置文件的字段名
 */
public interface ConfigConstant {
    String CONFIG_FILE="summer.properties";
    String JDBC_DRIVER="summer.framework.jdbc.driver";
    String JDBC_URL="summer.framework.jdbc.url";
    String JDBC_USERNAME="summer.framework.jdbc.username";
    String JDBC_PASSWORD="summer.framework.jdbc.password";
    String APP_BASE_PACKAGE="summer.framework.app.base_package";
    String APP_JSP_PATH="summer.framework.app.jsp_path";
    String APP_ASSET_PATH="summer.framework.app.asset_path";

}
