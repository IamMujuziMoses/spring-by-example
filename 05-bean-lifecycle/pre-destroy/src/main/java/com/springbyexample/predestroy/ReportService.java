package com.springbyexample.predestroy;

import jakarta.annotation.PreDestroy;

/**
 * @author Mujuzi Moses
 */
public class ReportService {

    private boolean active = true;

    @PreDestroy
    public void cleanup() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
