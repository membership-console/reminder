package cc.rits.membership.console.reminder.infrastructure.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RequestValidator.class})
public @interface RequestValidated {
    String message()

    default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        RequestValidated[] value();
    }
}
