package com.clap.pause.dto.validation;

import com.clap.pause.model.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<GenderValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        var genders = Gender.values();
        for (var gender : genders) {
            if (gender.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
