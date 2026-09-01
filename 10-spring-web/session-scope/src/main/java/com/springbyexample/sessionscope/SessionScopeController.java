package com.springbyexample.sessionscope;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class SessionScopeController {

    private final SessionScopedData sessionScopedData;

    public SessionScopeController(SessionScopedData sessionScopedData) {
        this.sessionScopedData = sessionScopedData;
    }

    @GetMapping("/api/session")
    public String getSessionScopeId() {
        return sessionScopedData.getId();
    }
}
