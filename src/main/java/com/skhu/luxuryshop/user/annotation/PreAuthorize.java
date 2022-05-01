package com.skhu.luxuryshop.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* @Target = 어노테이션이 사용될 위치를 지정한다. */
@Target(ElementType.METHOD)
/* @Retention = 어느 시점까지 어노테이션의 메모리를 가져갈지 설정한다. */
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize {
    String[] roles();
}