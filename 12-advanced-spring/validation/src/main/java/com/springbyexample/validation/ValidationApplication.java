package com.springbyexample.validation;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Mujuzi Moses
 */
public class ValidationApplication {

    public static void main(String[] args) {
        User user = new User("Moses", "moses@example.com");

        Validator validator = new UserValidator();

        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        System.out.println("Validation errors: " + errors.getErrorCount());
    }
}
