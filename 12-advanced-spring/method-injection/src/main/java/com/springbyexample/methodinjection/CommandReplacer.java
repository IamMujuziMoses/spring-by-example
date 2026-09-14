package com.springbyexample.methodinjection;

import java.lang.reflect.Method;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.support.MethodReplacer;

/**
 * @author Mujuzi Moses
 */
public class CommandReplacer implements MethodReplacer {

    @Override
    public Object reimplement(@NonNull Object target, @NonNull Method method, Object @NonNull [] arguments) {

        return new Command("Hello from Method Injection!");
    }
}
