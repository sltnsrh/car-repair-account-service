package com.salatin.account.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface ValidMobile {
    String message() default "Invalid phone number, please, try again";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
