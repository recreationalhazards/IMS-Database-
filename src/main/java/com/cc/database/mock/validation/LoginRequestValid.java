package com.cc.database.mock.validation;

import org.springframework.context.annotation.Profile;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Profile("mock")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginRequestValidator.class)
public @interface LoginRequestValid {
    Class<?>[] groups() default {};

    String message() default "Invalid login request";

    Class<? extends Payload> [] payload() default {};

}
