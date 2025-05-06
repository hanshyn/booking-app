package com.booking.bookingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValueObj = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValueObj = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        return Objects.equals(fieldValueObj, fieldMatchValueObj);
    }
}
