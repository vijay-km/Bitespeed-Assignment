package com.Bitespeed.IdentityReconciliation.customValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContactValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidContact {
    String message() default "Either email or mobile number must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
