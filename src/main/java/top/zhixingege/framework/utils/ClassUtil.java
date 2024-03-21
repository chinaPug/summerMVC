package top.zhixingege.framework.utils;

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

/**
 * 类操作工具类：
 *  简介：用于加载基础包名下所有的类，即使用了某注解的类、实现了某接口的类或者继承了父类的子类
 *  功能：获取类加载器、加载类和获取指定包名下的所有类
 */
public final class ClassUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器（获取当前线程下的类加载器）
     * @return 获取当前线程下的类加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载指定类
     * @param className 加载的类名
     * @param isInitialized 是否初始化，有的操作不需要初始化，例如仅仅获取该类的属性、方法名。因此，不进行初始化可以节省资源
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls=Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            LOGGER.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 获取指定包名下的所有类（注意，这里最终要获取的是.class文件，因此，需要递归去寻找）
     * @param packageName 指定的包名
     * @return 以Set封装的类
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<>();
        try {
            /*
                Enumeration是一个远古接口，JDK1.0就已经出现，早于迭代器Iter。用于迭代远古集合，Vector或者HashTable；仅仅存在两个方法，是否存在下一个元素和递归获取下一个元素。
             */
            //这里使用了当前的类加载器去获取资源，值得注意的是，在java中“.”表示运行class文件时的目录，而“/”则代表磁盘分区，因此，在获取资源时，需要进行替换。
            Enumeration<URL> urls=getClassLoader().getResources(packageName.replaceAll("\\.","/"));
            while (urls.hasMoreElements()){
                //为什么还要判断空呢？？？
                URL url=urls.nextElement();
                if (!Objects.isNull(url)){
                    //获取改url的协议名
                    String protocol=url.getProtocol();
                    //在这里需要进行资源区分，文件与jar包，其意义在于，文件的话，需要继续递归，而jar包可以直接插入到Set
                    if (protocol.equals("file")){
                        //url字符串编码中，空格由%20表示
                        String packagePath=url.getPath().replaceAll("%20"," ");
                        //调用递归方法
                        addClass(classSet,packagePath,packageName);
                    }
                    else if (protocol.equals("jar")){
                        //先通过java网络对url进行连接，再获取jar资源文件
                        JarURLConnection jarURLConnection=(JarURLConnection) url.openConnection();
                        if (!Objects.isNull(jarURLConnection)){
                            JarFile jarFile=jarURLConnection.getJarFile();
                            if (!Objects.isNull(jarFile)){
                                //对获取的jar文件进一步拆分
                                Enumeration<JarEntry> jarEntryEnumeration=jarFile.entries();
                                while (jarEntryEnumeration.hasMoreElements()){
                                    JarEntry jarEntry=jarEntryEnumeration.nextElement();
                                    String jarEntryName=jarEntry.getName();
                                    //字符串尾部以.class结尾的判断
                                    if (jarEntryName.endsWith(".class")){
                                        //注意，这里依然是磁盘路径与class路径的互相转换
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
     * 添加java类的递归方法
     * @param classSet
     * @param packagePath
     * @param packageName
     */
    public static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files=new File(packagePath).listFiles(pathname -> (pathname.isFile()&&pathname.getName().endsWith(".class"))||pathname.isDirectory());
        for(File file:files){
            String fileName=file.getName();
            if (file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtil.isNotEmpty(packageName)){
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
                    subPackageName=packageName+"."+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }

    }

    /**
     * 按照类名将类加载并加入Set
     * @param classSet
     * @param className
     */
    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls=loadClass(className,false);
        classSet.add(cls);
    }
}
