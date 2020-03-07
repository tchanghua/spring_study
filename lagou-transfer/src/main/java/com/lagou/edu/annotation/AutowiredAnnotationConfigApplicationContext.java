package com.lagou.edu.annotation;

import com.alibaba.druid.util.StringUtils;
import com.lagou.edu.utils.GetClassesUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:AutowiredAnnotationConfigApplicationContext
 * @Description: TODO
 * @Auth: tch
 * @Date: 2020/3/6
 */
public class AutowiredAnnotationConfigApplicationContext {

    private String packageName;
    private ConcurrentHashMap<String,Object> beans = null;

    public AutowiredAnnotationConfigApplicationContext(String packageName,ConcurrentHashMap<String,Object> map) throws Exception {
        this.packageName = packageName;
        beans = map;
//        initBeans();
        initEntryFiled();
    }

    private void initEntryFiled() throws Exception {
        System.out.println(beans);
        //遍历所有的bean对象
        for(Map.Entry<String, Object> entry :beans.entrySet()){
            //判断属性上是否有加@Autowired
            Object value = entry.getValue();
            attriAssign(value);
        }
    }

    private void attriAssign(Object o) throws Exception {
        //使用反射获取当前类的所有属性
        Class<?> obj = o.getClass();
        Field[] fields = obj.getDeclaredFields();
        //判断当前属性是否存在注解
        for (Field f:fields) {
            Autowired annotation = f.getAnnotation(Autowired.class);
            if(annotation != null){
                String beanId = f.getName();
                Object bean = getBean(beanId);
                if(bean != null){
                    //使用反射暴力访问
                    f.setAccessible(true);
                    f.set(o,bean);
                    System.out.println("aaaa");
                }
            }
        }
    }

    public Object getBean (String beanId) throws Exception {
        if (StringUtils.isEmpty(beanId)){
            throw new Exception("bean Id 不能为空");
        }
        //从spring 容器初始化对像
        Object object = beans.get(beanId);
        if (object == null) {
            throw  new Exception("Class not found");
        }

        return object;
    }

    private void initBeans() throws Exception {
        List<Class<?>> classes = GetClassesUtils.getClasses(packageName);
        ConcurrentHashMap<String, Object> classAnnotation = getClassAnnotation(classes);
        if (classAnnotation == null || classAnnotation.isEmpty()){
            throw  new Exception("没有使用注解@Autowired");
        }
    }

    //判断有没有添加注解
    public  ConcurrentHashMap<String ,Object> getClassAnnotation(List<Class<?>> classes) throws InstantiationException, IllegalAccessException {
        for(Class<?> classInfo:classes){
            Autowired autowired = classInfo.getAnnotation(Autowired.class);
            if(autowired != null){
                String className = classInfo.getSimpleName();
                String beanId = GetClassesUtils.getClassName(className);
                Object o = newInstance(classInfo);
                beans.put(beanId,o);
            }
        }
        return  beans;
    }

    private Object newInstance(Class<?> classInfo) throws IllegalAccessException, InstantiationException {
        return classInfo.newInstance();
    }
}