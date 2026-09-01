package com.springbyexample.requestscope;

import java.util.UUID;

import org.springframework.web.context.annotation.RequestScope;
import org.springframework.stereotype.Component;

/**
 * @author Mujuzi Moses
 */
@Component
@RequestScope
public class RequestScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}