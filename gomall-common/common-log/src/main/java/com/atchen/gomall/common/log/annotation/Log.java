package com.atchen.gomall.common.log.annotation;

import com.atchen.gomall.common.log.enums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {     // Custom operation log recording annotation

    public String title() ;								// Module name
    public OperatorType operatorType() default OperatorType.MANAGE;	// Operator category
    public int businessType() ;     // Business type (0: other, 1: add, 2: modify, 3: delete)
    public boolean isSaveRequestData() default true;   // Whether to save the request parameters
    public boolean isSaveResponseData() default true;  // Whether to save the response parameters

}
