package com.cc.database.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AddressRequestValidator.class)
public @interface AddressRequestValid {
    Class<?>[] groups() default {};

    String message() default "Invalid Add/Update Address Request";

    Class<? extends Payload> [] payload() default {};
}
