package com.springbyexample.applicationscope;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class ApplicationScopeController {

    private final ApplicationScopedData applicationScopedData;

    public ApplicationScopeController(ApplicationScopedData applicationScopedData) {
        this.applicationScopedData = applicationScopedData;
    }

    @GetMapping("/api/application")
    public String getApplicationScopeId() {
        return applicationScopedData.getId();
    }
}
