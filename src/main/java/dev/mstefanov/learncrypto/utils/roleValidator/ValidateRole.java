package dev.mstefanov.learncrypto.utils.roleValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateRole {

    String[] acceptedValues();

    String message() default "Invalid user role.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
