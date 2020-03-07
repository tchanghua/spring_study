package com.lagou.edu.annotation;

import com.lagou.edu.utils.GetClassesUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:AnnotationService
 * @Description: TODO
 * @Auth: tch
 * @Date: 2020/3/6
 */
public class ServiceAnnotationConfigApplicationContext {

    private String packageName;

    private ConcurrentHashMap<String ,Object> beans = null;

    public ServiceAnnotationConfigApplicationContext(String packageName,ConcurrentHashMap<String,Object> map) throws Exception {
        this.packageName = packageName;
        this.beans = map;
        initBeans();
    }

    private void initBeans() throws Exception{
        List<Class<?>> classes = GetClassesUtils.getClasses(packageName);
        ConcurrentHashMap<String, Object> classAnnotation = getClassAnnotation(classes);
        if (classAnnotation == null || classAnnotation.isEmpty()){
            throw  new Exception("没有使用注解@Service");
        }
    }

    //判断有没有添加注解
    public ConcurrentHashMap<String ,Object> getClassAnnotation(List<Class<?>> classes) throws IllegalAccessException, InstantiationException {
        for(Class<?> classInfo:classes){
            Service service = classInfo.getAnnotation(Service.class);
            if(service != null){
                String beanId = null;
                String value = service.value();
                if(StringUtils.isNotBlank(value)){
                    beanId = value;
                }else {
//                    String className = classInfo.getSimpleName();
                    String name = null;
                    Class<?>[] interfaces = classInfo.getInterfaces();
                    if(interfaces.length > 0) {
                         name = (classInfo.getInterfaces())[0].getSimpleName();
                    }else {
                        name = classInfo.getSimpleName();
                    }
//                    = classInfo.getName();
                    beanId = GetClassesUtils.getClassName(name);
                }
                Object o = classInfo.newInstance();
                beans.put(beanId,o);
            }
        }
        return  beans;
    }

    //获取bean
    public Object getBean(String beanId) throws Exception{
        if(StringUtils.isEmpty(beanId)){
            throw new Exception("beanId 不能为空");
        }
       Object obj = beans.get(beanId);
        if(obj == null){
            throw  new Exception("找不到对应的class信息");
        }
        return obj;
    }
}