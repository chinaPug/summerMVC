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

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext=config.getServletContext();
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAssetPath()+"*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod=req.getMethod().toLowerCase();
        String requestPath=req.getPathInfo();
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (!Objects.isNull(handler)){
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            Map<String,Object> paramMap=new HashMap<>();
            Enumeration<String> paramNames=req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }
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
            Object result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
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
