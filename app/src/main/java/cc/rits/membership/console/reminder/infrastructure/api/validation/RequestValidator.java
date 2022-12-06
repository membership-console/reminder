package cc.rits.membership.console.reminder.infrastructure.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import cc.rits.membership.console.reminder.infrastructure.api.request.IRequest;

public class RequestValidator implements ConstraintValidator<RequestValidated, IRequest> {

    @Override
    public void initialize(final RequestValidated constraintAnnotation) {}

    @Override
    public boolean isValid(final IRequest request, final ConstraintValidatorContext constraintValidatorContext) {
        request.validate();
        return true;
    }

}
