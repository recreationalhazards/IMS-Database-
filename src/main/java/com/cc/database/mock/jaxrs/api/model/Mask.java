package com.cc.database.mock.jaxrs.api.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask {
    int unmaskedPrefixLength() default 0;

    int unmaskedSuffixLength() default 0;

    char mask() default '*';



}
