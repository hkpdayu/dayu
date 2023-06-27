package com.xxxx.crm.annoation;

import java.lang.annotation.*;

/**
 * 去定义 方法需要的对应资源的权限码
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String code() default "";
}
