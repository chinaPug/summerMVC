package top.zhixingege.framework.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(ClassUtil.class);
    /**
     * 获取类加载器（获取当前线程下的类加载器）
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
    /**
     * 加载类
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls=Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            LOGGER.error("load class failure!!!",e);
            throw new RuntimeException(e);
        }
        return cls;
    }
    /**
     * 获取指定包名下的所有类
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<>();
        try {
            Enumeration<URL> urls=getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url=urls.nextElement();
                if (!Objects.isNull(url)){
                    //获取改url的协议名
                    String protocol=url.getProtocol();
                    //如果协议名是文件
                    if (protocol.equals("file")){
                        //url字符串编码中，空格由%20表示
                        String packagePath=url.getPath().replaceAll("%20"," ");
                        addClass(classSet,packagePath,packageName);
                    }
                    else if (protocol.equals("jar")){
                        JarURLConnection jarURLConnection=(JarURLConnection) url.openConnection();
                        if (!Objects.isNull(jarURLConnection)){
                            JarFile jarFile=jarURLConnection.getJarFile();
                            if (!Objects.isNull(jarFile)){
                                Enumeration<JarEntry> jarEntryEnumeration=jarFile.entries();
                                while (jarEntryEnumeration.hasMoreElements()){
                                    JarEntry jarEntry=jarEntryEnumeration.nextElement();
                                    String jarEntryName=jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")){
                                        String className=jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get class set failure",e);
            throw new RuntimeException(e);
        }
        return classSet;
    }
    /**
     * addClass
     */
    public static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files=new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile()&&pathname.getName().endsWith(".class"))||pathname.isDirectory();
            }
        });
        for(File file:files){
            String fileName=file.getName();
            if (file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtil.isEmpty(packageName)){
                    className= packageName + "." + className;
                }
                doAddClass(classSet,className);
            }else {
                String subPackagePath=fileName;
                String subPackageName=fileName;
                if (StringUtil.isNotEmpty(packagePath)){
                    subPackagePath=packagePath+"/"+subPackagePath;
                }
                if (StringUtil.isNotEmpty(packageName)){
                    subPackageName=packageName+"/"+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }
    /**
     * doAddClass
     */
    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls=loadClass(className,false);
        classSet.add(cls);
    }
}
