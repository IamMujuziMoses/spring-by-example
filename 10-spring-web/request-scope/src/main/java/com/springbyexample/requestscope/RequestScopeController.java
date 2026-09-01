package com.springbyexample.requestscope;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mujuzi Moses
 */
@RestController
public class RequestScopeController {

    private final RequestScopedData requestScopedData;

    public RequestScopeController(RequestScopedData requestScopedData) {
        this.requestScopedData = requestScopedData;
    }

    @GetMapping("/api/request")
    public String getRequestScopeId() {
        return requestScopedData.getId();
    }
}