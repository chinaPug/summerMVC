# 文件说明

## 1. 配置项相关
- **1.1 ConfigConstant**：提供相关配置项常量。
  - `CONFIG_FILE`：配置文件路径。
  - `JDBC_DRIVER`：Java数据库连接驱动路径（JDBC: Java Database Connectivity）。
  - `JDBC_URL`：JDBC连接URL。
  - `JDBC_USERNAME`：JDBC连接用户名。
  - `JDBC_PASSWORD`：JDBC连接密码。
  - `APP_BASE_PACKAGE`：开发项目的基础包路径。
  - `APP_JSP_PATH`：JSP基础路径。
  - `APP_ASSET_PATH`：静态资源路径。

- **1.2 PropsUtil**：用于读取开发项目的配置项文件。

- **1.3 ConfigHelper**：使用PropsUtil类获取配置项的值。

## 2. 注解相关
- `Controller`：标记控制器。
- `Action`：标记请求路径与方法类型。
- `Service`：标记服务逻辑器。
- `Inject`：依赖注入。

## 3. 类操作相关
- **3.1 ClassUtil**：类加载器相关。
  - 获取类加载器。
  - 加载类。
  - 获取指定包名下所有类。

- **3.2 ClassHelper**：类操作助手。
  - 通过ClassUtil获取基础包下所有类。
  - 获取基础包下所有的Controller和Service类。
  - 获取所有的Bean。

## 4. Bean容器
- **4.1 ReflectionUtil**：反射工具类。
  - 创建实例。
  - 调用方法。
  - 设置成员变量值。

- **4.2 BeanHelper**：Bean助手类，实质是一个映射Class对象到Bean实例的Map。在静态代码块中实现Bean实例的创建。
  - 获取Map映射。
  - 通过Class获取Bean实例。

## 5. 依赖注入相关
- **5.1 IocHelper**：依赖注入助手类。首先利用BeanHelper得到BeanMap，然后遍历BeanMap找到带有`Inject`注解的字段，从map中找到实例，并利用反射工具类ReflectionUtil进行初始化。

## 6. 控制器操作相关
- **6.1 Request**：封装请求信息，包括请求路径（RequestMethod）与请求方法（RequestPath）。
- **6.2 Handler**：封装Action信息，包括控制器类（ControllerClass）和action方法（ActionMethod）。
- **6.3 ControllerHelper**：用于存放请求和Handler之间的映射（Request -> Handler），并提供查询方法。

## 7. 初始化框架
- **7.1 HelperLoader**：包含一个`init`方法，通过ClassUtil循环加载ConfigHelper、ClassHelper、BeanHelper和ControllerHelper类。

## 8. 请求转发器
- **8.1 Param**：封装HttpServletRequest对象中的请求参数
- **8.2 View**：封装控制器的返回值是视图的对象
- **8.3 Data**：封装控制器的返回值是数据的对象，框架将该对象写入HttpServletResponse对象中，输出到浏览器
- **8.4 DispatcherServlet**：MCV核心类，仅重写了`init`和`service`方法，前者用于加载类和注册Servlet；后者用于进行业务逻辑处理
