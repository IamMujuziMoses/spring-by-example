package com.springbyexample.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Mujuzi Moses
 */
public class ValidationApplicationTest {

    @Test
    void shouldValidateValidUser() {
        User user = new User("Moses", "moses@example.com");

        Validator validator = new UserValidator();

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void shouldRejectMissingName() {
        User user = new User("", "moses@example.com");

        Validator validator = new UserValidator();

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("name"));
    }

    @Test
    void shouldRejectMissingEmail() {
        User user = new User("Moses", "");

        Validator validator = new UserValidator();

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("email"));
    }

    @Test
    void shouldSupportUser() {
        Validator validator = new UserValidator();

        assertTrue(validator.supports(User.class));
    }

    @Test
    void shouldNotSupportString() {
        Validator validator = new UserValidator();

        assertFalse(validator.supports(String.class));
    }
}
