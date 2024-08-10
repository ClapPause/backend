package com.clap.pause.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MemberRoleValidator.class)
public @interface MemberRoleValidation {

    String message() default "허용되지 않는 역할입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
