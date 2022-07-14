package com.portfolioSummary.core.validation.annotation;

import com.portfolioSummary.core.validation.validator.JsonStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = JsonStringValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonString {
    String message() default "String should be od type Json";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
