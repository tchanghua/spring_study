package com.lagou.edu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName:Service
 * @Description: TODO
 * @Auth: tch
 * @Date: 2020/3/5
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface Service {

    String value() default "";
}
