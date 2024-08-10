package com.clap.pause.dto.validation;

import com.clap.pause.model.MemberRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberRoleValidator implements ConstraintValidator<MemberRoleValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        var memberRoles = MemberRole.values();
        for (var memberRole : memberRoles) {
            if (memberRole.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
