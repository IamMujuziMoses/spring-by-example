package com.springbyexample.validation;

import org.jspecify.annotations.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Mujuzi Moses
 */
public class UserValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");

        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            errors.rejectValue("email", "email.invalid");
        }
    }
}
