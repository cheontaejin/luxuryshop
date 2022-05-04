package com.skhu.luxuryshop.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* @Target = 어노테이션이 사용될 위치를 지정한다. */
/* ElementType.METHOD = 메소드에만 어노테이션을 붙일 수 있다. */
@Target(ElementType.METHOD)
/* @Retention = 어느 시점까지 어노테이션의 메모리를 가져갈지 설정한다. */
/* RetentionPolicy.RUNTIME
= JVM이 실제 클래스의 자바 바이트코드를 해석해서 동작하는 Runtime단계에서 메모리가 유지되고,
Runtime이 종료되면 메모리도 사라진다. */
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize {
    String[] roles();
}