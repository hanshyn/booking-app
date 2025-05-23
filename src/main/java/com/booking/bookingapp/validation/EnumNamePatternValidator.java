package com.booking.bookingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EnumNamePatternValidator implements ConstraintValidator<EnumNamePattern, Enum<?>> {
    private Pattern pattern;

    @Override
    public void initialize(EnumNamePattern bookingStatus) {
        try {
            pattern = Pattern.compile(bookingStatus.regexp());
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException("regexp is invalid", e);
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Matcher matcher = pattern.matcher(value.name());
        return matcher.matches();
    }
}
