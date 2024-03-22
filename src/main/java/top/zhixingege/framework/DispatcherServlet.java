package top.zhixingege.framework;

import org.apache.commons.lang3.StringUtils;
import top.zhixingege.framework.bean.Data;
import top.zhixingege.framework.bean.Handler;
import top.zhixingege.framework.bean.Param;
import top.zhixingege.framework.bean.View;
import top.zhixingege.framework.helper.BeanHelper;
import top.zhixingege.framework.helper.ConfigHelper;
import top.zhixingege.framework.helper.ControllerHelper;
import top.zhixingege.framework.utils.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//urlPatterns = "/*"或者"/"都是任意匹配
//loadOnStartup参数可以控制预加载，即服务器启动时便创建，参数越小等级越高，>=0
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    //init用来注册Servlet
    @Override
    public void init(ServletConfig config) {
        //加载所有的Helper
        HelperLoader.init();
        //获取ServletContext对象，用来注册Servlet
        ServletContext servletContext=config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        //加入所有jsp路径下的文件
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAssetPath()+"*");
    }
    //HttpServlet的service方法是处理客户端请求的核心，当客户端（如浏览器）向Servlet发送请求时，web服务器（如tomcat）首先调用service方法
    //在原生的service方法中，会根据请求的HTTP方法进行相应的请求方法的调用处理，例如doGet、doPost、doDelete
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //请求方法和请求路径都封装在了HttpServletRequest中
        String requestMethod=req.getMethod().toLowerCase();
        String requestPath=req.getPathInfo();
        //根据请求放啊和请求路径可以得到相应的handler
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        //存在该handler则继续进行
        if (!Objects.isNull(handler)){
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //获取param类型的参数
            Map<String,Object> paramMap=new HashMap<>();
            Enumeration<String> paramNames=req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
            //处理body类型的参数
            String body= CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)){
                String[] params= StringUtils.split(body,"&");
                if (ArrayUtil.isNotEmpty(params)){
                    for (String param : params) {
                        String[] array=StringUtils.split(param,"=");
                        if (ArrayUtil.isNotEmpty(array)&&array.length==2){
                            paramMap.put(array[0],array[1]);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            Method actionMethod=handler.getActionMethod();
            //调用业务逻辑获取结果
            Object result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            //下面对结果进行封装，分视图View和数据Data
            if (result instanceof View view){
                String path=view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    if (path.endsWith("/")) {
                        resp.sendRedirect(req.getContextPath()+path);
                    }
                    else {
                        Map<String,Object> model=view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
                    }
                }
            }else if (result instanceof Data data){
                Object model=data.getModel();
                if (!Objects.isNull(model)){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer=resp.getWriter();
                    String json=JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
