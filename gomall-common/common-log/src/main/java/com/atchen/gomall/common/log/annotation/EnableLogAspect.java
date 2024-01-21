package com.atchen.gomall.common.log.annotation;

import com.atchen.gomall.common.log.aspect.LogAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(value = LogAspect.class)            // Import the log aspect class into the Spring container using the @Import
public @interface EnableLogAspect {
}
