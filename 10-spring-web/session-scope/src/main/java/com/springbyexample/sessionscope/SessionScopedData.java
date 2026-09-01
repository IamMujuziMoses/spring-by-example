package com.springbyexample.sessionscope;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author Mujuzi Moses
 */
@Component
@SessionScope
public class SessionScopedData {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }
}
