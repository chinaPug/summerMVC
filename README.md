# 文件说明
1. 配置项相关：  
1.1 ConfigConstant：提供相关配置项常量
   - CONFIG_FILE：配置文件路径
   - JDBC_DRIVER：java数据库连接路径（JDBC：Java database connectivity）
   - JDBC_URL：JDBC连接路径
   - JDBC_USERNAME：JDBC连接账号
   - JDBC_PASSWORD：JDBC连接密码
   - APP_BASE_PACKAGE：开发项目的基础包路径
   - APP_JSP_PATH：JSP基础路径
   - APP_ASSET_PATH：静态资源路径  
1.2 PropsUtil：读取开发项目的配置项文件   
1.3 ConfigHelper：使用PropsUtil类获取配置项的值
2. 注解相关：   
	- Controller：标记控制器
	- Action：标记请求路径与方法类型
	- Service：标记服务逻辑器
	- Inject：依赖注入
3. 类操作相关：
   3.1 ClassUtil：类加载器相关
	   - 获取类加载器
	   - 加载类
	   - 获取指定包名下所有类
	 3.2 ClassHelper：类操作助手
     - 通过ClassUtil获取基础包下所有类
     - 获取基础包下所有的类
     - 获取Controller的bean
     - 获取Service的bean
     - 获取所有的bean
4. Bean容器：
	4.1 ReflectionUtil：反射工具类
   - 创建实例
   - 调用方法
   - 设置成员变量值
	4.2 BeanHelper：Bean助手类，其实是一个Map，用来映射Class对象->Bean实例，在静态代码块中实现Bean实例的创建
     -获取Map映射
     -通过Class获取Bean实例
5.依赖注入相关，处理注解与Bean之间的联系
	5.1 IocHelper：依赖注入助手类，通过静态代码块的运行实现，首先利用BeanHelper得到BeanMap，
     						通过遍历BeanMap找到Inject注解的字段，从map中找到实例，并利用反射工具类ReflectionUtil
     						进行初始化。
6.控制器操作相关：
	6.1Request：封装请求信息，即请求路径RequestMethod与请求方法RequestPath
	6.2Handler：封装Action信息，即控制器类ControllerClass和action方法ActionMethod
	6.3ControllerHelper：用于存放请求和Handler之间的映射，即Request->Handler,并提供get方法
7.初始化框架：
	7.1HelperLoad：包含一个init方法，循环使用ClassUtil将四个Helper进行类加载：ConfigHelper、
								ClassHelper、BeanHelper和ControllerHelper
