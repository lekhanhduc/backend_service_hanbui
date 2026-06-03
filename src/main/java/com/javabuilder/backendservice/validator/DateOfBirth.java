package com.javabuilder.backendservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface DateOfBirth {

    String message() default "DateOfBirth must greater than 18 age";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
