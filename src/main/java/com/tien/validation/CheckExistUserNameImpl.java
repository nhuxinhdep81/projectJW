package com.tien.validation;

import com.tien.entity.Student;
import com.tien.service.AuthenticationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckExistUserNameImpl implements ConstraintValidator<CheckExistUserName, String> {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Student student = authenticationService.checkExistUserName(username);
        if (student == null) {
            return true;
        }
        return false;
    }
}
