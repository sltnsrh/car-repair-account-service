package com.salatin.account.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<ValidMobile, String> {
    private static final String PATTERN_FOR_UA =
            "^\\+{1}38[ -.(]?0[ -.]?\\d{2}[ -.)]?\\d{3}[ -.]?\\d{2}[ -.]?\\d{2}$";

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile(PATTERN_FOR_UA);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }
}
