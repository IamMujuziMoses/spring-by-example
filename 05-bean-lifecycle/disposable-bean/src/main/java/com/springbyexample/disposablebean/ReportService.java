package com.springbyexample.disposablebean;

import org.springframework.beans.factory.DisposableBean;

/**
 * @author Mujuzi Moses
 */
public class ReportService implements DisposableBean {

    private boolean active = true;

    public boolean isActive() {
        return active;
    }

    @Override
    public void destroy() {
        active = false;
    }
}
