package com.springbyexample.applicationscope;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author Mujuzi Moses
 */
@Component
@ApplicationScope
public class ApplicationScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}