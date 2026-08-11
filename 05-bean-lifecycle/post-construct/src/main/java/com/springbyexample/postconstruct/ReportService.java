package com.springbyexample.postconstruct;

import jakarta.annotation.PostConstruct;

/**
 * @author Mujuzi Moses
 */
public class ReportService {

    private boolean initialized;

    @PostConstruct
    public void initialize() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

}
